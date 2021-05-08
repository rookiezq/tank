package com.rookied.net;

import com.rookied.Tank;
import com.rookied.TankFrame;

import java.io.*;
import java.util.UUID;

/**
 * @author zhangqiang
 * @date 2021/5/8
 */
public class TankStopMsg extends Msg {
    public UUID id;
    public int x, y;

    public TankStopMsg() {
    }

    public TankStopMsg(int x, int y,UUID id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public TankStopMsg(Tank tank) {
        this.id = tank.getId();
        this.x = tank.getX();
        this.y = tank.getY();
    }

    @Override
    public void handle() {
        if (this.id.equals(TankFrame.INSTANCE.getMyTank().getId()))
            return;
        Tank tank = TankFrame.INSTANCE.findByUUID(this.id);
        if (tank != null) {
            tank.setMoving(false);
            tank.setX(this.x);
            tank.setY(this.y);
        }
        //TankFrame.INSTANCE.addTank(tank);
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

            this.x = dis.readInt();
            this.y = dis.readInt();
            this.id = new UUID(dis.readLong(), dis.readLong());

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
        return MsgType.TankStop;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TankStopMsg");
        sb.append("[");
        sb.append("id=").append(id);
        sb.append("| x=").append(x);
        sb.append("| y=").append(y);
        sb.append(']');
        return sb.toString();
    }
}
