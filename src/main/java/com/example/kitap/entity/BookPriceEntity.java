package com.example.kitap.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "book_price")
public class BookPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "site_name",nullable = false)
    private String siteName;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Column(name = "is_latest", nullable = false)
    private Boolean isLatest = false;

    @Column(name = "is_cheapest", nullable = false)
    private Boolean isCheapest = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn", referencedColumnName = "isbn", nullable = false)
    private BookDetailsEntity book;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BookDetailsEntity getBook() {
        return book;
    }

    public void setBook(BookDetailsEntity book) {
        this.book = book;
    }

    public String getSiteName() {
        return siteName;
    }
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Boolean getIsLatest() {
        return isLatest;
    }

    public void setIsLatest(Boolean latest) {
        isLatest = latest;
    }

    public Boolean getIsCheapest() {
        return isCheapest;
    }

    public void setIsCheapest(Boolean cheapest) {
        isCheapest = cheapest;
    }
}