package com.example.kitap.service.api;

import com.example.kitap.model.BookPrice;

public interface PriceProvider {
    BookPrice fetchPriceByISBN(String isbn);
}
