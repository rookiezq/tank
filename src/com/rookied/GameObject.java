package com.rookied;

import java.awt.*;

/**
 * @author zhangqiang
 * @date 2021/4/27
 */
public abstract class GameObject {
    int x,y;
    public abstract void paint(Graphics g);
    public abstract int getWidth();
    public abstract int getHeight();
}
