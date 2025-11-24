package com.shopease.user.controller;

import com.shopease.user.dto.CreateUserRequest;
import com.shopease.user.dto.LoginRequest;
import com.shopease.user.dto.UserResponse;
import com.shopease.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000") // allow requests from frontend
public class UserController {

    private final UserService svc;

    public UserController(UserService svc) {
        this.svc = svc;
    }

    /**
     * List all users (response without password)
     */
    @GetMapping
    public List<UserResponse> getAll() {
        return svc.listAll();
    }

    /**
     * Register a new user. Validates input, hashes password in service.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest req) {
        // Optionally, the service may throw an exception if email exists — handle via
        // global exception handler
        UserResponse created = svc.register(req);
        return ResponseEntity.ok(created);
    }

    /**
     * Login endpoint: validates credentials and returns user info (no password) on
     * success.
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest req) {
        UserResponse user = svc.login(req);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Delete user by id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = svc.deleteById(id);
        if (!deleted)
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
