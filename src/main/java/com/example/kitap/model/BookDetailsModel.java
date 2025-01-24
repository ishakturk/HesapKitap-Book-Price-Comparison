package com.example.kitap.model;

import java.util.List;

public class BookDetailsModel {
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private List<BookPriceModel> prices;
    private String imageUrl;
    private String description;

    public BookDetailsModel() {}

    public BookDetailsModel(String title, String author, String publisher, List<BookPriceModel> prices, String imageUrl, String isbn) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.prices = prices;
        this.imageUrl = imageUrl;
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public List<BookPriceModel> getPrices() {
        return prices;
    }

    public void setPrices(List<BookPriceModel> prices) {
        this.prices = prices;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "BookDetailsModel{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", prices=" + prices +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}