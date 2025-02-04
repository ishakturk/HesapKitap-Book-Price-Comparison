package com.example.kitap.service.dbservice;

import com.example.kitap.entity.BookDetailsEntity;
import com.example.kitap.entity.BookPriceEntity;
import com.example.kitap.entity.CustomerEntity;
import com.example.kitap.entity.WishlistEntity;
import com.example.kitap.model.BookDetailsModel;
import com.example.kitap.model.BookPriceModel;
import com.example.kitap.repository.BookDetailsRepository;
import com.example.kitap.repository.BookPriceRepository;
import com.example.kitap.repository.CustomerRepository;
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
public class WishlistService {
    private final BookDetailsRepository bookDetailsRepo;
    private final WishlistRepository wishlistRepo;
    private final BookPriceRepository bookPriceRepo;
    private final BookConverterService converter;
    private final CustomerRepository customerRepo;
    private final PriceComparatorService priceComparatorService;

    @Autowired
    public WishlistService(BookDetailsRepository bookDetailsRepo,
                           WishlistRepository wishlistRepo,
                           BookPriceRepository bookPriceRepo,
                           BookConverterService converter,
                           CustomerRepository customerRepo,
                           PriceComparatorService priceComparatorService) {
        this.bookDetailsRepo = bookDetailsRepo;
        this.wishlistRepo = wishlistRepo;
        this.bookPriceRepo = bookPriceRepo;
        this.converter = converter;
        this.customerRepo = customerRepo;
        this.priceComparatorService = priceComparatorService;
    }

    @Transactional
    public void addToWishlistWithPrices(Long customerId, BookDetailsModel bookModel, List<BookPriceModel> priceModels) {
        // 1. Müşteriyi bul
        CustomerEntity customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı: " + customerId));

        // 2. Kitap var mı kontrol et/Yeni oluştur
        BookDetailsEntity bookEntity = bookDetailsRepo.findByIsbn(bookModel.getIsbn())
                .orElseGet(() -> {
                    BookDetailsEntity newBook = converter.toEntity(bookModel);
                    return bookDetailsRepo.save(newBook);
                });

        // 3. Fiyatları kaydet
        priceModels.forEach(priceModel -> {
            BookPriceEntity priceEntity = new BookPriceEntity();
            priceEntity.setSiteName(priceModel.getSiteName());
            priceEntity.setPrice(priceModel.getPrice());
            priceEntity.setProvider(priceModel.getBookUrl());
            priceEntity.setBook(bookEntity); // ISBN ilişkisi
            priceEntity.setLastUpdated(LocalDateTime.now());
            bookPriceRepo.save(priceEntity);
        });

        // 4. Wishlist'e ekle (eğer yoksa)
        if (!wishlistRepo.existsByCustomerAndBook(customer, bookEntity)) {
            WishlistEntity wishlist = new WishlistEntity();
            wishlist.setCustomer(customer);
            wishlist.setBook(bookEntity);
            wishlistRepo.save(wishlist);
        }
    }

    public List<WishlistEntity> getWishlistByCustomer(CustomerEntity customer) {
        return wishlistRepo.findByCustomer(customer);
    }

    @Transactional
    public void removeFromWishlist(Long id, String isbn) {
        wishlistRepo.deleteByCustomerIdAndBookIsbn(id, isbn);
    }

    public List<BookDetailsModel> getWishlistBooksForUser(Long customerId) {
        List<WishlistEntity> wishlistItems = wishlistRepo.findByCustomerIdWithDetails(customerId);

        return wishlistItems.stream().map(item -> {
            BookDetailsModel model = converter.toModel(item.getBook());

            // Get only latest prices
            List<BookPriceModel> priceModels = bookPriceRepo
                    .findLatestByIsbn(item.getBook().getIsbn()).stream()
                    .map(price -> new BookPriceModel(
                            price.getSiteName(),
                            price.getPrice(),
                            price.getProvider(),
                            price.getIsCheapest()
                    ))
                    .collect(Collectors.toList());

            model.setPrices(priceModels);
            return model;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateAllPricesForCustomer(Long customerId) {
        List<WishlistEntity> wishlistItems = wishlistRepo.findByCustomerIdWithDetails(customerId);

        wishlistItems.forEach(item -> {
            String isbn = item.getBook().getIsbn();
            List<BookPriceModel> newPrices = priceComparatorService.fetchPricesFromAllSites(isbn);

            // Mark all existing prices as not latest
            bookPriceRepo.markAllAsNotLatest(isbn);

            // Save new prices with latest flag
            List<BookPriceEntity> savedPrices = new ArrayList<>();
            newPrices.forEach(priceModel -> {
                BookPriceEntity priceEntity = new BookPriceEntity();
                priceEntity.setBook(item.getBook());
                priceEntity.setSiteName(priceModel.getSiteName());
                priceEntity.setPrice(priceModel.getPrice());
                priceEntity.setProvider(priceModel.getBookUrl());
                priceEntity.setLastUpdated(LocalDateTime.now());
                priceEntity.setIsLatest(true);
                savedPrices.add(bookPriceRepo.save(priceEntity));
            });

            // Find and mark cheapest price
            savedPrices.stream()
                    .min(Comparator.comparingDouble(BookPriceEntity::getPrice))
                    .ifPresent(cheapest -> {
                        cheapest.setIsCheapest(true);
                        bookPriceRepo.save(cheapest);
                    });
        });
    }
}