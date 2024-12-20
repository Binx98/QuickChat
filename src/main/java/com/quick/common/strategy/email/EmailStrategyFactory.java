package com.quick.common.strategy.email;

import com.quick.common.enums.ResponseEnum;
import com.quick.common.exception.QuickException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 徐志斌
 * @CreateTime: 2023-11-17  16:59
 * @Description: 聊天信息策略工厂
 * @Version: 1.0
 */
@Slf4j
public class EmailStrategyFactory {
    private static final Map<Integer, AbstractEmailStrategy> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer type, AbstractEmailStrategy orderStrategy) {
        STRATEGY_MAP.put(type, orderStrategy);
    }

    public static AbstractEmailStrategy getStrategyHandler(Integer code) {
        AbstractEmailStrategy strategyHandler = STRATEGY_MAP.get(code);
        if (ObjectUtils.isEmpty(strategyHandler)) {
            throw new QuickException(ResponseEnum.FAIL);
        }
        return strategyHandler;
    }
}
