package com.example.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.LibraryManagementSystemApplication;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;



@ActiveProfiles("test")
@SpringBootTest(classes = LibraryManagementSystemApplication.class)
@AutoConfigureMockMvc
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
        // Mock the service method to return the testBookDTO
        Mockito.when(bookService.registerBook(Mockito.any(BookDTO.class))).thenReturn(testBookDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Expecting 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testBookDTO.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(testBookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(testBookDTO.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDTO.getIsbn()));
    }


    @Test
    void testGetAllBooks() throws Exception {
        List<BookDTO> bookDTOList = Collections.singletonList(testBookDTO);
        Page<BookDTO> bookPage = new PageImpl<>(bookDTOList);

       // Mockito.when(bookService.getAllBooks(Mockito.any(Pageable.class))).thenReturn(bookPage);
        Mockito.when(bookService.getAllBooks(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(testBookDTO)));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()) // Prints the request and response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(testBookDTO.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].title").value(testBookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].author").value(testBookDTO.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].isbn").value(testBookDTO.getIsbn()));
    }

}
