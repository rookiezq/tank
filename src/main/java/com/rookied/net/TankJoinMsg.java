package com.rookied.net;

import com.rookied.Dir;
import com.rookied.Group;
import com.rookied.Tank;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author zhangqiang
 * @date 2021/5/7
 */
public class TankJoinMsg {
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

}
