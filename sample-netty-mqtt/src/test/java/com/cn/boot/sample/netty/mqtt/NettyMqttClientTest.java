package com.cn.boot.sample.netty.mqtt;

import com.cn.boot.sample.netty.mqtt.handler.ClientHandler;
import io.moquette.broker.MoquetteIdleTimeoutHandler;
import io.moquette.broker.metrics.BytesMetricsCollector;
import io.moquette.broker.metrics.BytesMetricsHandler;
import io.moquette.broker.metrics.MessageMetricsCollector;
import io.moquette.broker.metrics.MessageMetricsHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 使用原生netty连接mqtt服务端
 *
 * @author Chen Nan
 * @see ClientHandler
 */
@Slf4j
public class NettyMqttClientTest {
    @Test
    public void runProtoClient() {
        log.info("客户端开始连接");
        String ip = "127.0.0.1";
        int port = 50001;

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            BytesMetricsCollector bytesMetricsCollector = new BytesMetricsCollector();
            MessageMetricsCollector metricsCollector = new MessageMetricsCollector();
            MoquetteIdleTimeoutHandler timeoutHandler = new MoquetteIdleTimeoutHandler();

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addFirst("idleStateHandler", new IdleStateHandler(10, 0, 0));
                            ch.pipeline().addAfter("idleStateHandler", "idleEventHandler", timeoutHandler);
                            ch.pipeline().addFirst("bytemetrics", new BytesMetricsHandler(bytesMetricsCollector));
                            ch.pipeline().addLast("decoder", new MqttDecoder());
                            ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
                            ch.pipeline().addLast("metrics", new MessageMetricsHandler(metricsCollector));

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
