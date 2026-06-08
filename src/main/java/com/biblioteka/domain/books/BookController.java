package com.biblioteka.domain.books;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")

public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable long id, @RequestBody Book bookdetails) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        existingBook.setTitle(bookdetails.getTitle());
        existingBook.setAuthor(bookdetails.getAuthor());
        existingBook.setIsbn(bookdetails.getIsbn());
        existingBook.setGenre(bookdetails.getGenre());
        existingBook.setTotalCopies(bookdetails.getTotalCopies());
        existingBook.setAvailableCopies(bookdetails.getAvailableCopies());

        return bookRepository.save(existingBook);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }

}
