package com.example.kitap.repository;

import com.example.kitap.entity.BookDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDetailsRepository extends JpaRepository<BookDetailsEntity, Long> {

    Optional<BookDetailsEntity> findByIsbn(String isbn);

    @Query(value = "SELECT * FROM book_details ORDER BY RAND() LIMIT 10", nativeQuery = true)
    List<BookDetailsEntity> findRandomBooks();
}

