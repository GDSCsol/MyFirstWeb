package org.example.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Getter
@AllArgsConstructor
// key : id + jwtToken
@RedisHash(value = "jwtToken", timeToLive = 30 * 24 * 60 * 60) // TODO: 2024-02-27 유효시간 환경변수화 
public class RefreshToken implements Serializable {

    @Id
    private String name;

    @Indexed // JPA를 사용하듯 findByAccessToken같은 질의 가능
    private String accessToken;

    private String refreshToken;

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
