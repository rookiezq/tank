package com.rookied;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        TankFrame tf = new TankFrame();
        while(true){
            Thread.sleep(50);
            tf.repaint();
        }
    }
}