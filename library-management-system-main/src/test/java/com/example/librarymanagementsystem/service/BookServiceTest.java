package com.example.librarymanagementsystem.service;

import com.library.management.dto.BookDTO;
import com.library.management.entity.Book;
import com.library.management.entity.Borrower;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowerRepository;
import com.library.management.service.impl.BookServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDTO createBookDTO(String isbn, String title, String author) {
        return new BookDTO(null, isbn, title, author);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterBook() {
        // Given
        BookDTO bookDTO = createBookDTO("1234567890", "Test Book", "Test Author");
        Borrower borrower = new Borrower(); // Create or fetch the Borrower object
        borrower.setId(UUID.randomUUID()); // Set ID or fetch from the database
        borrower.setName("John Doe"); // Example name

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
        
        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(book);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        BookDTO savedBook = bookService.registerBook(bookDTO);

        // Then
        assertEquals(bookDTO.getIsbn(), savedBook.getIsbn());
        assertEquals(bookDTO.getTitle(), savedBook.getTitle());
        assertEquals(bookDTO.getAuthor(), savedBook.getAuthor());
    }

    @Test
    void testRegisterBookWithExistingISBNAndSameTitleAuthor() {
        // Given
        BookDTO bookDTO = createBookDTO("1234567890", "Test Book", "Test Author");
         Borrower borrower = new Borrower(); // Create or fetch the Borrower object
        borrower.setId(UUID.randomUUID()); // Set ID or fetch from the database
        borrower.setName("John Doe"); // Example name

        Book existingBook = Book.builder()
        	    .id(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))
        	    .isbn("1234567890")
        	    .title("Test Book")
        	    .author("Test Author")
        	    .borrower(borrower) // Pass Borrower object
        	    .borrowingRecords(new ArrayList<>()) // Empty borrowing records
        	    .version(1) // Initial version
        	    .borrowed(false) // Book is available
        	    .build();
        List<Book> existingBooks = List.of(existingBook);

     // Mocking
     when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(!existingBooks.isEmpty());
     when(modelMapper.map(bookDTO, Book.class)).thenReturn(existingBook);


        // When / Then
        assertDoesNotThrow(() -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithExistingISBNAndDifferentTitle() {
        // Given
        BookDTO bookDTO = createBookDTO("1234567890", "Different Title", "Test Author");
        Borrower borrower = new Borrower(); // Create or fetch the Borrower object
        borrower.setId(UUID.randomUUID()); // Set ID or fetch from the database
        borrower.setName("John Doe"); 
        Book existingBook = Book.builder()
        	    .id(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))
        	    .isbn("1234567890")
        	    .title("Test Book")
        	    .author("Test Author")
        	    .borrower(borrower) // Pass Borrower object
        	    .borrowingRecords(new ArrayList<>()) // Empty borrowing records
        	    .version(1) // Initial version
        	    .borrowed(false) // Book is available
        	    .build();
        List<Book> existingBooks = List.of(existingBook);

     // Mocking
     when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(!existingBooks.isEmpty());
     when(modelMapper.map(bookDTO, Book.class)).thenReturn(existingBook);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithExistingISBNAndDifferentAuthor() {
        // Given
        BookDTO bookDTO = createBookDTO("1234567890", "Test Book", "Different Author");
        Borrower borrower = new Borrower(); // Create or fetch the Borrower object
        borrower.setId(UUID.randomUUID()); // Set ID or fetch from the database
        borrower.setName("John Doe"); 
        Book existingBook = Book.builder()
        	    .id(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))
        	    .isbn("1234567890")
        	    .title("Test Book")
        	    .author("Test Author")
        	    .borrower(borrower) // Pass Borrower object
        	    .borrowingRecords(new ArrayList<>()) // Empty borrowing records
        	    .version(1) // Initial version
        	    .borrowed(false) // Book is available
        	    .build();
        List<Book> existingBooks = List.of(existingBook);

     // Mocking
     when(bookRepository.existsByIsbn(bookDTO.getIsbn())).thenReturn(!existingBooks.isEmpty());
     when(modelMapper.map(bookDTO, Book.class)).thenReturn(existingBook);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testGetAllBooks() {
    	// Given
    	Borrower borrower = new Borrower(); // Create or fetch the Borrower object
    	borrower.setId(UUID.randomUUID()); // Set ID or fetch from the database
    	borrower.setName("John Doe");

    	Book book1 = Book.builder()
    	        .id(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))
    	        .isbn("1234567890")
    	        .title("Test Book 1")
    	        .author("Test Author 1")
    	        .borrower(borrower) // Pass Borrower object
    	        .borrowingRecords(new ArrayList<>()) // Empty borrowing records
    	        .version(1) // Initial version
    	        .borrowed(false) // Book is available
    	        .build();

    	Book book2 = Book.builder()
    	        .id(UUID.randomUUID()) // Use a different UUID
    	        .isbn("0987654321")
    	        .title("Test Book 2")
    	        .author("Test Author 2")
    	        .borrower(borrower) // Pass Borrower object
    	        .borrowingRecords(new ArrayList<>()) // Empty borrowing records
    	        .version(1) // Initial version
    	        .borrowed(false) // Book is available
    	        .build();

    	List<Book> mockBooks = Arrays.asList(book1, book2);
    	Pageable pageable = PageRequest.of(0, 10);
    	// Mocking
    	when(bookRepository.findAll()).thenReturn(mockBooks);

    	BookDTO bookDTO1 = new BookDTO(book1.getId(), book1.getIsbn(), book1.getTitle(), book1.getAuthor());
    	BookDTO bookDTO2 = new BookDTO(book2.getId(), book2.getIsbn(), book2.getTitle(), book2.getAuthor());

    	when(modelMapper.map(book1, BookDTO.class)).thenReturn(bookDTO1);
    	when(modelMapper.map(book2, BookDTO.class)).thenReturn(bookDTO2);

    	// When
    	List<BookDTO> books = (List<BookDTO>) bookService.getAllBooks(pageable);

    	// Then
    	assertEquals(2, books.size());
    	assertEquals(book1.getIsbn(), books.get(0).getIsbn());
    	assertEquals(book2.getIsbn(), books.get(1).getIsbn());
   }


    @Test
    void testRegisterBookWithValidBorrowerId() {
        // Given
        BookDTO bookDTO = createBookDTO("1234567890", "Test Book", "Test Author");
       
        Borrower borrower = new Borrower(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"), "doe@john.com", "John Doe");

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(modelMapper.map(new Book(), BookDTO.class)).thenReturn(bookDTO);
        when(borrowerRepository.findById(  UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.of(borrower));
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // When
        BookDTO savedBook = bookService.registerBook(bookDTO);

        // Then
        assertNotNull(savedBook);
      
    }

    @Test
    void testRegisterBookWithInvalidBorrowerId() {
        // Given
        BookDTO bookDTO = createBookDTO("1234567890", "Test Book", "Test Author");
        

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(borrowerRepository.findById(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"))).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithNullBorrowerId() {
        // Given
        BookDTO bookDTO = createBookDTO("1234567890", "Test Book", "Test Author");
       
        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(modelMapper.map(new Book(), BookDTO.class)).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // When
        BookDTO savedBook = bookService.registerBook(bookDTO);

        // Then
        assertNotNull(savedBook);
     
    }

    @Test
    void testRegisterBookWithZeroBorrowerId() {
        // Given
        BookDTO bookDTO = createBookDTO("1234567890", "Test Book", "Test Author");
       

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(modelMapper.map(new Book(), BookDTO.class)).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // When
        BookDTO savedBook = bookService.registerBook(bookDTO);

        // Then
        assertNotNull(savedBook);
        
    }

}
