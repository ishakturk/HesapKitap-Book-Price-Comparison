package com.example.kitap.service.dbservice;

import com.example.kitap.entity.BookDetailsEntity;
import com.example.kitap.model.BookDetailsModel;
import com.example.kitap.repository.BookDetailsRepository;
import com.example.kitap.repository.WishlistRepository;
import com.example.kitap.service.BookConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookDetailsRepository bookDetailsRepo;
    private final WishlistRepository wishlistRepo;
    private final BookConverterService converter;

    @Autowired
    public BookService(BookDetailsRepository bookDetailsRepo,
                       WishlistRepository wishlistRepo,
                       BookConverterService converter) {
        this.bookDetailsRepo = bookDetailsRepo;
        this.wishlistRepo = wishlistRepo;
        this.converter = converter;
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
}