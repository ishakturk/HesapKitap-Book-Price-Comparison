package com.example.kitap.model;

import java.util.List;

// BookRequest.java (Yeni DTO sınıfı)
public class BookRequest {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;
    private List<BookPriceModel> prices;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<BookPriceModel> getPrices() {
        return prices;
    }

    public void setPrices(List<BookPriceModel> prices) {
        this.prices = prices;
    }
}
