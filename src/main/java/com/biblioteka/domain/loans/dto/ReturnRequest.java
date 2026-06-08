package com.biblioteka.domain.loans.dto;

public record ReturnRequest(

        long loanId,
        long memberId

) {
}
