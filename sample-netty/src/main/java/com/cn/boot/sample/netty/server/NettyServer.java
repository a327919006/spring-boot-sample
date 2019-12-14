package com.cn.boot.sample.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title:</p>
 * <p>Description:</p>
 *
 * @author Chen Nan
 * @date 2018/3/22.
 */
@Slf4j
@Component
public class NettyServer {

    /**
     * 单线程-线程池
     */
    private ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), new ThreadPoolExecutor.AbortPolicy());
    /**
     * 用来接收进来的连接
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     * 用来处理已经被接收的连接，一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * 端口号
     */
    @Value("${netty.port}")
    private Integer port;

    @PostConstruct
    public void contextInitialized() {
        // 要用线程，否则会阻塞主线程
        singleThreadPool.execute(this::runServer);
    }

    public void runServer() {
        log.info("【netty server start run】port:" + port);

        try {
            // 是一个启动NIO服务的辅助启动类。
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 设置Marshalling消息编解码器
                            ch.pipeline().addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            ch.pipeline().addLast(new ObjectEncoder());
                            // 设置消息处理器
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    })
                    // 默认128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


            ChannelFuture future = bootstrap.bind(port).sync();

            log.info("【netty server running】");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void contextDestroyed() {
        // 关闭netty服务器
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        // 关闭线程池
        singleThreadPool.shutdown();
        log.info("【netty server stop】");
        // 睡眠3秒，等待netty关闭结束，不睡的话会抛异常，但不影响流程
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
