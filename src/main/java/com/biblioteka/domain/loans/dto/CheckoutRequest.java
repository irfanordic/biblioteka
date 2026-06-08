package com.biblioteka.domain.loans.dto;

public record CheckoutRequest(
        long bookId,
        long memberId) {
}