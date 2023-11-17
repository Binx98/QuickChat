package com.quick.websocket;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 徐志斌
 * @Date: 2023/5/28 19:29
 * @Description: 用户 & Channel 关系
 * ---------------------------------------------------------------------------------
 * 1：ChannelGroup：管理所有channel，负责连接，每个channel就是一个通道（GlobalEventExecutor.INSTANCE:单例）
 * 2：ConcurrentHashMap：管理 UserId 与 Channel 对应关系（用于给指定用户发送消息）
 */
public class NettyChannelRelation {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ConcurrentHashMap<Long, Channel> userChannelMap = new ConcurrentHashMap<>();

    /**
     * 获取channel组
     */
    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    /**
     * 获取用户channel map
     */
    public static ConcurrentHashMap<Long, Channel> getUserChannelMap() {
        return userChannelMap;
    }
}
