package com.rookied.net;

import com.rookied.*;

import java.io.*;
import java.util.UUID;

/**
 * @author zhangqiang
 * @date 2021/5/9
 */
public class BulletNewMsg extends Msg {
    public UUID playerID;
    public UUID id;
    public int x, y;
    public Dir dir;
    public Group group;

    public BulletNewMsg() {
    }

    public BulletNewMsg(Bullet bullet) {
        this.x = bullet.getX();
        this.y = bullet.getY();
        this.dir = bullet.getDir();
        this.group = bullet.getGroup();
        this.id = bullet.getId();
        this.playerID = bullet.getPlayerID();
    }

    public BulletNewMsg(UUID playerID, UUID id, int x, int y, Dir dir, Group group) {
        this.playerID = playerID;
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
    }

    @Override
    public void handle() {
        if (this.playerID.equals(TankFrame.INSTANCE.getMyTank().getId()))
            return;

        TankFrame.INSTANCE.addBullet(new Bullet(this));
        //Client.INSTANCE.send(new BulletNewMsg());
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);

            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeInt(group.ordinal());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeLong(playerID.getMostSignificantBits());
            dos.writeLong(playerID.getLeastSignificantBits());

            dos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        ByteArrayInputStream bais = null;
        DataInputStream dis = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            dis = new DataInputStream(bais);

            this.x = dis.readInt();
            this.y = dis.readInt();
            this.dir = Dir.VALUES.get(dis.readInt());
            this.group = Group.values()[dis.readInt()];
            this.id = new UUID(dis.readLong(), dis.readLong());
            this.playerID = new UUID(dis.readLong(), dis.readLong());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.BulletNew;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BulletNew");
        sb.append("[");
        sb.append("playerID=").append(playerID);
        sb.append("| id=").append(id);
        sb.append("| x=").append(x);
        sb.append("| y=").append(y);
        sb.append("| dir=").append(dir);
        sb.append("| group=").append(group);
        sb.append(']');
        return sb.toString();
    }
}
