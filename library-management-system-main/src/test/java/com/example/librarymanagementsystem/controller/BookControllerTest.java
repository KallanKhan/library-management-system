package com.example.librarymanagementsystem.controller;

import com.library.management.controller.BookController;
import com.library.management.dto.BookDTO;
import com.library.management.service.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.PageImpl;
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDTO mockBookDTO;

    @BeforeEach
    void setUp() {
    	UUID bookId = UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7");
        mockBookDTO = new BookDTO(bookId, "1234567890", "Test Book", "Test Author");
    }

    @Test
    void testRegisterBook() {
        // Mocking behavior of BookService
        when(bookService.registerBook(any(BookDTO.class))).thenReturn(mockBookDTO);

        // Call the controller method
        ResponseEntity<BookDTO> responseEntity = bookController.registerBook(mockBookDTO);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockBookDTO, responseEntity.getBody());
    }

    @Test
    void testGetAllBooks() {
        // Mocking behavior of BookService
    /*    List<BookDTO> mockBooks = Collections.singletonList(mockBookDTO);
        when(bookService.getAllBooks()).thenReturn(mockBooks);

        // Call the controller method
        ResponseEntity<List<BookDTO>> responseEntity = bookController.getAllBooks();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockBooks, responseEntity.getBody());
        */
        ////
        
        // Create mock data
        BookDTO mockBookDTO = new BookDTO(UUID.randomUUID(), "1234567890", "Test Book", "Test Author");
        List<BookDTO> mockBooks = Collections.singletonList(mockBookDTO);

        // Mock Pageable to simulate pagination
        Pageable pageable = PageRequest.of(0, 10);  // First page with 10 items

     // Wrap the mockBooks in a Page using PageImpl
        Page<BookDTO> mockPage = new PageImpl<>(mockBooks, pageable, mockBooks.size());

        // Mock the service method to return a page of books
        when(bookService.getAllBooks(pageable)).thenReturn(mockPage);

        // Call the controller method
      

     
    }
}