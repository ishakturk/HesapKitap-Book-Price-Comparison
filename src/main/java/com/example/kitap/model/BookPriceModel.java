package com.example.kitap.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookPriceModel {
    private String siteName;
    private Double price;
    private String bookUrl;
    private Boolean isCheapest;
    private LocalDateTime date;

    public BookPriceModel() {}

    public BookPriceModel(String siteName, Double price, String bookUrl) {
        this.siteName = siteName;
        this.price = price;
        this.bookUrl = bookUrl;
    }

    public BookPriceModel(String siteName, Double price, String provider, Boolean isCheapest) {
        this.siteName = siteName;
        this.price = price;
        this.bookUrl = provider;
        this.isCheapest = isCheapest;
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

    public Boolean getIsCheapest() {
        return isCheapest;
    }

    public void setIsCheapest(Boolean cheapest) {
        isCheapest = cheapest;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getFormattedDate() {
        return DateTimeFormatter.ofPattern("MMMM d, yyyy").format(date);
    }

    public int[] getDate() {
        return new int[] {
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth(),
                date.getHour(),
                date.getMinute()
        };
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
