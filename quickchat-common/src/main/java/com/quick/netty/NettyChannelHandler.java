package com.quick.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
     * 断开连接
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        // 移除 Channel Group 中的 Channel 信息
        UserChannelRelation.getChannelGroup().remove(ctx.channel());

        // 移除 uid 和 Channel 关联关系
        AttributeKey<Long> key = AttributeKey.valueOf("uid");
        Long uid = ctx.channel().attr(key).get();
        if (ObjectUtils.isNotEmpty(uid)) {
            UserChannelRelation.getUserChannelMap().remove(uid);
        }

        // 用户状态更新为下线
        log.info("------------------客户端与WebSocket服务器断开连接：{}------------------", ctx);
    }

    /**
     * 事件触发监听
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) {
        // 连接建立，握手完成
        if (event instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // TODO 修改用户在线状态
            log.info("--------------------客户端建立连接成功：{}--------------------", ctx);
        }

        // 心跳检测：30s无心跳触发断开事件
        else if (event instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) event;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("--------------------30s未检测到心跳，断开WebSocket链接：{}--------------------", ctx);
                ctx.close(); // 需要手动触发，否则不会主动给下线
            }
        }
    }
}
