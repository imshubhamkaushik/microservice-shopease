package com.shopease.user.service;

import com.shopease.user.dto.*;
import com.shopease.user.model.User;
import com.shopease.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository repo;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private UserService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_success() {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Test");
        req.setEmail("test@example.com");
        req.setPassword("Password1");

        when(repo.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        when(encoder.encode(req.getPassword())).thenReturn("hashed");
        when(repo.save(any())).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        var res = service.register(req);
        assertNotNull(res);
        assertEquals(1L, res.getId());
        verify(repo).save(any());
    }

    @Test
    void login_success() {
        User user = new User();
        user.setId(1L);
        user.setEmail("x@example.com");
        user.setPassword("hashed");

        when(repo.findByEmail("x@example.com")).thenReturn(Optional.of(user));
        when(encoder.matches("Password1", "hashed")).thenReturn(true);

        LoginRequest req = new LoginRequest();
        req.setEmail("x@example.com");
        req.setPassword("Password1");

        var res = service.login(req);
        assertNotNull(res);
    }

    @Test
    void register_duplicateEmail_throws() {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("X");
        req.setEmail("x@example.com");
        req.setPassword("Password1");

        when(repo.findByEmail(req.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> service.register(req));
    }
}
