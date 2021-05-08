package com.rookied.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author zhangqiang
 * @date 2021/5/7
 */
public class TankMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if(in.readableBytes()<8) {
            return;
        }
        int x = in.readInt();
        int y = in.readInt();

        out.add(new TankMsg(x,y));
    }
}
