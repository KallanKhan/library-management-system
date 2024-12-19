package com.library.management.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BORROWING_RECORD")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {
    
	    @Id
	    private UUID id;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "book_id", nullable = false)
	    private Book book;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "borrower_id", nullable = false)
	    private Borrower borrower;
	    @CreatedDate
	    private LocalDateTime borrowDate;
	    private LocalDateTime returnDate;
	    
	


	    public BorrowRecord(Book book, Borrower borrower, LocalDateTime returnDate) {
	        this.book = book;
	        this.borrower = borrower;
	        this.returnDate = returnDate;
	    }

	    public void markAsReturned() {
	        this.returnDate = LocalDateTime.now();
	    }

	    // Getters and Setters
}
