
// BorrowerService.java
package com.library.management.service;

import java.util.List;
import java.util.UUID;

import com.library.management.dto.BorrowerDTO;

public interface BorrowerService {
    BorrowerDTO registerBorrower(BorrowerDTO borrowerDTO);
    void borrowBook(UUID borrowerId, UUID bookId);
    void returnBook(UUID borrowerId, UUID bookId);
    List<BorrowerDTO> getAllBorrowers();

    BorrowerDTO getBorrowerById(UUID borrowerId);
}