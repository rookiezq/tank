package com.rookied;

import java.awt.*;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Bullet {
    //速度
    private static final int SPEED = 5;
    //长宽
    private static final int WIDTH = 5, HEIGHT = 5;

    //坐标
    private int x, y;
    //方向
    private Dir dir;
    //是否存活
    private boolean live = true;

    //获得TankFrame的引用
    private TankFrame tf;

    public Bullet(int x, int y, Dir dir, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.tf = tf;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);
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
            live = false;
        }
    }
}
