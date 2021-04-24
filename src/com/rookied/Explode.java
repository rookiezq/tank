package com.rookied;

import java.awt.*;

/**
 * 爆炸类
 *
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Explode {
    //长宽
    public static final int WIDTH = ResourceMgr.explodes[0].getWidth();
    public static final int HEIGHT = ResourceMgr.explodes[0].getHeight();
    //坐标
    private int x, y;

    //获得TankFrame的引用
    private TankFrame tf;

    public Explode(int x, int y, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.tf = tf;
    }

    private int step = 1;

    public void paint(Graphics g) {
        g.drawImage(ResourceMgr.explodes[step++], this.x, this.y, null);
        if (step >= 16) step = 1;
    }

}
