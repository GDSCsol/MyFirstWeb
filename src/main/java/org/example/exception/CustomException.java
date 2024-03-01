package org.example.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 커스텀으로 사용할 런타임(언체크) Exception
@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

}
