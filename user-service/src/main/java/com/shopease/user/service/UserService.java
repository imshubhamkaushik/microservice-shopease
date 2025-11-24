package com.shopease.user.service;

import com.shopease.user.dto.CreateUserRequest;
import com.shopease.user.dto.LoginRequest;
import com.shopease.user.dto.UserResponse;
import com.shopease.user.model.User;
import com.shopease.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for users:
 * - register (hash password)
 * - login (verify password)
 * - listAll (return DTOs)
 * - deleteById
 */

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user, Throws Exception if email already exists

    public UserResponse register(CreateUserRequest req) {
        // Check existing email
        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword())); // Hash password before saving
        
        User saved = repo.save(user);
        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

    // Login user: return UserResponse if successful, null if failed
    public UserResponse login(LoginRequest req) {
        return repo.findByEmail(req.getEmail())
                .filter(u -> passwordEncoder.matches(req.getPassword(), u.getPassword()))
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail()))
                .orElse(null);
    }

    // List all users as DTOs (no passwords)
    public List<UserResponse> listAll() {
        return repo.findAll()
                .stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail()))
                .collect(Collectors.toList());
    }

    // Delete user by id, return true if deleted, false if not found
    public boolean deleteById(Long id) {
        if (!repo.existsById(id))
            return false;
        repo.deleteById(id);
        return true;
    }
}
