package com.example.kitap.repository;

import com.example.kitap.entity.BookPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookPriceRepository extends JpaRepository<BookPriceEntity, Long> {

    @Modifying
    @Query("SELECT p FROM BookPriceEntity p WHERE p.book.isbn = :isbn")
    List<BookPriceEntity> findByBookIsbn(@Param("isbn") String isbn);


}

