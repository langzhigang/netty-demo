package cn.lzg.netty.part1.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author wolflang
 * @date 2019/5/6
 */
//标记该类的实例可以被多个Channel共享
@ChannelHandler.Sharable
public class EchoClientHanndler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知Channel是活跃的时候，发送第一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("第一条 Netty 消息!", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

        //记录已接收消息的转储
        System.out.println("客户端收到消息: " + msg.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        //发生异常时，记录错误并关闭Channel
        cause.printStackTrace();
        ctx.close();
    }
}
