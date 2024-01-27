package com.quick.utils;

import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author 徐志斌
 * @Date: 2023/12/30 19:05
 * @Version 1.0
 * @Description:
 */
public class RelationUtil {
    /**
     * 通过参数生成：关系字符串
     */
    public static String generate(String fromId, String toId) {
        if (StringUtils.isEmpty(fromId) || StringUtils.isEmpty(toId)) {
            throw new QuickException(ResponseEnum.RELATION_GENERATE_ERROR);
        }

        if (toId.compareTo(fromId) <= 0) {
            return toId + ":" + fromId;
        } else {
            return fromId + ":" + toId;
        }
    }

    public static void main(String[] args) {
        String result = generate("xuzhibin", "wenshuangxin");
        System.out.println(result);
    }
}
