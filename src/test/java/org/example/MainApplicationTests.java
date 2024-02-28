package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
public class MainApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testSave() {
        //given
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "first";

        //when
        valueOperations.set(key, "helloWold!");

        //then
        String value = valueOperations.get(key);
        Assertions.assertEquals(value, "helloWold!");
    }
    @Test
    void contextLoads() {

    }
}
