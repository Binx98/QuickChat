package com.quick.common.pojo.dto;

import com.quick.common.enums.EmailEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @Author 徐志斌
 * @Date: 2023/11/25 15:05
 * @Version 1.0
 * @Description: 邮箱DTO
 */
@Data
public class EmailDTO {
    /**
     * 邮件类型
     *
     * @see EmailEnum
     */
    @NotNull(message = "邮件类型不能为空")
    private Integer type;

    /**
     * 接收方邮箱
     */
    @NotBlank(message = "接收方邮箱不能为空")
    private String toEmail;
}
