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
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
            "id", savedUser.getId(),
            "name", savedUser.getName(),
            "email", savedUser.getEmail(),
            "role", savedUser.getRole()
        ));
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
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "email", user.getEmail(),
            "role", user.getRole()
        ));
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
                return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "user", Map.of(
                        "id", user.get().getId(),
                        "name", user.get().getName(),
                        "email", user.get().getEmail(),
                        "role", user.get().getRole()
                    )
                ));
            }
        }
        return ResponseEntity.ok(Map.of("valid", false));
    }
}
