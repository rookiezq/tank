package com.rookied;

import java.awt.*;

/**
 * 爆炸类
 *
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Explode extends GameObject{
    //长宽
    public static final int WIDTH = ResourceMgr.explodes[0].getWidth();
    public static final int HEIGHT = ResourceMgr.explodes[0].getHeight();
    //坐标
    private int x, y;

    GameModel gm;
    public Explode(int x, int y, GameModel gm) {
        this.x = x;
        this.y = y;
        this.gm = gm;
    }

    private int step = 0;

    @Override
    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.explodes[step++], this.x, this.y, null);
        if (step >= 16) gm.remove(this);
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

}
