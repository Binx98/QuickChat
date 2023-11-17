package com.quick.pojo.vo;

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
public class UserVO {
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
     * 创建时间
     */
    private LocalDateTime createTime;
}
