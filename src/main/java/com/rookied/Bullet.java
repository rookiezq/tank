package com.rookied;

import com.rookied.net.BulletNewMsg;
import com.rookied.net.Client;
import com.rookied.net.TankDieMsg;

import java.awt.*;
import java.util.UUID;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Bullet {
    //长宽
    public static final int WIDTH = ResourceMgr.bulletD.getWidth();
    public static final int HEIGHT = ResourceMgr.bulletD.getHeight();
    //速度
    private static final int SPEED = 10;
    //坐标
    private int x, y;
    //方向
    private Dir dir;
    //是否存活
    private boolean living = true;

    //默认是坏子弹
    private Group group;
    private UUID id = UUID.randomUUID();
    private UUID playerID;

    Rectangle rect = new Rectangle();

    public Bullet(UUID playerId, int x, int y, Dir dir, Group group) {
        this.playerID = playerId;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public Bullet(BulletNewMsg bulletNewMsg) {
        this.playerID = bulletNewMsg.playerID;
        this.id = bulletNewMsg.id;
        this.x = bulletNewMsg.x;
        this.y = bulletNewMsg.y;
        this.dir = bulletNewMsg.dir;
        this.group = bulletNewMsg.group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getX() {
        return x;
    }

    public Bullet setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Bullet setY(int y) {
        this.y = y;
        return this;
    }

    public Dir getDir() {
        return dir;
    }

    public Bullet setDir(Dir dir) {
        this.dir = dir;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public Bullet setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public Bullet setPlayerID(UUID playerID) {
        this.playerID = playerID;
        return this;
    }

    public void paint(Graphics g) {
        if (!living) {
            TankFrame.INSTANCE.bullets.remove(this);
            return;
        }
        switch (dir) {
            case LEFT:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case UP:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
            default:
                break;
        }
        move();
    }

    private void move() {
        switch (dir) {
            case UP:
                y -= SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
            case LEFT:
                x -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            default:
                break;
        }
        //更新rect
        rect.x = this.x;
        rect.y = this.y;

        if (x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT) {
            die();
        }
    }

    public void die() {
        this.living = false;
    }

    /**
     * 子弹和坦克的碰撞检测
     */
    public void collideWith(Tank tank) {
        if (tank.getId().equals(this.playerID)) {
            //System.out.println(22222);
            return;
        }
        if (this.rect.intersects(tank.rect)) {
            tank.die();
            this.die();
            Client.INSTANCE.send(new TankDieMsg(this.id, tank.getId()));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Bullet");
        sb.append("[");
        sb.append("x=").append(x);
        sb.append("| y=").append(y);
        sb.append("| dir=").append(dir);
        sb.append("| living=").append(living);
        sb.append("| group=").append(group);
        sb.append("| id=").append(id);
        sb.append("| playerID=").append(playerID);
        sb.append("| rect=").append(rect);
        sb.append(']');
        return sb.toString();
    }
}
