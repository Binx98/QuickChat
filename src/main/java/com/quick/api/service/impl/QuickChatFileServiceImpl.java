package com.quick.api.service.impl;

import com.quick.api.service.QuickChatFileService;
import com.quick.common.strategy.file.AbstractFileStrategy;
import com.quick.common.strategy.file.FileStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 * 表情包 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-04-04
 */
@Service
public class QuickChatFileServiceImpl implements QuickChatFileService {
    @Override
    public Map<String, Object> uploadFile(int type, MultipartFile file) throws Exception {
        AbstractFileStrategy fileStrategy = FileStrategyFactory.getStrategyHandler(type);
        return fileStrategy.uploadFile(file);
    }

    @Override
    public void downloadFile(int type, String url) {
        AbstractFileStrategy fileStrategy = FileStrategyFactory.getStrategyHandler(type);
        fileStrategy.downloadFile(url);
    }

    @Override
    public void deleteFile(int type, String url) throws Exception {
        AbstractFileStrategy fileStrategy = FileStrategyFactory.getStrategyHandler(type);
        fileStrategy.deleteFile(url);
    }
}
