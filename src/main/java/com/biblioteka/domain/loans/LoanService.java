package com.biblioteka.domain.loans;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biblioteka.domain.books.Book;
import com.biblioteka.domain.books.BookRepository;
import com.biblioteka.domain.member.Member;
import com.biblioteka.domain.member.MemberRepository;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository,
            MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Loan CheckoutBook(long bookId, long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("no available copies for this book");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (!member.isActive()) {
            throw new RuntimeException("Member account is not active");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setEmail(member.getEmail());
        loan.setLoanDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14));

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan ReturnBook(long loanId, long memberId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("loan not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (loan.getMember().getId() != member.getId()) {
            throw new RuntimeException("This loan does not belong to the user");
        }

        if (loan.isReturned()) {
            throw new RuntimeException("This book has been returned already");
        }

        Book book = loan.getBook();
        book.returnBook();
        bookRepository.save(book);

        loan.setReturned(true);
        return loanRepository.save(loan);

    }

}
