package com.example.demo.controllers;

import com.example.demo.*;
import com.example.demo.model.persistence.*;
import com.example.demo.model.persistence.repositories.*;
import org.junit.*;
import org.springframework.http.*;

import java.math.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp () {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void test_submit () {
        User testUser = getMockUser();
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

        UserOrder testUserOrder = new UserOrder();
        testUserOrder.setId(1L);
        testUserOrder.setUser(testUser);
        testUser.setCart(testUser.getCart());
        when(orderRepository.findById(testUserOrder.getId())).thenReturn(Optional.of(testUserOrder));
        when(orderRepository.save(testUserOrder)).thenReturn(testUserOrder);

        ResponseEntity<UserOrder> response = orderController.submit(testUser.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
    }

    @Test
    public void test_get_oders_by_user () {
        User testUser = getMockUser();
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);

        UserOrder testUserOrder = new UserOrder();
        testUserOrder.setId(1L);
        testUserOrder.setUser(testUser);
        testUser.setCart(testUser.getCart());
        List<UserOrder> testUserOders = new ArrayList<>();
        testUserOders.add(testUserOrder);
        when(orderRepository.findByUser(testUser)).thenReturn(testUserOders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(testUser.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> userOrders = response.getBody();
        assertNotNull(userOrders);
        assertEquals(userOrders, testUserOders);
    }

    private User getMockUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("trangnt97");
        user.setPassword("testPassword");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        List<Item> items = new ArrayList<>();
        items.add(getMockItem());
        cart.setItems(items);
        user.setCart(cart);
        return user;
    }

    private Item getMockItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("laptop");
        item.setPrice(new BigDecimal(12.345));
        item.setDescription("Item test");
        return item;
    }
}
