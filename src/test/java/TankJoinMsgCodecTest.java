import com.rookied.Dir;
import com.rookied.Group;
import com.rookied.net.MsgType;
import com.rookied.net.TankJoinMsg;
import com.rookied.net.TankJoinMsgDecoder;
import com.rookied.net.TankJoinMsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author zhangqiang
 * @date 2021/5/8
 */
public class TankJoinMsgCodecTest {
    @Test
    void testEncoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        UUID id = UUID.randomUUID();
        TankJoinMsg msg = new TankJoinMsg(5, 10, Dir.DOWN, true, Group.BAD, id);
        ch.pipeline().addLast(new TankJoinMsgEncoder());
        ch.writeOutbound(msg);

        ByteBuf buf = ch.readOutbound();
        MsgType msgType = MsgType.values()[buf.readInt()];
        assertEquals(MsgType.TankJoin, msgType);
        int length = buf.readInt();
        assertEquals(33, length);
        int x = buf.readInt();
        int y = buf.readInt();
        int dirOrdinal = buf.readInt();
        Dir dir = Dir.VALUES.get(dirOrdinal);
        boolean moving = buf.readBoolean();
        int groupOrdinal = buf.readInt();
        Group g = Group.values()[groupOrdinal];
        UUID uuid = new UUID(buf.readLong(), buf.readLong());

        assertEquals(5, x);
        assertEquals(10, y);
        assertEquals(Dir.DOWN, dir);
        assertTrue(moving);
        assertEquals(Group.BAD, g);
        assertEquals(id, uuid);
    }

    @Test
    void testDecoder() {
        EmbeddedChannel ch = new EmbeddedChannel();

        ch.pipeline().addLast(new TankJoinMsgDecoder());
        UUID id = UUID.randomUUID();
        TankJoinMsg msg = new TankJoinMsg(5, 10, Dir.DOWN, true, Group.BAD, id);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(MsgType.TankJoin.ordinal());
        byte[] bytes = msg.toBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        ch.writeInbound(buf.duplicate());

        TankJoinMsg msgR = ch.readInbound();
        assertEquals(5, msgR.x);
        assertEquals(10, msgR.y);
        assertEquals(Dir.DOWN, msgR.dir);
        assertTrue(msgR.moving);
        assertEquals(Group.BAD, msgR.group);
        assertEquals(id, msgR.id);
    }
}
