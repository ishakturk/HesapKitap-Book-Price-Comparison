package com.example.kitap.repository;

import com.example.kitap.entity.BookDetailsEntity;
import com.example.kitap.entity.CustomerEntity;
import com.example.kitap.entity.WishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {

    @Modifying
    @Query("DELETE FROM WishlistEntity w WHERE w.customer.id = :customerId AND w.book.isbn = :isbn")
    void deleteByCustomerIdAndBookIsbn(@Param("customerId") Long customerId, @Param("isbn") String isbn);

    boolean existsByCustomerAndBook(CustomerEntity customer, BookDetailsEntity book);
}

