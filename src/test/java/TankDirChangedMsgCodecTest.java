import com.rookied.Dir;
import com.rookied.net.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author zhangqiang
 * @date 2021/5/8
 */
public class TankDirChangedMsgCodecTest {
    @Test
    void testEncoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        UUID id = UUID.randomUUID();
        //1
        TankDirChangedMsg msg = new TankDirChangedMsg(5, 10, Dir.DOWN, id);

        ch.pipeline().addLast(new MsgEncoder());
        ch.writeOutbound(msg);

        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        //2
        assertEquals(MsgType.TankDirChanged, msgType);

        int length = buf.readInt();
        assertEquals(msg.toBytes().length, length);
        //3
        int x = buf.readInt();
        int y = buf.readInt();
        int dirOrdinal = buf.readInt();
        Dir dir = Dir.VALUES.get(dirOrdinal);
        UUID uuid = new UUID(buf.readLong(), buf.readLong());
        //4
        assertEquals(5, x);
        assertEquals(10, y);
        assertEquals(Dir.DOWN, dir);
        assertEquals(id, uuid);
    }

    @Test
    void testDecoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new MsgDecoder());
        UUID id = UUID.randomUUID();
        //1
        TankDirChangedMsg msg = new TankDirChangedMsg(5, 10, Dir.DOWN,id);

        ByteBuf buf = Unpooled.buffer();
        //2
        buf.writeInt(MsgType.TankDirChanged.ordinal());

        byte[] bytes = msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        ch.writeInbound(buf.duplicate());

        TankDirChangedMsg msgR = ch.readInbound();
        //3
        assertEquals(5, msgR.x);
        assertEquals(10, msgR.y);
        assertEquals(Dir.DOWN, msgR.dir);
        assertEquals(id, msgR.id);
    }
}
