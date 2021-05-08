package com.rookied.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author zhangqiang
 * @date 2021/5/7
 */
public class MsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        //拆包和粘包
        if (in.readableBytes() < 8) { //消息类型和长度一共8字节
            return;
        }
        in.markReaderIndex();//标记起始位置

        MsgType msgType = MsgType.values()[in.readInt()];
        int length = in.readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();//回到上面标记起始位置
            return;
        }
        //将消息体拷贝一份
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Msg msg = null;
        switch (msgType) {
            case TankJoin:
                msg = new TankJoinMsg();
                break;
            case TankStartMoving:
                msg = new TankStartMovingMsg();
                break;
            default:
                break;
        }
        assert msg != null;
        msg.parse(bytes);
        out.add(msg);
    }
}
