package com.bookservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*")
@Tag(name = "Books", description = "Book management operations")
public class BookController {
    
    @Autowired
    private BookRepository bookRepository;
    
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve a list of all books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available books", description = "Retrieve books that are in stock")
    public List<Book> getAvailableBooks() {
        return bookRepository.findByStatus(Book.BookStatus.AVAILABLE);
    }
    
    @GetMapping("/seller/{sellerId}")
    @Operation(summary = "Get books by seller", description = "Retrieve all books listed by a specific seller")
    public List<Book> getBooksBySeller(@PathVariable Long sellerId) {
        return bookRepository.findBySellerId(sellerId);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Create a new book", description = "Add a new book to the collection")
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update an existing book by ID")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            book.setId(id);
            return ResponseEntity.ok(bookRepository.save(book));
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/quantity")
    @Operation(summary = "Update book quantity", description = "Update the stock quantity of a book")
    public ResponseEntity<Book> updateBookQuantity(@PathVariable Long id, @RequestBody java.util.Map<String, Integer> quantityUpdate) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setQuantity(quantityUpdate.get("quantity"));
            return ResponseEntity.ok(bookRepository.save(book));
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Remove a book from the collection by ID")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}