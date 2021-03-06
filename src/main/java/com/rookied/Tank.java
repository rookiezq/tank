package com.rookied;

import com.rookied.net.BulletNewMsg;
import com.rookied.net.Client;
import com.rookied.net.TankJoinMsg;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Tank {
    public static final int WIDTH = ResourceMgr.goodTankD.getWidth();
    public static final int HEIGHT = ResourceMgr.goodTankD.getHeight();
    //坦克随机发射子弹
    public static final Random RANDOM = new Random();
    //移动距离
    private static final int SPEED = 3;
    Rectangle rect = new Rectangle();
    UUID id = UUID.randomUUID();
    private int x, y;
    private Dir dir;
    //是否静止
    private boolean moving = false;
    private boolean living = true;
    //默认是坏子弹
    private Group group;

    public Tank(int x, int y, Dir dir, Group group, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public Tank(TankJoinMsg msg) {
        this.x = msg.x;
        this.y = msg.y;
        this.dir = msg.dir;
        this.moving = msg.moving;
        this.group = msg.group;
        this.id = msg.id;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public UUID getId() {
        return id;
    }


    public Group getGroup() {
        return group;
    }


    public int getX() {
        return x;
    }

    public Tank setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Tank setY(int y) {
        this.y = y;
        return this;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void paint(Graphics g) {
        if (!living) {
            //TODO:自己被打后需要删除自己,否则子弹会显示黑洞
            /*if (this == tf.myTank) {
                g.dispose();
                return;
            }*/
            return;
        }
        //uuid on head
        Color c = g.getColor();
        g.setColor(Color.YELLOW);
        g.drawString(id.toString(), this.x, this.y - 10);
        g.setColor(c);
        switch (dir) {
            case LEFT:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankL : ResourceMgr.badTankL, x, y, null);
                break;
            case UP:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankU : ResourceMgr.badTankU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankR : ResourceMgr.badTankR, x, y, null);
                break;
            case DOWN:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankD : ResourceMgr.badTankD, x, y, null);
                break;
            default:
                break;
        }
        move();
    }

    private void move() {
        if (!moving) {
            return;
        }
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

        if (Group.BAD == this.group) {
            //随机发射子弹
            if (RANDOM.nextInt(100) > 97) this.fire();
            //随机朝另外三个方向转动
            if (RANDOM.nextInt(100) > 95) {
                dir = Dir.randomDir();
            }
        }
        boudsCheck();

        //更新rect
        rect.x = this.x;
        rect.y = this.y;
    }

    /**
     * 边界检测
     */
    private void boudsCheck() {
        if (x <= 0) x = 0;
        if (x >= TankFrame.GAME_WIDTH - WIDTH) x = TankFrame.GAME_WIDTH - WIDTH;
        if (y <= 30) y = 30;
        if (y >= TankFrame.GAME_HEIGHT - HEIGHT) y = TankFrame.GAME_HEIGHT - HEIGHT;

    }

    public void fire() {
        //设置子弹的起始位置,从坦克的中心打出
        int bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
        Bullet bullet = new Bullet(this.id, bX, bY, dir, this.group);
        TankFrame.INSTANCE.addBullet(bullet);
        Client.INSTANCE.send(new BulletNewMsg(bullet));
    }

    public void die() {
        this.living = false;
        int eX = this.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
        int eY = this.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
        TankFrame.INSTANCE.explodes.add(new Explode(eX, eY));
        TankFrame.INSTANCE.tanks.remove(this.id);
        Client.INSTANCE.closeConnect();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Tank");
        sb.append("[");
        sb.append("rect=").append(rect);
        sb.append("| id=").append(id);
        sb.append("| x=").append(x);
        sb.append("| y=").append(y);
        sb.append("| dir=").append(dir);
        sb.append("| moving=").append(moving);
        sb.append("| living=").append(living);
        sb.append("| group=").append(group);
        sb.append(']');
        return sb.toString();
    }
}
