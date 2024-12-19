package com.example.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.controller.BookController;
import com.library.management.dto.BookDTO;
import com.library.management.service.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
        // Sample BookDTO for testing
        testBookDTO = new BookDTO();
        testBookDTO.setId(UUID.fromString("9a82a2a7-7e5e-4be2-bc57-64773f8978e7"));
        testBookDTO.setTitle("Sample Book");
        testBookDTO.setAuthor("John Doe");
        testBookDTO.setIsbn("1234567890");
        
    }

    @Test
    void testRegisterBook() throws Exception {
        // Mocking the service method to return the testBookDTO when called
        Mockito.when(bookService.registerBook(Mockito.any(BookDTO.class))).thenReturn(testBookDTO);

        // Perform POST request to /api/books with testBookDTO as JSON
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBookDTO)));

        // Verify the response
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBookDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(testBookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(testBookDTO.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDTO.getIsbn()));
    }

    @Test
    void testGetAllBooks() throws Exception {
        // Mocking the service method to return a list of testBookDTOs when called
        List<BookDTO> bookDTOList = Collections.singletonList(testBookDTO);
       // Mockito.when(bookService.getAllBooks(Pageable pageable)).thenReturn(bookDTOList);
        Mockito.when(bookService.getAllBooks(Mockito.any(Pageable.class))).thenReturn((Page<BookDTO>) bookDTOList);
        // Perform GET request to /api/books
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/books"));

        // Verify the response
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(testBookDTO.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(testBookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(testBookDTO.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBookDTO.getIsbn()));
    }
}
