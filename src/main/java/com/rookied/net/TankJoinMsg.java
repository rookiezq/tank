package com.rookied.net;

import com.rookied.Dir;
import com.rookied.Group;
import com.rookied.Tank;
import com.rookied.TankFrame;

import java.io.*;
import java.util.UUID;

/**
 * @author zhangqiang
 * @date 2021/5/7
 */
public class TankJoinMsg extends Msg {
    //所有属性加起来33字节
    public int x, y;
    public Dir dir;
    public boolean moving;
    public Group group;
    //128位 16字节
    public UUID id;

    public TankJoinMsg() {
    }

    public TankJoinMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.moving = moving;
        this.group = group;
        this.id = id;
    }

    public TankJoinMsg(Tank tank) {
        this.x = tank.getX();
        this.y = tank.getY();
        this.dir = tank.getDir();
        this.moving = tank.isMoving();
        this.group = tank.getGroup();
        this.id = tank.getId();
    }

    @Override
    public void handle() {
        if (this.id.equals(TankFrame.INSTANCE.getMyTank().getId()) || TankFrame.INSTANCE.findByUUID(this.id) != null)
            return;
        TankFrame.INSTANCE.addTank(new Tank(this));
        //让新坦克能看到老坦克
        Client.INSTANCE.send(new TankJoinMsg(TankFrame.INSTANCE.getMyTank()));
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
            //枚举本质是个数组,ordinal()为对象的下标
            dos.writeInt(dir.ordinal());
            dos.writeBoolean(moving);
            dos.writeInt(group.ordinal());
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());

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

            this.x=dis.readInt();
            this.y=dis.readInt();
            this.dir=Dir.VALUES.get(dis.readInt());
            this.moving=dis.readBoolean();
            this.group=Group.values()[dis.readInt()];
            this.id=new UUID(dis.readLong(),dis.readLong());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
        return MsgType.TankJoin;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append('[');
        sb.append("x=").append(x);
        sb.append("| y=").append(y);
        sb.append("| dir=").append(dir);
        sb.append("| moving=").append(moving);
        sb.append("| group=").append(group);
        sb.append("| id=").append(id);
        sb.append(']');
        return sb.toString();
    }
}
