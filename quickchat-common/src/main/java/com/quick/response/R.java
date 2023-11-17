package com.quick.response;

import com.quick.enums.ResponseEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 21:59
 * @Version 1.0
 * @Description: 响应封装类
 */
@Getter
@Setter
public class R<T> extends BaseResponse {
    private T data;

    private R(ResponseEnum code) {
        super(code);
    }

    private R(ResponseEnum code, T data) {
        super(code);
        this.data = data;
    }

    public static <T> R<T> out(ResponseEnum code) {
        return new R<>(code);
    }

    public static <T> R<T> out(ResponseEnum code, T data) {
        return new R<>(code, data);
    }
}
