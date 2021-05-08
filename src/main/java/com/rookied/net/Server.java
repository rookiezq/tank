package com.rookied.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author zhangqiang
 * @date 2021/5/6
 */
public class Server {
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    int port = 10001;

    public void start() {
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);

        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new TankJoinMsgDecoder())
                                    .addLast(new TankJoinMsgEncoder())
                                    .addLast(new Handler());
                        }
                    });
            ChannelFuture f = b.bind(this.port)
                    .sync();
            ServerFrame.INSTANCE.updateServerMsg("server started!");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new Server().start();
    }
}

class Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Server.clients.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        TankJoinMsg tm = (TankJoinMsg) msg;
        System.out.println(tm);
        Server.clients.writeAndFlush(msg);
        /*System.out.println("channelRead");
        try {
            TankJoinMsg tm = (TankJoinMsg) msg;

            System.out.println(tm);
        } finally {
            ReferenceCountUtil.release(msg);
        }*/
        /*ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), bytes);
        String s = new String(bytes);
        if ("_bye_".equals(s)) {
            System.out.println("客户端断开连接");
            ctx.close();
            Server.clients.remove(ctx.channel());
        } else {
            Server.clients.writeAndFlush(msg);
            ServerFrame.INSTANCE.updateClientMsg(s);
        }*/
    }

    //Server异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        Server.clients.remove(ctx.channel());
        ctx.close();
    }
}