package com.example.kitap.model;

public class WishlistItemModel {
    private Long wishlistId;
    private BookDetailsModel book;

    public WishlistItemModel(Long wishlistId, BookDetailsModel book) {
        this.wishlistId = wishlistId;
        this.book = book;
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public BookDetailsModel getBook() {
        return book;
    }

    public void setBook(BookDetailsModel book) {
        this.book = book;
    }
}
