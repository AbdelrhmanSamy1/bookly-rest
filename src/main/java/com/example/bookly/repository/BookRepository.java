package com.example.bookly.repository;// BookRepository.java

import com.example.bookly.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    boolean existsByIsbn(String isbn);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByCategoryId(Long categoryId);
}