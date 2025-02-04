package com.example.kitap.controller;

import com.example.kitap.entity.BookDetailsEntity;
import com.example.kitap.entity.BookPriceEntity;
import com.example.kitap.model.BookDetailsModel;
import com.example.kitap.model.BookPriceModel;
import com.example.kitap.repository.BookDetailsRepository;
import com.example.kitap.repository.BookPriceRepository;
import com.example.kitap.service.BookConverterService;
import com.example.kitap.service.dbservice.BookService;
import com.example.kitap.service.scrapers.PriceComparatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PriceComparatorService priceComparatorService;
    private final BookService bookService;
    private final BookDetailsRepository bookDetailsRepo;
    private final BookPriceRepository bookPriceRepo;
    private final BookConverterService bookConverterService;

    @Autowired
    public BookController(PriceComparatorService priceComparatorService,
                          BookService bookService,
                          BookDetailsRepository bookDetailsRepo,
                          BookPriceRepository bookPriceRepo,
                          BookConverterService bookConverterService) {
        this.priceComparatorService = priceComparatorService;
        this.bookService = bookService;
        this.bookDetailsRepo = bookDetailsRepo;
        this.bookPriceRepo = bookPriceRepo;
        this.bookConverterService = bookConverterService;
    }

    @PostMapping("/search")
    public String handleSearch(@RequestParam("searchQuery") String searchQuery,
                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("query", searchQuery);
        return "redirect:/books/results";
    }

    @GetMapping("/results")
    public String showResults(@RequestParam("query") String searchQuery, Model model) {
        List<BookDetailsModel> bookDetails = priceComparatorService.fetchAndSortPricesBySearchQuery(searchQuery);
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
        // Fetch existing book or throw error
        BookDetailsEntity bookEntity = bookDetailsRepo.findByIsbn(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Get the latest prices from the database
        List<BookPriceEntity> priceEntities = bookPriceRepo.findLatestByIsbn(isbn);
        List<BookPriceModel> prices = priceEntities.stream()
                .map(bookConverterService::toPriceModel)
                .collect(Collectors.toList());

        // Get price history for chart
        List<BookPriceModel> priceHistory = bookService.getCheapestPriceHistory(isbn);

        // Prepare model
        BookDetailsModel bookModel = bookConverterService.toModel(bookEntity);
        bookModel.setPrices(prices);
        model.addAttribute("book", bookModel);
        model.addAttribute("priceHistory", priceHistory); // Add chart data

        return "book-detail";
    }

    @PostMapping("/details/{isbn}/refresh")
    public String refreshBookDetails(@PathVariable String isbn) {
        bookService.refreshBookDetails(isbn);
        return "redirect:/books/details/" + isbn;
    }

}
