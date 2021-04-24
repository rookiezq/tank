package com.rookied;

import java.awt.*;

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

    //获得TankFrame的引用
    private TankFrame tf;

    public Bullet(int x, int y, Dir dir, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.tf = tf;
    }

    public void paint(Graphics g) {
        if (!living) {
            tf.bullets.remove(this);
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
        Rectangle recT = new Rectangle(tank.getX(),tank.getY(),Tank.WIDTH,Tank.HEIGHT);
        Rectangle recB = new Rectangle(this.x,this.y,WIDTH,HEIGHT);
        if(recT.intersects(recB)){
            tank.die();
            this.die();
        }
    }
}