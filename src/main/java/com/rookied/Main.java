package com.rookied;

import com.rookied.net.Client;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Main {
    public static void main(String[] args) {
        TankFrame tf = TankFrame.INSTANCE;
        tf.setVisible(true);
        /*int count = Integer.parseInt(PropertyMgr.get("initTankCount"));
        for (int i = 0; i < count; i++) {
            tf.tanks.add(new Tank(50 + i * 80, 200, Dir.DOWN, Group.BAD, tf));
        }*/
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tf.repaint();
            }
        }).start();

        Client.INSTANCE.connect();
    }
}
