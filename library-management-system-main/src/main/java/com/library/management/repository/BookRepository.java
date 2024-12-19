
// BookRepository.java
package com.library.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.management.entity.Book;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
   
    // Method to fetch books by ISBN
	boolean existsByIsbn(String isbn);
}
