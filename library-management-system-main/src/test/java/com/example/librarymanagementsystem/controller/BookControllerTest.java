package com.example.librarymanagementsystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.controller.BookController;
import com.library.management.dto.BookDTO;
import com.library.management.entity.Book;
import com.library.management.service.BookService;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
        testBookDTO = new BookDTO();
        testBookDTO.setId(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"));
        testBookDTO.setTitle("Sample Book");
        testBookDTO.setAuthor("John Doe");
        testBookDTO.setIsbn("1234567890");
    }

    @Test
    void testGetAllBooks() throws Exception {
        // Mock the service method
        List<BookDTO> bookDTOList = Collections.singletonList(testBookDTO);
        Page<BookDTO> bookPage = new PageImpl<>(bookDTOList);
        Mockito.when(bookService.getAllBooks(Mockito.any(Pageable.class))).thenReturn(bookPage);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testBookDTO.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(testBookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].author").value(testBookDTO.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].isbn").value(testBookDTO.getIsbn()));
    }

    @Test
    void testRegisterBook() throws Exception {
        // Mocking behavior of BookService
        when(bookService.registerBook(any(BookDTO.class))).thenReturn(testBookDTO);

        // Perform POST request to the controller
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Expecting HTTP 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBookDTO.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(testBookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(testBookDTO.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDTO.getIsbn()));
    }


}
