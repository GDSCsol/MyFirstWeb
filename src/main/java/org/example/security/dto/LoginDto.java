package org.example.security.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotEmpty(message = "name은 필수항목입니다.")
    @Size(min = 3, max = 50)
    private String name;

    @NotEmpty(message = "password는 필수항목입니다.")
    @Size(min = 3, max = 100)
    private String password;
}