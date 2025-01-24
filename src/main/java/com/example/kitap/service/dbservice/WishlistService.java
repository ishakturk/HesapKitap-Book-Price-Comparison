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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.util.Assert;

@Service
public class WishlistService {
    private final BookDetailsRepository bookDetailsRepo;
    private final WishlistRepository wishlistRepo;
    private final BookPriceRepository bookPriceRepo;
    private final BookConverterService converter;
    private final CustomerRepository customerRepo;

    @Autowired
    public WishlistService(BookDetailsRepository bookDetailsRepo,
                           WishlistRepository wishlistRepo,
                           BookPriceRepository bookPriceRepo,
                           BookConverterService converter,
                           CustomerRepository customerRepo) {
        this.bookDetailsRepo = bookDetailsRepo;
        this.wishlistRepo = wishlistRepo;
        this.bookPriceRepo = bookPriceRepo;
        this.converter = converter;
        this.customerRepo = customerRepo;
    }

    // WishlistService.java
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
            priceEntity.setProvider(priceModel.getSiteName());
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
}