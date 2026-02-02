package com.bookservice;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String author;
    private String isbn;
    private Double price;
    private Integer quantity;
    private Long sellerId;
    
    @Enumerated(EnumType.STRING)
    private BookStatus status;
    
    public enum BookStatus {
        AVAILABLE, SOLD_OUT
    }
    
    public Book() {
        this.status = BookStatus.AVAILABLE;
    }
    
    public Book(String title, String author, String isbn, Double price, Integer quantity, Long sellerId) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.quantity = quantity;
        this.sellerId = sellerId;
        this.status = quantity > 0 ? BookStatus.AVAILABLE : BookStatus.SOLD_OUT;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity;
        this.status = quantity > 0 ? BookStatus.AVAILABLE : BookStatus.SOLD_OUT;
    }
    
    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }
    
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
}