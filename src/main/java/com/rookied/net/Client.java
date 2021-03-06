package com.rookied.net;

import com.rookied.PropertyMgr;
import com.rookied.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author zhangqiang
 * @date 2021/5/5
 */
public class Client {
    public static final Client INSTANCE = new Client();
    int port = Integer.parseInt(PropertyMgr.get("port"));
    private Channel channel;

    private Client() {
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
                                    .addLast(new MsgEncoder())
                                    .addLast(new MsgDecoder())
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

    public void send(Msg msg) {
        channel.writeAndFlush(msg);
    }

    public void closeConnect() {
        //this.send("_bye_");
        //channel.close();
    }

    public static void main(String[] args) {
        //new Thread(() -> new Client().connect()).start();
        //new Thread(()->new Client().connect("world")).start();

    }
}

class ClientHandler extends SimpleChannelInboundHandler<Msg> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //System.out.println("channel is activated");
        ctx.channel().writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getMyTank()));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Msg msg) {
        msg.handle();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        Server.clients.remove(ctx.channel());
        ctx.close();
    }
}
