package com.example.kitap.service.scrapers.api;

import com.example.kitap.model.BookPriceModel;

public interface PriceProvider {
    BookPriceModel fetchPriceByISBN(String isbn);
}
