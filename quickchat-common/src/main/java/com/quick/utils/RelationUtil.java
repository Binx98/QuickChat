package com.quick.utils;

import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author 徐志斌
 * @Date: 2023/12/30 19:05
 * @Version 1.0
 * @Description: 账号关联 id 工具类
 */
public class RelationUtil {
    /**
     * 生成账号关联id
     */
    public static String generate(String fromId, String toId) {
        if (StringUtils.isEmpty(fromId) || StringUtils.isEmpty(toId)) {
            throw new QuickException(ResponseEnum.RELATION_GENERATE_ERROR);
        }
        if (toId.compareTo(fromId) <= 0) {
            return new StringBuffer()
                    .append(toId)
                    .append(":")
                    .append(fromId)
                    .toString();
        } else {
            return new StringBuffer()
                    .append(fromId)
                    .append(":")
                    .append(toId)
                    .toString();
        }
    }

    /**
     * 拆分关联 id
     */
    public static String[] generate(String relationId) {
        if (StringUtils.isEmpty(relationId)) {
            throw new QuickException(ResponseEnum.FAIL);
        }
        return relationId.split(":");
    }


    public static void main(String[] args) {
        String result = generate("xuzhibin", "wenshuangxin");
        System.out.println(result);
    }
}
