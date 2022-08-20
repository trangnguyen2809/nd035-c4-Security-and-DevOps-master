package com.example.demo.controllers;

import com.example.demo.*;
import com.example.demo.model.persistence.*;
import com.example.demo.model.persistence.repositories.*;
import com.example.demo.model.requests.*;
import org.junit.*;
import org.springframework.http.*;

import java.math.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    @Before
    public void setUp () {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void test_add_to_cart(){
        User testUser = getMockUser();
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        List<Item> testItems = testUser.getCart().getItems();
        when(itemRepository.findById(testItems.get(0).getId())).thenReturn(Optional.of(testItems.get(0)));
        Cart testCart = testUser.getCart();

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(testItems.get(0).getId());
        modifyCartRequest.setUsername(testUser.getUsername());
        modifyCartRequest.setQuantity(1);
        when(cartRepository.save(testCart)).thenReturn(testCart);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(testCart.getItems(), cart.getItems());
    }

    @Test
    public void test_remove_from_cart(){
        User testUser = getMockUser();
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        List<Item> testItems = testUser.getCart().getItems();
        when(itemRepository.findById(testItems.get(0).getId())).thenReturn(Optional.of(testItems.get(0)));
        Cart testCart = testUser.getCart();

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(testItems.get(0).getId());
        modifyCartRequest.setUsername(testUser.getUsername());
        modifyCartRequest.setQuantity(1);

        testItems.remove(testItems.get(0));
        testCart.setItems(testItems);
        when(cartRepository.save(testCart)).thenReturn(testCart);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(testCart.getItems(), cart.getItems());
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
