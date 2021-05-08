package com.rookied.net;

import com.rookied.Dir;
import com.rookied.Group;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.UUID;

/**
 * @author zhangqiang
 * @date 2021/5/7
 */
public class TankJoinMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if(in.readableBytes()<33) { //当前版本TcpJoinMsg属性字节加起来为33字节
            return;
        }
        TankJoinMsg msg = new TankJoinMsg();
        msg.x = in.readInt();
        msg.y = in.readInt();
        msg.dir = Dir.VALUES.get(in.readInt());
        msg.moving = in.readBoolean();
        msg.group = Group.values()[in.readInt()];
        msg.id = new UUID(in.readLong(),in.readLong());

        out.add(msg);
    }
}
