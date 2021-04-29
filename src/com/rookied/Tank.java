package com.rookied;

import com.rookied.observer.FireListener;
import com.rookied.observer.Listener;
import com.rookied.observer.TankEvent;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Tank extends GameObject {
    public static final int WIDTH = ResourceMgr.goodTankD.getWidth();
    public static final int HEIGHT = ResourceMgr.goodTankD.getHeight();
    //移动距离
    private static final int SPEED = 3;
    //坦克随机发射子弹
    public static final Random RANDOM = new Random();

    Dir dir;
    //是否静止
    private boolean moving = true;
    private boolean living = true;

    //默认是坏子弹
    Group group;
    Rectangle rect = new Rectangle();
    FireStrategy<Tank> fs;

    public Tank(int x, int y, Dir dir, Group group) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        GameModel.getInstance().add(this);

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;

        String name;
        if (this.group == Group.GOOD) {
            name = PropertyMgr.get("goodFS");
        } else {
            name = PropertyMgr.get("badFS");
        }
        try {
            fs = (FireStrategy<Tank>) Class.forName(name).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Group getGroup() {
        return group;
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

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    //上一次的坐标
    private int lx, ly;

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    void back() {
        this.x = this.lx;
        this.y = this.ly;
    }

    @Override
    public void paint(Graphics g) {
        this.lx = this.x;
        this.ly = this.y;
        if (!living) {
            //TODO:自己被打后需要删除自己,否则子弹会显示黑洞
            /*if (this == tf.myTank) {
                g.dispose();
                return;
            }*/
            GameModel.getInstance().remove(this);
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
        fs.fire(this);
    }

    public void die() {
        this.living = false;
    }


    List<Listener> list = Collections.singletonList(new FireListener());
    public void handFireKey() {
        TankEvent tk = new TankEvent(this);
        for (Listener listener : list) {
            listener.action(tk);
        }
    }
}

