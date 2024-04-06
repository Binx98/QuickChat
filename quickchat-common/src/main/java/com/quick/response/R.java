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

    private R(ResponseEnum respEnum) {
        super(respEnum);
    }

    private R(ResponseEnum respEnum, T data) {
        super(respEnum);
        this.data = data;
    }

    public static <T> R<T> out(ResponseEnum respEnum) {
        return new R<>(respEnum);
    }

    public static <T> R<T> out(ResponseEnum respEnum, T data) {
        return new R<>(respEnum, data);
    }
}
