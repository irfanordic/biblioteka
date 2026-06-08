package com.biblioteka.domain.loans;

import org.springframework.web.bind.annotation.*;

import com.biblioteka.domain.loans.dto.CheckoutRequest;
import com.biblioteka.domain.loans.dto.ReturnRequest;
import com.biblioteka.domain.loans.Loan;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/checkout")
    public Loan CheckoutBook(@RequestBody CheckoutRequest Request) {
        return loanService.CheckoutBook(Request.bookId(), Request.memberId());
    }

    @PostMapping("/return")
    public Loan ReturnBook(@RequestBody ReturnRequest request) {

        return loanService.ReturnBook(request.loanId(), request.memberId());
    }

}
