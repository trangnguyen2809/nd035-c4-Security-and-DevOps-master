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

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp () {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void test_find_items () {
        List<Item> testItems = new ArrayList<>();
        testItems.add(getMockItem());
        when(itemRepository.findAll()).thenReturn(testItems);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(testItems, items);
    }

    @Test
    public void test_get_item_by_id () {
        Item testItem = getMockItem();
        when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));
        ResponseEntity<Item> response = itemController.getItemById(testItem.getId());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals(item, testItem);
    }

    @Test
    public void test_get_item_by_name () {
        List<Item> testItems = new ArrayList<>();
        testItems.add(getMockItem());
        when(itemRepository.findByName(testItems.get(0).getName())).thenReturn(testItems);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(testItems.get(0).getName());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(items, testItems);
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
