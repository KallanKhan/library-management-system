// BorrowerController.java
package com.library.management.controller;

import java.util.List;
/*
@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {
    private static final Logger logger = LoggerFactory.getLogger(BorrowerController.class);

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public ResponseEntity<BorrowerDTO> registerBorrower(@RequestBody BorrowerDTO borrowerDTO) {
        logger.info("Request to create borrower: {}", borrowerDTO);
        BorrowerDTO savedBorrower = borrowerService.registerBorrower(borrowerDTO);
        return new ResponseEntity<>(savedBorrower, HttpStatus.CREATED);
    }

    @PostMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        logger.info("Request to borrow book with borrowerId: {}, bookId: {}", borrowerId, bookId);
        borrowerService.borrowBook(borrowerId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @PostMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        logger.info("Request to return book with borrowerId: {}, bookId: {}", borrowerId, bookId);
        borrowerService.returnBook(borrowerId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }

    @GetMapping
    public ResponseEntity<List<BorrowerDTO>> getAllBorrowers() {
        logger.info("Request to get all borrowers");
        List<BorrowerDTO> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }

    @GetMapping("/{borrowerId}")
    public ResponseEntity<BorrowerDTO> getBorrowerDetails(@PathVariable Long borrowerId) {
        logger.info("Request to get borrower with id: {}", borrowerId);
        BorrowerDTO borrowerDTO = borrowerService.getBorrowerById(borrowerId);
        return ResponseEntity.ok(borrowerDTO);
    }
}*/
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.management.dto.BorrowerDTO;
import com.library.management.service.BorrowerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/borrowers")
@Tag(name = "Borrower Management", description = "APIs for managing borrowers and borrowing operations")
public class BorrowerController {
    private static final Logger logger = LoggerFactory.getLogger(BorrowerController.class);

    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @Operation(summary = "Register a new borrower", description = "Creates a new borrower with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Borrower registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<BorrowerDTO> registerBorrower(@RequestBody BorrowerDTO borrowerDTO) {
        logger.info("Request to create borrower: {}", borrowerDTO);
        BorrowerDTO savedBorrower = borrowerService.registerBorrower(borrowerDTO);
        return new ResponseEntity<>(savedBorrower, HttpStatus.CREATED);
    }

    @Operation(summary = "Borrow a book", description = "Marks a book as borrowed by a borrower.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
            @ApiResponse(responseCode = "404", description = "Borrower or Book not found"),
            @ApiResponse(responseCode = "400", description = "Book already borrowed")
    })
    @PostMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(
            @Parameter(description = "ID of the borrower") @PathVariable UUID borrowerId,
            @Parameter(description = "ID of the book") @PathVariable UUID bookId) {
        logger.info("Request to borrow book with borrowerId: {}, bookId: {}", borrowerId, bookId);
        borrowerService.borrowBook(borrowerId, bookId);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    @Operation(summary = "Return a borrowed book", description = "Marks a borrowed book as returned.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "404", description = "Borrower or Book not found")
    })
    @PostMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<String> returnBook(
            @Parameter(description = "ID of the borrower") @PathVariable UUID borrowerId,
            @Parameter(description = "ID of the book") @PathVariable UUID bookId) {
        logger.info("Request to return book with borrowerId: {}, bookId: {}", borrowerId, bookId);
        borrowerService.returnBook(borrowerId, bookId);
        return ResponseEntity.ok("Book returned successfully");
    }

    @Operation(summary = "Get all borrowers", description = "Fetches a list of all registered borrowers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of borrowers retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<BorrowerDTO>> getAllBorrowers() {
        logger.info("Request to get all borrowers");
        List<BorrowerDTO> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }

    @Operation(summary = "Get borrower details", description = "Fetches the details of a specific borrower by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrower details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Borrower not found")
    })
    @GetMapping("/{borrowerId}")
    public ResponseEntity<BorrowerDTO> getBorrowerDetails(
            @Parameter(description = "ID of the borrower") @PathVariable UUID borrowerId) {
        logger.info("Request to get borrower with id: {}", borrowerId);
       // UUID borrowerIds = UUID.fromString("your-uuid-string");
        BorrowerDTO borrowerDTO = borrowerService.getBorrowerById(borrowerId);
        return ResponseEntity.ok(borrowerDTO);
    }
}
