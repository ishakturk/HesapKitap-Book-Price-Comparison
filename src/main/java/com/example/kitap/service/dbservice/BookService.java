package com.example.kitap.service.dbservice;

import com.example.kitap.entity.BookDetailsEntity;
import com.example.kitap.entity.BookPriceEntity;
import com.example.kitap.model.BookDetailsModel;
import com.example.kitap.model.BookPriceModel;
import com.example.kitap.repository.BookDetailsRepository;
import com.example.kitap.repository.BookPriceRepository;
import com.example.kitap.repository.WishlistRepository;
import com.example.kitap.service.BookConverterService;
import com.example.kitap.service.scrapers.PriceComparatorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookDetailsRepository bookDetailsRepo;
    private final WishlistRepository wishlistRepo;
    private final BookConverterService converter;
    private final BookPriceRepository bookPriceRepo;
    private final PriceComparatorService priceComparatorService;

    @Autowired
    public BookService(BookDetailsRepository bookDetailsRepo,
                       WishlistRepository wishlistRepo,
                       BookConverterService converter,
                       PriceComparatorService priceComparatorService,
                       BookPriceRepository bookPriceRepo) {
        this.bookDetailsRepo = bookDetailsRepo;
        this.wishlistRepo = wishlistRepo;
        this.converter = converter;
        this.bookPriceRepo = bookPriceRepo;
        this.priceComparatorService = priceComparatorService;
    }

    public List<BookDetailsModel> getTrendingBooks() {
        return wishlistRepo.findTopTrendingBooks().stream()
                .map(result -> converter.toModel((BookDetailsEntity) result[0]))
                .collect(Collectors.toList());
    }

    public List<BookDetailsModel> getRandomBooks() {
        return bookDetailsRepo.findRandomBooks().stream()
                .map(converter::toModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public void refreshBookDetails(String isbn) {
        // Find or create the book
        BookDetailsEntity bookEntity = bookDetailsRepo.findByIsbn(isbn)
                .orElseGet(() -> {
                    BookDetailsModel bookModel = priceComparatorService.fetchBookDetailsByIsbn(isbn);
                    if (bookModel == null) {
                        throw new RuntimeException("Book not found");
                    }
                    return bookDetailsRepo.save(converter.toEntity(bookModel));
                });

        // Mark old prices as not latest
        bookPriceRepo.markAllAsNotLatest(isbn);

        // Get and save new prices
        List<BookPriceEntity> savedPrices = new ArrayList<>();
        priceComparatorService.fetchPricesFromAllSites(isbn).forEach(priceModel -> {
            BookPriceEntity priceEntity = new BookPriceEntity();
            priceEntity.setBook(bookEntity);
            priceEntity.setPrice(priceModel.getPrice());
            priceEntity.setSiteName(priceModel.getSiteName());
            priceEntity.setProvider(priceModel.getBookUrl());
            priceEntity.setLastUpdated(LocalDateTime.now());
            priceEntity.setIsLatest(true);
            // Save and collect the entity
            savedPrices.add(bookPriceRepo.save(priceEntity));
        });

        // Find and mark the cheapest price among new entries
        savedPrices.stream()
                .min(Comparator.comparingDouble(BookPriceEntity::getPrice))
                .ifPresent(cheapest -> {
                    cheapest.setIsCheapest(true);
                    bookPriceRepo.save(cheapest);
                });
    }

    public List<BookPriceModel> getCheapestPriceHistory(String isbn) {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

        return bookPriceRepo.findDailyMinPrices(isbn, oneYearAgo)
                .stream()
                .sorted(Comparator.comparing(BookPriceEntity::getLastUpdated))
                .map(converter::toPriceModelWithDate)
                .collect(Collectors.toList());
    }
}