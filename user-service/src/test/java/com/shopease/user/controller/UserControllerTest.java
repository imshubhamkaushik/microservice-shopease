package com.shopease.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopease.user.dto.CreateUserRequest;
import com.shopease.user.dto.UserResponse;
import com.shopease.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @MockitoBean
    UserService service;

    @Test
    @SuppressWarnings("null")
    void registerReturnsCreated() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("John");
        req.setEmail("john@example.com");
        req.setPassword("Password1");

        when(service.register(any()))
                .thenReturn(new UserResponse(1L, "John", "john@example.com"));

        mvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
