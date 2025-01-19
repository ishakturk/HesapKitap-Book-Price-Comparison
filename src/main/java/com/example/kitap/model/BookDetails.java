package com.example.kitap.model;

import java.util.List;

public class BookDetails {
    private String title;
    private String author;
    private String publisher;
    private List<BookPrice> prices;
    private String imageUrl;


    public BookDetails(String title, String author, String publisher, List<BookPrice> prices, String imageUrl) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.prices = prices;
        this.imageUrl = imageUrl;
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

    public List<BookPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<BookPrice> prices) {
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
        return "BookDetails{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", prices=" + prices +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}