package com.library.management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.dto.BookDTO;
import com.library.management.entity.Book;
import com.library.management.entity.BorrowRecord;
import com.library.management.entity.Borrower;
import com.library.management.exception.BookNotBorrowedException;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowRecordRepository;
import com.library.management.repository.BorrowerRepository;
import com.library.management.service.BookService;

import ch.qos.logback.classic.Logger;
import jakarta.transaction.Transactional;

@Service
public class BookServiceImpl implements BookService{

    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookServiceImpl.class);
    private static final ThreadLocal<ObjectMapper> objectMapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    // Get all books with pagination
    public Page<Book> getAllBooks(int page, int size) {
        logger.debug("Fetching all books with pagination, page: {}, size: {}", page, size);
        return bookRepository.findAll(PageRequest.of(page, size));
    }

    // Register book using ISBN for validation
    @Override
    public BookDTO registerBook(BookDTO bookDTO) {
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            logger.warn("Book with ISBN: {} already exists", bookDTO.getIsbn());
            throw new IllegalArgumentException("Book with the same ISBN already exists.");
        }
        Book book = new Book();
        book.setIsbn(UUID.randomUUID().toString());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        bookRepository.save(book);
        logger.info("Book with ISBN: {} registered successfully", bookDTO.getIsbn());
		return bookDTO;
    }

    @Transactional
    @Override
    public void borrowBook(UUID bookId, UUID borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (book.isBorrowed()) {
            logger.warn("Book with ID: {} is already borrowed", bookId);
            throw new IllegalStateException("Book is already borrowed.");
        }
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new IllegalArgumentException("Borrower not found"));
        book.setBorrower(borrower);
        bookRepository.save(book);

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setId(UUID.randomUUID());
        borrowRecord.setBook(book);
        borrowRecord.setBorrower(borrower);
        borrowRecord.setBorrowDate(LocalDateTime.now());
        borrowRecordRepository.save(borrowRecord);

        logger.info("Book with ID: {} successfully borrowed by borrower: {}", bookId, borrowerId);
    }

	@Override
	public Page<BookDTO> getAllBooks(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookDTO getBookById(UUID bookId) {
	    Book book = bookRepository.findById(bookId)
	            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
	    return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn());
	}


	@Override
	public List<BorrowRecord> getBorrowingRecordsByBorrower(UUID borrowerId) {
	    return borrowRecordRepository.findAll().stream()
	            .filter(record -> record.getBorrower().getId().equals(borrowerId))
	            .collect(Collectors.toList());
	}

	@Override
	public List<BorrowRecord> getBorrowingRecordsByBook(UUID bookId) {
	    return borrowRecordRepository.findAll().stream()
	            .filter(record -> record.getBook().getId().equals(bookId))
	            .collect(Collectors.toList());
	}

	@Transactional
    @Override
    public void returnBook(UUID bookId, UUID borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (book.getBorrower() == null || !book.getBorrower().getId().equals(borrowerId)) {
            throw new IllegalStateException("Book is not borrowed by this borrower.");
        }
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new IllegalArgumentException("Borrower not found"));
        book.setBorrower(borrower);

        bookRepository.save(book);


        logger.info("Book with ID: {} successfully returned by borrower: {}", bookId, borrowerId);
        BorrowRecord borrowRecord = borrowRecordRepository.findTopByBookAndBorrowerAndReturnDateIsNull(book, borrower)
                .orElseThrow(() -> new BookNotBorrowedException("This book was not borrowed by the borrower."));

        // Mark the book as returned in the borrow record
        borrowRecord.markAsReturned();

        // Save the updated borrow record
        borrowRecordRepository.save(borrowRecord);
	
	}


}


	