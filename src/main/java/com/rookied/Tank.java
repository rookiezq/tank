package com.rookied;

import java.awt.*;
import java.util.Random;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Tank {
    public static final int WIDTH = ResourceMgr.goodTankD.getWidth();
    public static final int HEIGHT = ResourceMgr.goodTankD.getHeight();
    //移动距离
    private static final int SPEED = 3;
    //坦克随机发射子弹
    public static final Random RANDOM = new Random();

    private int x, y;
    private Dir dir;
    //是否静止
    private boolean moving = true;
    private boolean living = true;

    //获得TankFrame的引用
    private TankFrame tf;
    //默认是坏子弹
    private Group group;
    Rectangle rect = new Rectangle();

    public Tank(int x, int y, Dir dir, Group group, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
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

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
            tf.tanks.remove(this);
            return;
        }
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
        tf.bullets.add(new Bullet(bX, bY, dir, this.group, this.tf));
    }

    public void die() {
        this.living = false;
    }
}
