package com.example.kitap.model;

public class BookPriceModel {
    private String siteName;
    private Double price;
    private String bookUrl;

    public BookPriceModel() {}

    public BookPriceModel(String siteName, Double price, String bookUrl) {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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
