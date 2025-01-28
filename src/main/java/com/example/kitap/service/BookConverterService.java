package com.example.kitap.service;

import com.example.kitap.entity.BookDetailsEntity;
import com.example.kitap.entity.BookPriceEntity;
import com.example.kitap.model.BookDetailsModel;
import com.example.kitap.model.BookPriceModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookConverterService {

    public BookDetailsEntity toEntity(BookDetailsModel model) {
        BookDetailsEntity entity = new BookDetailsEntity();
        entity.setIsbn(model.getIsbn());
        entity.setTitle(model.getTitle());
        entity.setAuthor(model.getAuthor());
        entity.setPublisher(model.getPublisher());
        entity.setImageUrl(model.getImageUrl());
        entity.setDescription(model.getDescription());
        return entity;
    }

    public BookDetailsModel toModel(BookDetailsEntity entity) {
        BookDetailsModel model = new BookDetailsModel();
        model.setIsbn(entity.getIsbn());
        model.setTitle(entity.getTitle());
        model.setAuthor(entity.getAuthor());
        model.setPublisher(entity.getPublisher());
        model.setImageUrl(entity.getImageUrl());
        return model;
    }
}