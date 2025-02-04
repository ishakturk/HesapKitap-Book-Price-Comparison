package com.example.kitap.repository;

import com.example.kitap.entity.BookPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookPriceRepository extends JpaRepository<BookPriceEntity, Long> {

    @Modifying
    @Query("SELECT p FROM BookPriceEntity p WHERE p.book.isbn = :isbn")
    List<BookPriceEntity> findByBookIsbn(@Param("isbn") String isbn);

    @Modifying
    @Query("UPDATE BookPriceEntity p SET p.isLatest = false WHERE p.book.isbn = :isbn")
    void markAllAsNotLatest(String isbn);

    @Query("SELECT p FROM BookPriceEntity p WHERE p.book.isbn = :isbn AND p.isLatest = true")
    List<BookPriceEntity> findLatestByIsbn(@Param("isbn") String isbn);

    @Query("SELECT p FROM BookPriceEntity p WHERE p.book.isbn = :isbn ORDER BY p.lastUpdated DESC")
    List<BookPriceEntity> findLatestPricesByBook(@Param("isbn") String isbn);

    @Query(value = """
    SELECT * FROM (
        SELECT *, 
               ROW_NUMBER() OVER (
                   PARTITION BY DATE(last_updated) 
                   ORDER BY price ASC, last_updated ASC
               ) as rn 
        FROM book_price 
        WHERE isbn = :isbn 
          AND is_cheapest = true 
          AND last_updated >= :date
    ) AS subquery
    WHERE rn = 1
    """, nativeQuery = true)
    List<BookPriceEntity> findDailyMinPrices(
            @Param("isbn") String isbn,
            @Param("date") LocalDateTime date
    );

}

