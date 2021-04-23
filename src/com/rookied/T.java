package com.rookied;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class T {
    public static void main(String[] args) {
        Frame f = new Frame();
        f.setSize(800,600);
        f.setResizable(false);
        f.setTitle("Tank");
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }
}
