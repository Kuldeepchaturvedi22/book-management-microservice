package com.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);
        
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("id", savedUser.getId());
        userDetails.put("name", savedUser.getName());
        userDetails.put("email", savedUser.getEmail());
        userDetails.put("role", savedUser.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userDetails);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        }
        
        User user = userOpt.get();
        String token = jwtUtil.generateToken(user);
        
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("id", user.getId());
        userDetails.put("name", user.getName());
        userDetails.put("email", user.getEmail());
        userDetails.put("role", user.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userDetails);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("valid", false));
        }
        
        String token = authHeader.substring(7);
        boolean valid = jwtUtil.validateToken(token);
        
        if (valid) {
            String email = jwtUtil.extractEmail(token);
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                Map<String, Object> userDetails = new HashMap<>();
                userDetails.put("id", user.get().getId());
                userDetails.put("name", user.get().getName());
                userDetails.put("email", user.get().getEmail());
                userDetails.put("role", user.get().getRole());

                return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "user", userDetails
                ));
            }
        }
        return ResponseEntity.ok(Map.of("valid", false));
    }
}
