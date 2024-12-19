package com.library.management.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BOOK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private UUID id;
	    
	    @Column(nullable = false, unique = true)
	    private String isbn;
	    
	    @Column(nullable = false)
	    private String title;
	    
	    @Column(nullable = false)
	    private String author;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "borrower_id")
	    private Borrower borrower;
	    
	    @OneToMany(mappedBy = "book")
	    private List<BorrowRecord> borrowingRecords; // To track borrowing history
	    
	    @Version
	    private int version;
	    private boolean borrowed;
	    
}
