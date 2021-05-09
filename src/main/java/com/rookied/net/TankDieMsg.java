package com.rookied.net;

import com.rookied.Bullet;
import com.rookied.Tank;
import com.rookied.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankDieMsg extends Msg {
    UUID bulletId; //who killed me
    UUID id;

    public TankDieMsg(UUID playerId, UUID id) {
        this.id = id;
        this.bulletId = playerId;
    }

    public TankDieMsg() {
    }

    public UUID getBulletId() {
        return bulletId;
    }

    public TankDieMsg setBulletId(UUID bulletId) {
        this.bulletId = bulletId;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public TankDieMsg setId(UUID id) {
        this.id = id;
        return this;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            dos = new DataOutputStream(baos);
            dos.writeLong(id.getMostSignificantBits());
            dos.writeLong(id.getLeastSignificantBits());
            dos.writeLong(bulletId.getMostSignificantBits());
            dos.writeLong(bulletId.getLeastSignificantBits());
            dos.flush();
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            this.id = new UUID(dis.readLong(), dis.readLong());
            this.bulletId = new UUID(dis.readLong(), dis.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void handle() {
        System.out.println("we got a tank die:" + id);
        System.out.println("and my tank is:" + TankFrame.INSTANCE.getMyTank().getId());
        Tank tank = TankFrame.INSTANCE.findByUUID(id);
        System.out.println("i found a tank with this id:" + tank);

        Bullet bullet = TankFrame.INSTANCE.findBulletsByID(bulletId);
        if (bullet != null) {
            bullet.die();
        }

        if (TankFrame.INSTANCE.getMyTank().getId().equals(id)) {
            TankFrame.INSTANCE.getMyTank().die();
        } else {
            if (tank != null) {
                tank.die();
            }
        }
    }


    @Override
    public MsgType getMsgType() {
        return MsgType.TankDie;
    }

}
