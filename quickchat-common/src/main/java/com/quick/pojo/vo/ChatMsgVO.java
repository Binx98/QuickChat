package com.quick.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quick.pojo.dto.FileExtraDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author 徐志斌
 * @Date: 2024/1/13 19:24
 * @Version 1.0
 * @Description: 聊天信息VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsgVO {
    /**
     * 账户id
     */
    private String accountId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 内容
     */
    private String content;

    /**
     * 关系id
     */
    private String relationId;

    /**
     * 消息类型
     *
     * @see com.quick.enums.ChatMsgEnum
     */
    private Integer msgType;

    /**
     * 额外信息
     */
    private FileExtraDTO extraInfo;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
