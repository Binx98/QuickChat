package com.salt.websocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
     * 读取客户端 Channel 数据（发送信息通过接口，不通过Channel，例如：抖音）
     * ------------------------------------------------------------
     * -----------------------------------------------------------
     * 逻辑：
     * 首次建立链接，前端会调用send发送uid，后端在这里绑定一下Channel和uid关系
     * 这里不接受客户端发送的消息，发送消息通过接口接受
     * ------------------------------------------------------------
     * socket.onopen = () => {
     * socket.send(this.userId)
     * }
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
//        Long uid = JSON.parse(frame.text(), Long.class);
//        NettyChannelRelation.getChannelGroup().add(ctx.channel());
//        NettyChannelRelation.getUserChannelMap().put(uid, ctx.channel());
//        AttributeKey<Long> key = AttributeKey.valueOf("uid");
        // 相当于为channel做个标识，用于removeUserId()
//        ctx.channel().attr(key).setIfAbsent(uid);
        log.info("--------------建立连接成功：{}--------------", frame.text());
    }

    /**
     * 建立连接
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("------------------客户端建立连接成功:{}------------------", ctx.channel());
    }

    /**
     * 断开连接
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        // 移除 Channel Group 中的 Channel 信息
        NettyChannelRelation.getChannelGroup().remove(ctx.channel());

        // 移除 uid 和 Channel 关联关系
        AttributeKey<Long> key = AttributeKey.valueOf("uid");
        Long uid = ctx.channel().attr(key).get();
        if (ObjectUtils.isNotEmpty(uid)) {
            NettyChannelRelation.getUserChannelMap().remove(uid);
        }

        // 用户状态更新为下线
        log.info("------------------客户端与WebSocket服务器断开连接：{}------------------", ctx);
    }

    /**
     * 事件触发监听
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) {
        // 心跳检测：30s无心跳触发断开事件
        if (event instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) event;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                ctx.close(); // 调用close可以触发handlerRemoved逻辑
                log.info("--------------------30s未检测到心跳，断开WebSocket链接：{}--------------------", ctx);
            }
        }
    }
}
