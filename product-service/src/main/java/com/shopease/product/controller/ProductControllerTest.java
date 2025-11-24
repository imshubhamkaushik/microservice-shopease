package com.shopease.product.controller;

import com.shopease.product.dto.ProductResponse;
import com.shopease.product.model.Product;
import com.shopease.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    ProductRepository repo;

    @Test
    void list_returns200() throws Exception {
        when(repo.findAll()).thenReturn(Collections.emptyList());
        mvc.perform(get("/products")).andExpect(status().isOk());
    }

    @Test
    void create_returns201() throws Exception {
        String body = "{\"name\":\"Phone\",\"description\":\"Nice\",\"price\":100}";
        mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());
    }
}
