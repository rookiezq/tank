package com.rookied.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * @author zhangqiang
 * @date 2021/5/5
 */
public class Client {
    int port = 10001;
    private Channel channel;

    public Client() {
    }

    public void connect() {
        EventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();

        try {
            ChannelFuture f = b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new TankJoinMsgEncoder())
                                    .addLast(new ClientHandler());
                        }
                    })
                    .connect("localhost", this.port)
                    .addListener((ChannelFutureListener) future -> {
                                if (future.isSuccess()) {
                                    System.out.println("conneted");
                                    channel = future.channel();
                                } else {
                                    System.out.println("not conneted");
                                }
                            }
                    ).sync();
            //System.out.println("....");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void send(String msg) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(msg.getBytes());
        channel.writeAndFlush(byteBuf);
    }

    public void closeConnect() {
        this.send("_bye_");
        //channel.close();
    }

    public static void main(String[] args) {
        //new Thread(() -> new Client().connect()).start();
        //new Thread(()->new Client().connect("world")).start();

    }
}

class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channel is activated");
        //ctx.channel().writeAndFlush(new TankJoinMsg());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = null;
        try {

            buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String msgAccepted = new String(bytes);
            //System.out.println(Thread.currentThread().getName() + "------>" + msgAccepted);
            //ClientFrame.INSTENCE.updateTxt(msgAccepted);
        } finally {
            if (buf != null) {
                ReferenceCountUtil.release(buf);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        Server.clients.remove(ctx.channel());
        ctx.close();
    }
}
