package com.rookied;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class TankFrame extends Frame {
    public TankFrame(){
        setSize(800,600);
        //能否改变的大小
        //setResizable(false);
        setTitle("Tank");
        setVisible(true);addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    //窗口创建 改变大小 关闭时自动调用
    @Override
    public void paint(Graphics g){
        System.out.println(1);
        g.fill3DRect(200,200,50,50,true);
    }
}
