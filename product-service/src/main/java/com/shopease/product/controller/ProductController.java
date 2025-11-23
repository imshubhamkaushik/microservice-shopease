package com.shopease.product.controller;

import com.shopease.product.model.Product;
import com.shopease.product.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*") // allow requests from frontend
public class ProductController {
    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Product> all() {
        return repo.findAll();
    }

    @PostMapping
    @SuppressWarnings("null")
    public Product create(@RequestBody Product p) {
        return repo.save(p);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
