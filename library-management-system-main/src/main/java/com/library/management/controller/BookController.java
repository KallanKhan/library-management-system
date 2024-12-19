package com.library.management.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.management.dto.BookDTO;
import com.library.management.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/books")
public class BookController<Pageable> {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    // Fetch all books with pagination
    @Operation(summary = "Fetch all books with pagination", description = "Retrieve all books with pagination support.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved books"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<?> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching all books with pagination, page: {}, size: {}", page, size);
        Pageable pageable = (Pageable) PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getAllBooks((org.springframework.data.domain.Pageable) pageable));
    }
    // Register book using ISBN for validation
    @PostMapping
    public ResponseEntity<?> registerBook(@RequestBody BookDTO bookDTO) {
        logger.info("Registering book with ISBN: {}", bookDTO.getIsbn());
        bookService.registerBook(bookDTO);
        return ResponseEntity.ok("Book registered successfully.");
    }

    // Borrow book with concurrency handling
    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<?> borrowBook(@PathVariable UUID bookId, @RequestParam UUID borrowerId) {
        logger.info("Attempting to borrow book with ID: {} by borrower: {}", bookId, borrowerId);
        bookService.borrowBook(bookId, borrowerId);
        return ResponseEntity.ok("Book borrowed successfully.");
    }

}