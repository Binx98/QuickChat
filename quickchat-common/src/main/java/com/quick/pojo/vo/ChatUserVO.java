package com.quick.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author 徐志斌
 * @Date: 2023/11/13 20:58
 * @Version 1.0
 * @Description: 用户信息VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatUserVO {
    /**
     * 账号
     */
    private String accountId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别（1：男，0：女）
     */
    private Integer gender;
    /**
     * 位置
     */
    private String location;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
