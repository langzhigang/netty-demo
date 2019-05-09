package cn.lzg.netty.part1.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author wolflang
 * @date 2019/5/6
 */
public class EchoServer {

    private final int port = 9999;

    public void start() throws Exception {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();

        //创建Event-LoopGroup
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {

            //创建Server-Bootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(eventLoopGroup)
                    //指定所使用的 NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    //添加一个EchoServer-Handler到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //EchoServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例
                            ch.pipeline().addLast(echoServerHandler);
                        }
                    });

            //异步地绑定服务器；调用sync()方法阻塞等待，直到绑定完成
            ChannelFuture channelFuture = serverBootstrap.bind().sync();

            //获取Channel的CloseFuture,并且阻塞当前线程直到它完成
            channelFuture.channel().closeFuture().sync();

        } finally {
            //关闭EventLoopGroup，释放所有的资源
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {

        new EchoServer().start();
    }
}
