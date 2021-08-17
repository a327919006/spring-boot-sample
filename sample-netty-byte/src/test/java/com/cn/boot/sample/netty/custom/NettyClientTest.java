package com.cn.boot.sample.netty.custom;

import com.cn.boot.sample.netty.custom.coder.CustomDecoder;
import com.cn.boot.sample.netty.custom.coder.CustomEncoder;
import com.cn.boot.sample.netty.custom.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 发起客户端长连接，具体连接后的操作查看
 *
 * @author Chen Nan
 * @see ClientHandler
 */
@Slf4j
public class NettyClientTest {
    @Test
    public void runClient() {
        log.info("客户端开始连接");
        String ip = "127.0.0.1";
        int port = 50001;

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new CustomDecoder());
                            ch.pipeline().addLast(new CustomEncoder());
                            // 设置消息处理器
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect(ip, port).sync();
            log.info("客户端连接完成");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
