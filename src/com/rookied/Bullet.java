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
    private int x,y;
    //方向
    private Dir dir;

    public Bullet(int x, int y, Dir dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public void paint(Graphics g) {
        g.fillOval(x, y, WIDTH, HEIGHT);
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
    }
}
