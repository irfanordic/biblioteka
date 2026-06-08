package com.biblioteka.domain.loans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteka.domain.books.Book;
import com.biblioteka.domain.books.BookRepository;
import com.biblioteka.domain.member.Member;
import com.biblioteka.domain.member.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoanService loanService;
    private Book testBook;
    private Member testMember;

    @BeforeEach
    public void setup() {
        testBook = new Book();
        testBook.setId(106l);
        testBook.setTitle("springBoot champion");
        testBook.setAvailableCopies(5);

        testMember = new Member();
        testMember.setId(253l);
        testMember.setName("wendyTest");
        testMember.setActive(true);

    }

    @Test
    void successCheckoutAndSavingAndInventoryUpdate() {
        when(bookRepository.findById(106l)).thenReturn(Optional.of(testBook));
        when(memberRepository.findById(253l)).thenReturn(Optional.of(testMember));

        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan activeLoan = loanService.CheckoutBook(106l, 253l);

        assertNotNull(activeLoan);
        assertEquals(testBook, activeLoan.getBook());
        assertEquals(testMember, activeLoan.getMember());
        assertEquals(4, testBook.getAvailableCopies());

        verify(bookRepository, times(1)).save(testBook);
        verify(loanRepository, times(1)).save(any(Loan.class));

    }

    @Test
    void outofstockCheckout() {
        testBook.setAvailableCopies(0);
        when(bookRepository.findById(106l)).thenReturn(Optional.of(testBook));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            loanService.CheckoutBook(106l, 253l);
        });

        assertEquals("no available copies for this book", exception.getMessage());

        verify(memberRepository, never()).findById(anyLong());
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void inactiveMemberCheckout() {
        testMember.setActive(false);

        when(bookRepository.findById(106l)).thenReturn(Optional.of(testBook));
        when(memberRepository.findById(253l)).thenReturn(Optional.of(testMember));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            loanService.CheckoutBook(106l, 253l);
        });

        assertEquals("Member account is not active", exception.getMessage());

        verify(loanRepository, never()).save(any(Loan.class));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void successfullReturn() {
        Book isolatedBook = new Book();
        isolatedBook.setId(106l);
        isolatedBook.setTitle("springBoot champion");
        isolatedBook.setAvailableCopies(5);
        isolatedBook.setTotalCopies(10);

        Loan testLoan = new Loan();
        testLoan.setId(41l);
        testLoan.setBook(isolatedBook);
        testLoan.setMember(testMember);
        testLoan.setReturned(false);

        when(loanRepository.findById(41l)).thenReturn(Optional.of(testLoan));
        when(memberRepository.findById(253l)).thenReturn(Optional.of(testMember));

        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(bookRepository.save(any(Book.class))).thenReturn(isolatedBook);

        // Act
        Loan returnedLoan = loanService.ReturnBook(41l, 253l);

        assertNotNull(returnedLoan);
        assertTrue(returnedLoan.isReturned());
        assertEquals(6, isolatedBook.getAvailableCopies());

        verify(bookRepository, times(1)).save(isolatedBook);
        verify(loanRepository, times(1)).save(testLoan);
    }

    @Test
    void returnAlreadyReturnedBook() {

        Loan testLoan = new Loan();
        testLoan.setId(41l);
        testLoan.setBook(testBook);
        testLoan.setMember(testMember);

        testLoan.setReturned(true);

        when(loanRepository.findById(41l)).thenReturn(Optional.of(testLoan));
        when(memberRepository.findById(253l)).thenReturn(Optional.of(testMember));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.ReturnBook(41l, 253l);
        });

        assertEquals("This book has been returned already", exception.getMessage());

        verify(bookRepository, never()).save(any(Book.class));
        verify(loanRepository, never()).save(any(Loan.class));

    }

    @Test
    void returnBookWithWrongMember() {
        Loan testLoan = new Loan();
        testLoan.setId(41l);
        testLoan.setBook(testBook);
        testLoan.setMember(testMember);
        testLoan.setReturned(false);

        Member wrongMember = new Member();
        wrongMember.setId(165l);
        wrongMember.setName("anotherGuy");
        wrongMember.setActive(true);

        when(loanRepository.findById(41l)).thenReturn(Optional.of(testLoan));
        when(memberRepository.findById(165l)).thenReturn(Optional.of(wrongMember));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loanService.ReturnBook(41l, 165l);
        });

        assertEquals("This loan does not belong to the user", exception.getMessage());

        verify(bookRepository, never()).save(any(Book.class));
        verify(loanRepository, never()).save(any(Loan.class));
    }

}
