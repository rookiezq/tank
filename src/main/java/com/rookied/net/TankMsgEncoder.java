package com.rookied.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * @author zhangqiang
 * @date 2021/5/7
 */
public class TankMsgEncoder extends MessageToByteEncoder<TankMsg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TankMsg msg, ByteBuf out) {
        out.writeInt(msg.x);
        out.writeInt(msg.y);
    }
}
