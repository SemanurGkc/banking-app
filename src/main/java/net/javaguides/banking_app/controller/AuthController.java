package net.javaguides.banking_app.controller;

import net.javaguides.banking_app.dto.Register.RegisterRequest;
import net.javaguides.banking_app.entity.User;
import net.javaguides.banking_app.repository.IUserRepository;
import net.javaguides.banking_app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthController(IUserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // username check
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Username already exists"));
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            user.setRole("ROLE_USER");
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully!");
            response.put("username", user.getUsername());
            response.put("id", user.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }


    //Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid username or password"));
            }

            User user = userOptional.get();

            // password check
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid username or password"));
            }

            // Login successful
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

//Get user
@GetMapping("/user/{id}")
public ResponseEntity<?> getUserById(@PathVariable Long id) {
    try {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        User user = userOptional.get();
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        // Şifreyi ASLA dönmeyin!

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to get user: " + e.getMessage()));
    }
}

    //Delete account
    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody RegisterRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            User user = userOptional.get();

            // passw check
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid password"));
            }

            userRepository.delete(user);
            return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Account deletion failed: " + e.getMessage()));
        }
    }
}
