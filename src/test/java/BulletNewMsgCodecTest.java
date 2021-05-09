import com.rookied.Dir;
import com.rookied.Group;
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
public class BulletNewMsgCodecTest {
    @Test
    void testEncoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        UUID id = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        //1
        BulletNewMsg msg = new BulletNewMsg(playerId, id, 5, 10, Dir.DOWN, Group.BAD);

        ch.pipeline().addLast(new MsgEncoder());
        ch.writeOutbound(msg);

        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        //2
        assertEquals(MsgType.BulletNew, msgType);

        int length = buf.readInt();
        assertEquals(msg.toBytes().length, length);
        //3
        int x = buf.readInt();
        int y = buf.readInt();
        int dirOrdinal = buf.readInt();
        Dir dir = Dir.VALUES.get(dirOrdinal);
        int groupOrdinal = buf.readInt();
        Group group = Group.values()[groupOrdinal];
        UUID idN = new UUID(buf.readLong(), buf.readLong());
        UUID playerIdN = new UUID(buf.readLong(), buf.readLong());
        //4
        assertEquals(5, x);
        assertEquals(10, y);
        assertEquals(Dir.DOWN, dir);
        assertEquals(Group.BAD, group);
        assertEquals(id, idN);
        assertEquals(playerId, playerIdN);
    }

    @Test
    void testDecoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new MsgDecoder());
        UUID id = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        //1
        BulletNewMsg msg = new BulletNewMsg(playerId, id, 5, 10, Dir.DOWN, Group.BAD);

        ByteBuf buf = Unpooled.buffer();
        //2
        buf.writeInt(MsgType.BulletNew.ordinal());

        byte[] bytes = msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        ch.writeInbound(buf.duplicate());

        //3
        BulletNewMsg msgR = ch.readInbound();

        assertEquals(5, msgR.x);
        assertEquals(10, msgR.y);
        assertEquals(Dir.DOWN, msgR.dir);
        assertEquals(Group.BAD, msgR.group);
        assertEquals(id, msgR.id);
        assertEquals(playerId,msgR.playerID);
    }
}
