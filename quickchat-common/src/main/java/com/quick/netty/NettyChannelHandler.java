package com.quick.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author 徐志斌
 * @Date: 2023/11/12 20:10
 * @Version 1.0
 * @Description: WebSocket Channel处理器
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     * 读取客户端 Channel 数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        log.info("--------------接收信息：{}--------------", frame.text());
    }

    /**
     * 建立连接
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // 建立 account_id 和 Channel 关联
        // 通知在线好友，我已经上线了
        log.info("--------------WebSocket连接建立：{}--------------", ctx);
    }

    /**
     * 断开连接
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        // 登陆状态调整：下线
        // 通知在线好友：我已经下线了！
        log.info("--------------WebSocket连接断开：{}--------------", ctx);
    }
}
