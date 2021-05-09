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
public class TankDieMsgCodecTest {
    @Test
    void testEncoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        //1
        UUID id = UUID.randomUUID();
        UUID bid = UUID.randomUUID();
        TankDieMsg msg = new TankDieMsg(bid,id);

        ch.pipeline().addLast(new MsgEncoder());
        ch.writeOutbound(msg);

        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        //2
        assertEquals(MsgType.TankDie, msgType);

        int length = buf.readInt();
        assertEquals(msg.toBytes().length, length);
        //3
        UUID idN = new UUID(buf.readLong(), buf.readLong());
        UUID bidN = new UUID(buf.readLong(), buf.readLong());
        //4
        assertEquals(id, idN);
        assertEquals(bid, bidN);
    }

    @Test
    void testDecoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new MsgDecoder());
        //1
        UUID id = UUID.randomUUID();
        UUID bid = UUID.randomUUID();
        TankDieMsg msg = new TankDieMsg(bid,id);

        ByteBuf buf = Unpooled.buffer();
        //2
        buf.writeInt(MsgType.TankDie.ordinal());

        byte[] bytes = msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        ch.writeInbound(buf.duplicate());

        //3
        TankDieMsg msgR = ch.readInbound();

        assertEquals(id, msgR.getId());
        assertEquals(bid, msgR.getBulletId());
    }
}
