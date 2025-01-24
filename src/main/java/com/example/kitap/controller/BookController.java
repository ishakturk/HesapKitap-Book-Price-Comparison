package com.example.kitap.controller;

import com.example.kitap.model.BookDetailsModel;
import com.example.kitap.service.scrapers.PriceComparatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PriceComparatorService priceComparatorService;

    @Autowired
    public BookController(PriceComparatorService priceComparatorService) {
        this.priceComparatorService = priceComparatorService;
    }

    // Ana sayfayı göstermek için GET mapping
    @GetMapping("/search")
    public String showSearchPage() {
        return "search";
    }

    // Arama işlemi için POST mapping
    @PostMapping("/search")
    public String searchBooks(@RequestParam("searchQuery") String searchQuery, Model model) {
        List<BookDetailsModel> bookDetails = priceComparatorService.fetchAndSortPricesBySearchQuery(searchQuery);

        if (bookDetails.isEmpty()) {
            model.addAttribute("message", "No books found for '" + searchQuery + "'.");
        }
        model.addAttribute("bookDetails", bookDetails);

        return "search";
    }
}