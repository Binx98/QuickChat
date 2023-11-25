package com.quick.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author 徐志斌
 * @Date: 2023/11/12 19:36
 * @Version 1.0
 * @Description: Netty构建WebSocket服务器
 */
@Slf4j
@Component
public class NettyWebSocketServer {
    private static final int WEB_SOCKET_PORT = 10086;
    @Autowired
    private NettyChannelHandler channelHandler;

    /**
     * boss接受客户端连接等事件
     * work处理boss接收的事件
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());

    /**
     * Netty WebSocket Server启动
     */
    private void start() throws InterruptedException {
        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workGroup);
        server.channel(NioServerSocketChannel.class);
        server.option(ChannelOption.SO_BACKLOG, 128);
        server.option(ChannelOption.SO_KEEPALIVE, true);
        // BossGroup日志
        server.handler(new LoggingHandler(LogLevel.INFO));
        server.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                // 30s客户端未发送心跳给服务器，断开链接（存在直接关闭客户端情况）
                pipeline.addLast(new IdleStateHandler(30, 0, 0));
                // HTTP协议编、解码器
                pipeline.addLast(new HttpServerCodec());
                // 块方式写数据
                pipeline.addLast(new ChunkedWriteHandler());
                pipeline.addLast(new HttpObjectAggregator(8192));
                pipeline.addLast(new WebSocketServerProtocolHandler("/")); // HTTP升级为WebSocket
                // 自定义Channel处理器
                pipeline.addLast(channelHandler);
            }
        });
        ChannelFuture future = server.bind(WEB_SOCKET_PORT).sync();
        log.info("-----------------------Netty WebSocket服务器启动成功：{}-----------------------", future.channel().localAddress());
    }

    /**
     * 启动 Netty WebSocket Server
     */
    @PostConstruct
    private void init() {
        new Thread(() -> {
            try {
                start();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * WebSocket Server停止释放资源
     */
    @PreDestroy
    private void destroy() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workGroup.shutdownGracefully().sync();
        log.info("-----------------------Netty WebSocket Server已关闭-----------------------");
    }
}
