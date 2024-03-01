package org.example.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {

    INVALID_ACCESSTOKEN(HttpStatus.BAD_REQUEST, "Invalid access token"),
    INVALID_REFRESHTOKEN(HttpStatus.BAD_REQUEST, "Invalid refresh token"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
