// BorrowerRepository.java
package com.library.management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.library.management.entity.Borrower;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, UUID> {
    boolean existsByEmail(String email);
    @Query("SELECT b.name FROM Borrower b WHERE b.id = :borrowerId")
    String getBorrowerName(@Param("borrowerId") UUID borrowerId);
}
