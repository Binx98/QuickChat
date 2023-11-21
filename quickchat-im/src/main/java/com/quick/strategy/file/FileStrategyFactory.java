package com.quick.strategy.file;

import com.quick.enums.ResponseEnum;
import com.quick.exception.QuickException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:59
 * @Description: 文件策略工厂
 * @Version: 1.0
 */
@Slf4j
public class FileStrategyFactory {
    private static final Map<Integer, AbstractFileStrategy> STRATEGY_MAP = new HashMap<>();

    /**
     * 注册工厂
     */
    public static void register(Integer type, AbstractFileStrategy orderStrategy) {
        STRATEGY_MAP.put(type, orderStrategy);
    }

    /**
     * 获取对应Handler
     */
    public static AbstractFileStrategy getStrategyHandler(Integer code) {
        AbstractFileStrategy strategyHandler = STRATEGY_MAP.get(code);
        if (ObjectUtils.isEmpty(strategyHandler)) {
            throw new QuickException(ResponseEnum.FAIL);
        }
        return strategyHandler;
    }
}
