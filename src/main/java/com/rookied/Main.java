package com.rookied;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        TankFrame tf = new TankFrame();
        /*int count = Integer.parseInt(PropertyMgr.get("initTankCount"));
        for (int i = 0; i < count; i++) {
            tf.tanks.add(new Tank(50 + i * 80, 200, Dir.DOWN, Group.BAD, tf));
        }*/
        while (true) {
            Thread.sleep(50);
            tf.repaint();
        }
    }
}
