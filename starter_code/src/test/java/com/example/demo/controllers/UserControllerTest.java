package com.example.demo.controllers;

import com.example.demo.*;
import com.example.demo.model.persistence.*;
import com.example.demo.model.persistence.repositories.*;
import com.example.demo.model.requests.*;
import org.junit.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void test_create_user() {
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("hashedPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("trangnt97");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");
        ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("trangnt97", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void test_find_by_id() {
        User testUser = getMockUser();
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        ResponseEntity<User> response = userController.findById(testUser.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertEquals(testUser.getId(), user.getId());
        assertEquals(testUser.getUsername(), user.getUsername());
    }

    @Test
    public void test_find_by_username() {
        User testUser = getMockUser();
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        ResponseEntity<User> response = userController.findByUserName(testUser.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertEquals(testUser.getId(), user.getId());
        assertEquals(testUser.getUsername(), user.getUsername());
    }

    private User getMockUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("trangnt97");
        user.setPassword("testPassword");
        user.setCart(new Cart());
        return user;
    }
}
