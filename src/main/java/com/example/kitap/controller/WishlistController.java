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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
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

    @GetMapping
    public String getWishlist(Principal principal, Model model) {
        CustomerEntity customer = customerService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        List<BookDetailsModel> wishlistBooks = wishlistService.getWishlistBooksForUser(customer.getId());
        model.addAttribute("bookDetails", wishlistBooks != null ? wishlistBooks : Collections.emptyList());

        return "wishlist";
    }


    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addBookToWishlist(
            Principal principal,
            @ModelAttribute BookRequest bookRequest) {

        // Get all fields at once with ModelAttribute

        try {
            // 1. Find user
            String username = principal.getName();
            CustomerEntity customer = customerService.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

            // 2. Create the book model
            BookDetailsModel bookModel = new BookDetailsModel();
            bookModel.setIsbn(bookRequest.getIsbn());
            bookModel.setTitle(bookRequest.getTitle());
            bookModel.setAuthor(bookRequest.getAuthor());
            bookModel.setPublisher(bookRequest.getPublisher());
            bookModel.setImageUrl(bookRequest.getImageUrl());

            // 3. Get price models
            List<BookPriceModel> priceModels = bookRequest.getPrices();

            // 4. Adding to Wishlist
            wishlistService.addToWishlistWithPrices(customer.getId(), bookModel, priceModels);


            return ResponseEntity.ok().body(Map.of(
                "status", "success",
                "message", "Kitap ve fiyat bilgileri başarıyla eklendi!"
        ));
    }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hata: " + e.getMessage());
        }
    }

    @PostMapping("/remove/{isbn}")
    public String removeFromWishlist(Principal principal, @PathVariable("isbn") String isbn) {
        String username = principal.getName();
        CustomerEntity customer = customerService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        wishlistService.removeFromWishlist(customer.getId(),isbn);
        return "redirect:/wishlist?removed=true";
    }

    @PostMapping("/update-prices")
    public String updateWishlistPrices(Principal principal) {
        CustomerEntity customer = customerService.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        wishlistService.updateAllPricesForCustomer(customer.getId());
        return "redirect:/wishlist?updated=true";
    }
}