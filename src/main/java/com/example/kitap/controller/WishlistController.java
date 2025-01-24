package com.example.kitap.controller;

import com.example.kitap.entity.CustomerEntity;
import com.example.kitap.model.BookDetailsModel;
import com.example.kitap.model.BookPriceModel;
import com.example.kitap.model.BookRequest;
import com.example.kitap.service.dbservice.CustomerService;
import com.example.kitap.service.dbservice.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
@PreAuthorize("hasRole('USER')")
public class WishlistController {
    private final WishlistService wishlistService;
    private final CustomerService customerService;

    @Autowired
    public WishlistController(WishlistService wishlistService,
                              CustomerService customerService) {
        this.wishlistService = wishlistService;
        this.customerService = customerService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBookToWishlist(
            Principal principal,
            @ModelAttribute BookRequest bookRequest) { // ModelAttribute ile tüm alanları tek seferde al

        try {
            // 1. Kullanıcıyı bul
            String username = principal.getName();
            CustomerEntity customer = customerService.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

            // 2. Kitap modelini oluştur
            BookDetailsModel bookModel = new BookDetailsModel();
            bookModel.setIsbn(bookRequest.getIsbn());
            bookModel.setTitle(bookRequest.getTitle());
            bookModel.setAuthor(bookRequest.getAuthor());
            bookModel.setPublisher(bookRequest.getPublisher());
            bookModel.setImageUrl(bookRequest.getImageUrl());

            // 3. Fiyat modellerini al
            List<BookPriceModel> priceModels = bookRequest.getPrices();

            // 4. Wishlist'e ekleme işlemi
            wishlistService.addToWishlistWithPrices(customer.getId(), bookModel, priceModels);

            return ResponseEntity.ok("Kitap ve fiyat bilgileri başarıyla eklendi!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hata: " + e.getMessage());
        }
    }
}