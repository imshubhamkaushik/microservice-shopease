package com.shopease.user.controller;

import com.shopease.user.dto.UserResponse;
import com.shopease.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    UserService service;

    @Test
    void register_returns201() throws Exception {
        when(service.register(any())).thenReturn(new UserResponse(1L, "John", "john@example.com"));

        mvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"John","email":"john@example.com","password":"Password1"}
                        """))
                .andExpect(status().isCreated());
    }
}
