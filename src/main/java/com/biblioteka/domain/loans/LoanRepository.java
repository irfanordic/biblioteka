package com.biblioteka.domain.loans;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByEmailAndBookId(String email, Long bookId);

}
