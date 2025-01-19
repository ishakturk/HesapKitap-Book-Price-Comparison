package com.example.kitap.model;

public class BookPrice {
    private String siteName;
    private double price;
    private String bookUrl;

    public BookPrice(String siteName, double price, String bookUrl) {
        this.siteName = siteName;
        this.price = price;
        this.bookUrl = bookUrl;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    @Override
    public String toString() {
        return "BookPrice{" +
                "siteName='" + siteName + '\'' +
                ", price=" + price +
                ", bookUrl='" + bookUrl + '\'' +
                '}';
    }
}
