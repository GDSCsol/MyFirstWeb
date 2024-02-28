package org.example.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessRefreshTokenDto {

    private String accessToken;

    private String refreshToken;
}
