package com.rookied;

import java.awt.*;
import java.io.Serializable;

/**
 * @author zhangqiang
 * @date 2021/4/27
 */
public abstract class GameObject  implements Serializable {
    private static final long serialVersionUID = 5703789534993655916L;
    int x,y;
    public abstract void paint(Graphics g);
    public abstract int getWidth();
    public abstract int getHeight();
}
