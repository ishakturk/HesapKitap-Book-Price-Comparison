package com.example.kitap.model;

public class SearchRequest {
    private String searchQuery;

    public SearchRequest(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    // Getter ve Setter
    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}

