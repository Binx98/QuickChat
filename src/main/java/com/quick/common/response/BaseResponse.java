package com.quick.common.response;

import com.quick.common.enums.ResponseEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author 徐志斌
 * @Date: 2023/5/8 21:59
 * @Version 1.0
 * @Description: BaseResponse
 */
@Getter
@Setter
public class BaseResponse {
    private Integer code;
    private String msg;

    protected BaseResponse(ResponseEnum code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }
}