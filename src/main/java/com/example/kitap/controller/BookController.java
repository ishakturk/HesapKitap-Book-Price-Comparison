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
        model.addAttribute("searchQuery", searchQuery);
        return "search-results";
    }

    @GetMapping()
    public String showHomePage(Model model) {
        model.addAttribute("trendingBooks", bookService.getTrendingBooks());
        model.addAttribute("randomBooks", bookService.getRandomBooks());
        return "home";
    }

    @GetMapping("/details/{isbn}")
    public String showBookDetails(@PathVariable String isbn, Model model) {
        // Fetch or create book
        BookDetailsEntity bookEntity = bookDetailsRepo.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book with ISBN " + isbn + " not found in the database."));

        // Fetch latest prices
        List<BookPriceModel> newPrices = priceComparatorService.fetchPricesFromAllSites(isbn);

        // Save new prices
        newPrices.forEach(price -> {
            BookPriceEntity priceEntity = new BookPriceEntity();
            priceEntity.setBook(bookEntity);
            priceEntity.setPrice(price.getPrice());
            priceEntity.setSiteName(price.getSiteName());
            priceEntity.setProvider(price.getBookUrl());
            priceEntity.setLastUpdated(LocalDateTime.now());
            bookPriceRepo.save(priceEntity);
        });

        // Prepare model
        BookDetailsModel bookModel = bookConverterService.toModel(bookEntity);
        bookModel.setPrices(newPrices);
        model.addAttribute("book", bookModel);

        return "book-detail";
    }
}