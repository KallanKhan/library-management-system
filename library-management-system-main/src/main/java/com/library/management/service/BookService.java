// BookService.java
package com.library.management.service;

import com.library.management.dto.BookDTO;
import com.library.management.entity.Book;
import com.library.management.entity.BorrowRecord;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface BookService {
	public BookDTO registerBook(BookDTO bookDTO);
   // List<BookDTO> getAllBooks();
	
	 Page<BookDTO> getAllBooks(Pageable pageable);
    BookDTO getBookById(UUID bookId);
    public List<BorrowRecord> getBorrowingRecordsByBorrower(UUID borrowerId);
    public List<BorrowRecord> getBorrowingRecordsByBook(UUID bookId);
    @Transactional
    public void borrowBook(UUID bookId, UUID borrowerId);
    @Transactional
    public void returnBook(UUID bookId, UUID borrowerId);
	
	
    
    
}
