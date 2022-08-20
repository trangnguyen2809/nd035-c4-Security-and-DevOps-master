package com.example.demo;

import com.example.demo.controllers.*;
import org.junit.runner.*;
import org.junit.runners.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CartControllerTest.class,
        ItemControllerTest.class,
        OrderControllerTest.class,
        UserControllerTest.class
})
public class ECommerceSuiteTest {
}
