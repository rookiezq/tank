package com.rookied.observer;

import com.rookied.Tank;

/**
 * @author zhangqiang
 * @date 2021/4/29
 */
public class FireListener implements Listener {

    @Override
    public void action(TankEvent event) {
        Tank tank = event.getSource();
        tank.fire();
    }
}
