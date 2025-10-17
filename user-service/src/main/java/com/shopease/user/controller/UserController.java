package com.shopease.user.controller;

import com.shopease.user.model.User;
import com.shopease.user.repository.UserRepository;
import com.shopease.user.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        User saved = repo.save(user);
        saved.setPassword(null); // don't return password
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest lr) {
        return repo.findByEmail(lr.getEmail())
                .filter(u -> u.getPassword().equals(lr.getPassword()))
                .map(u -> {
                    u.setPassword(null);
                    return ResponseEntity.ok(u);
                })
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping
    public List<User> all() {
        List<User> list = repo.findAll();
        list.forEach(u -> u.setPassword(null));
        return list;
    }
}
