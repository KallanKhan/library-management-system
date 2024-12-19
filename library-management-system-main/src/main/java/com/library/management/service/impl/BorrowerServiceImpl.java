// BorrowerServiceImpl.java
package com.library.management.service.impl;

import com.library.management.dto.BorrowerDTO;
import com.library.management.entity.Book;
import com.library.management.entity.Borrower;
import com.library.management.exception.BookAlreadyBorrowedException;
import com.library.management.exception.BookNotBorrowedException;
import com.library.management.exception.DuplicateBorrowerEmailException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowerRepository;
import com.library.management.service.BorrowerService;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BorrowerServiceImpl(BorrowerRepository borrowerRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO) {
        // Check if a borrower with the same email already exists
        if (borrowerRepository.existsByEmail(borrowerDTO.getEmail())) {
            throw new DuplicateBorrowerEmailException("A borrower with this email already exists.");
        }

        Borrower borrower = modelMapper.map(borrowerDTO, Borrower.class);
        Borrower savedBorrower = borrowerRepository.save(borrower);
        return modelMapper.map(savedBorrower, BorrowerDTO.class);
    }
    @Transactional
    @Override
    public void borrowBook(UUID borrowerId, UUID bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        // Check if the book is already borrowed
        if (book.getBorrower() != null) {
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }

        book.setBorrower(borrower);

        // Save the book entity with optimistic locking
        bookRepository.save(book);
    }


    @Override
    public void returnBook(UUID borrowerId, UUID bookId) {
        // Retrieve borrower from repository or throw exception if not found
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerId));

        // Retrieve book from repository or throw exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        // Additional logic to ensure the book was borrowed by the borrower
        if ( book.getBorrower() ==null || !book.getBorrower().equals(borrower)) {
            throw new BookNotBorrowedException("Book was not borrowed by this borrower");
        }

        // Logic to mark the book as returned
        book.setBorrower(null);

        // Save the updated book entity
        bookRepository.save(book);
    }

    @Override
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(borrower -> modelMapper.map(borrower, BorrowerDTO.class))
                .toList();
    }

    @Override
    public BorrowerDTO getBorrowerById(UUID borrowerId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowerId));
        return modelMapper.map(borrower, BorrowerDTO.class);
    }

}