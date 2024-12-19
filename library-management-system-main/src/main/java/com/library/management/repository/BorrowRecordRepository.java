package com.library.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.management.entity.Book;
import com.library.management.entity.BorrowRecord;
import com.library.management.entity.Borrower;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByBook(Book book);
    List<BorrowRecord> findByBorrower(Borrower borrower);
    Optional<BorrowRecord> findTopByBookAndBorrowerAndReturnDateIsNull(Book book, Borrower borrower);
    
}