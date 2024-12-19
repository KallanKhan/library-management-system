package com.example.librarymanagementsystem.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.library.management.dto.BorrowerDTO;
import com.library.management.entity.Book;
import com.library.management.entity.Borrower;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowerRepository;
import com.library.management.service.impl.BorrowerServiceImpl;


@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    @Test
    void testRegisterBorrower() {
        // Given
        BorrowerDTO borrowerDTO = new BorrowerDTO(null, "test@example.com", "Test Borrower");
        Borrower savedBorrower = new Borrower(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "test@example.com", "Test Borrower");

        when(modelMapper.map(borrowerDTO, Borrower.class)).thenReturn(new Borrower());
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(savedBorrower);
        when(modelMapper.map(savedBorrower, BorrowerDTO.class)).thenReturn(borrowerDTO);

        // When
        BorrowerDTO result = borrowerService.registerBorrower(borrowerDTO);

        // Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test Borrower", result.getName());
    }

    @Test
    void testBorrowBook() {
        // Given
        Borrower borrower = new Borrower(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "test@example.com", "Test Borrower");
        Book book = Book.builder()
        	    .id(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))
        	    .isbn("1234567890")
        	    .title("Test Book")
        	    .author("Test Author")
        	    .borrower(borrower) // Pass Borrower object
        	    .borrowingRecords(new ArrayList<>()) // Empty borrowing records
        	    .version(1) // Initial version
        	    .borrowed(false) // Book is available
        	    .build();
        when(borrowerRepository.findById(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        assertDoesNotThrow(() -> borrowerService.borrowBook(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7")));

        // Then
        assertNotNull(book.getBorrower());
        assertEquals(borrower, book.getBorrower());
    }

    @Test
    void testBorrowBookAlreadyBorrowed() {
        // Given
        Borrower borrower = new Borrower(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "test@example.com", "Test Borrower");
        Book book = Book.builder()
        	    .id(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))
        	    .isbn("1234567890")
        	    .title("Test Book")
        	    .author("Test Author")
        	    .borrower(borrower) // Pass Borrower object
        	    .borrowingRecords(new ArrayList<>()) // Empty borrowing records
        	    .version(1) // Initial version
        	    .borrowed(false) // Book is available
        	    .build();
        book.setBorrower(borrower);

        when(borrowerRepository.findById(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(book));

        // When, Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> borrowerService.borrowBook(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"),   UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7")));
        assertEquals("Book is already borrowed", exception.getMessage());
    }

    @Test
    void testReturnBook() {
        // Given
        Borrower borrower = new Borrower(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "test@example.com", "Test Borrower");
        Book book = Book.builder()
        	    .id(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))
        	    .isbn("1234567890")
        	    .title("Test Book")
        	    .author("Test Author")
        	    .borrower(borrower) // Pass Borrower object
        	    .borrowingRecords(new ArrayList<>()) // Empty borrowing records
        	    .version(1) // Initial version
        	    .borrowed(false) // Book is available
        	    .build();
        book.setBorrower(borrower);

        when(borrowerRepository.findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        assertDoesNotThrow(() -> borrowerService.returnBook(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"),   UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7")));

        // Then
        assertNull(book.getBorrower());
    }

    @Test
    void testReturnBookNotBorrowed() {
        // Given
        Borrower borrower = new Borrower(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "test@example.com", "Test Borrower");
        Borrower anotherBorrower = new Borrower(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "another@example.com", "Another Borrower");
        Book book = Book.builder()
        	    .id(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))
        	    .isbn("1234567890")
        	    .title("Test Book")
        	    .author("Test Author")
        	    .borrower(borrower) // Pass Borrower object
        	    .borrowingRecords(new ArrayList<>()) // Empty borrowing records
        	    .version(1) // Initial version
        	    .borrowed(false) // Book is available
        	    .build();
        book.setBorrower(anotherBorrower);

        when(borrowerRepository.findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(book));

        // When, Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> borrowerService.returnBook(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"),   UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7")));
        assertEquals("Book was not borrowed by this borrower", exception.getMessage());
    }

    @Test
    void testGetAllBorrowers() {
        // Given
        Borrower borrower1 = new Borrower(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "borrower1@example.com", "Borrower 1");
        Borrower borrower2 = new Borrower(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "borrower2@example.com", "Borrower 2");
        List<Borrower> borrowers = Arrays.asList(borrower1, borrower2);

        when(borrowerRepository.findAll()).thenReturn(borrowers);
        when(modelMapper.map(borrower1, BorrowerDTO.class)).thenReturn(new BorrowerDTO(1L, "borrower1@example.com", "Borrower 1"));
        when(modelMapper.map(borrower2, BorrowerDTO.class)).thenReturn(new BorrowerDTO(2L, "borrower2@example.com", "Borrower 2"));

        // When
        List<BorrowerDTO> result = borrowerService.getAllBorrowers();

        // Then
        assertEquals(2, result.size());
        assertEquals("borrower1@example.com", result.get(0).getEmail());
        assertEquals("Borrower 2", result.get(1).getName());
    }

    @Test
    void testBorrowBook_BorrowerNotFound() {
        // Given
        Long borrowerId = 1L;
        Long bookId = 1L;

        when(borrowerRepository.findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> borrowerService.borrowBook(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"),   UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7")));

        // Then
        assertEquals("Borrower not found with id: " + borrowerId, exception.getMessage());
        verify(bookRepository, never()).findById(any());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testBorrowBook_BookNotFound() {
        // Given
        Long borrowerId = 1L;
        Long bookId = 1L;
        Borrower borrower = new Borrower(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "borrower@example.com", "Test Borrower");

        when(borrowerRepository.findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(borrower));
        when(bookRepository.findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> borrowerService.borrowBook(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"),   UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7")));

        // Then
        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"));
        verify(bookRepository, never()).save(any(Book.class));
    }

}
