package com.quick.service.impl;

import com.quick.pojo.dto.FileExtraDTO;
import com.quick.service.QuickChatFileService;
import com.quick.strategy.file.AbstractFileStrategy;
import com.quick.strategy.file.FileStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public FileExtraDTO uploadFile(int type, MultipartFile file) throws Exception {
        AbstractFileStrategy fileStrategy = FileStrategyFactory.getStrategyHandler(type);
        return fileStrategy.uploadFile(file);
    }

    @Override
    public void downloadFile(int type, String url) {
        AbstractFileStrategy fileStrategy = FileStrategyFactory.getStrategyHandler(type);
        fileStrategy.downloadFile(url);
    }
}
