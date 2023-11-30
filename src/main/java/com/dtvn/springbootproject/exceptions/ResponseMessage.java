package com.dtvn.springbootproject.exceptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseMessage<T> {
    private String message;
    private Integer Code;
    private T data;

    public ResponseMessage(String message, Integer code) {
        this.message = message;
        Code = code;
    }
    public ResponseMessage(String message, Integer code, T data) {
        this.message = message;
        Code = code;
        this.data = data;
    }
}

