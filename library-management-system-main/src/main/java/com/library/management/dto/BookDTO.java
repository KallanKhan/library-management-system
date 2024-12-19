// BookDTO.java
package com.library.management.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
	
		private UUID id;
	    private String isbn;
	    private String title;
	    private String author;
	   
}
