package org.example;

import org.example.security.entity.RefreshToken;
import org.example.security.repository.RefreshTokenRepository;
import org.example.security.service.RefreshTokenService;
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

    @Autowired
    private RefreshTokenService service;

    @Autowired
    private RefreshTokenRepository repository;

    @Test
    void testRedisTempleteSave() {
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
    void testRedistEntity() {
        //given
        String name = "testname";
        String access = "access1";
        String refresh = "refresh1";

        //when
        service.saveTokenInfo(name, access, refresh);

        //then
        RefreshToken refreshToken = repository.findByAccessToken(access).orElse(new RefreshToken("error", "", ""));
        Assertions.assertEquals(name, refreshToken.getName());
    }

    @Test
    void contextLoads() {

    }
}
