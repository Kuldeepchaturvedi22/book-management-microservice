package com.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    @PostMapping("/purchase")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderRequest) {
        Long buyerId = Long.valueOf(orderRequest.get("buyerId").toString());
        Long bookId = Long.valueOf(orderRequest.get("bookId").toString());
        Integer quantity = Integer.valueOf(orderRequest.get("quantity").toString());
        
        // Fetch book details from book-service
        Map bookResponse = webClientBuilder.build()
                .get()
                .uri("http://book-service/books/" + bookId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        
        if (bookResponse == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Book not found"));
        }
        
        Integer availableQty = (Integer) bookResponse.get("quantity");
        Double price = ((Number) bookResponse.get("price")).doubleValue();
        Long sellerId = Long.valueOf(bookResponse.get("sellerId").toString());
        
        if (availableQty < quantity) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient stock"));
        }
        
        // Update book quantity
        Map<String, Integer> updateRequest = Map.of("quantity", availableQty - quantity);
        webClientBuilder.build()
                .put()
                .uri("http://book-service/books/" + bookId + "/quantity")
                .bodyValue(updateRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        
        // Create order
        Order order = new Order(buyerId, bookId, sellerId, quantity, price * quantity);
        order.setStatus(Order.OrderStatus.COMPLETED);
        Order savedOrder = orderRepository.save(order);
        
        return ResponseEntity.ok(savedOrder);
    }
    
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Order>> getBuyerOrders(@PathVariable Long buyerId) {
        return ResponseEntity.ok(orderRepository.findByBuyerId(buyerId));
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Order>> getSellerOrders(@PathVariable Long sellerId) {
        return ResponseEntity.ok(orderRepository.findBySellerId(sellerId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(Order.OrderStatus.valueOf(statusUpdate.get("status")));
                    return ResponseEntity.ok(orderRepository.save(order));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
