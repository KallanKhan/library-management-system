package com.library.management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.library.management.LibraryManagementSystemApplication;

@SpringBootTest(classes = LibraryManagementSystemApplication.class)
@ActiveProfiles("test")
class LibraryManagementSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
