package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = ShareItServer.class)
class ShareItServerTest {

    @Test
    void contextLoads() {

    }

}