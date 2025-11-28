package com.shopease.product.controller;

import com.shopease.product.dto.CreateProductRequest;
import com.shopease.product.dto.ProductResponse;
import com.shopease.product.model.Product;
import com.shopease.product.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://shop.local"}) // allow requests from frontend
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    // List products as ProductResponse DTOs
    @GetMapping
    public List<ProductResponse> listAll() {
        return repo.findAll().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice()))
                .toList();
    }
    
    // Create product with validation
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest req) {
        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        Product saved = repo.save(p);
        ProductResponse resp = new ProductResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice());

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved.getId())
            .toUri();
        return ResponseEntity.created(location).body(resp);
    }

    // Get single product
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getOne(@PathVariable long id) {
        return repo.findById(id)
                .map(p -> ResponseEntity
                        .ok(new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice())))
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
