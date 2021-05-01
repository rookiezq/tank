package com.rookied.observer;

import java.io.Serializable;

/**
 * @author zhangqiang
 * @date 2021/4/29
 */
public interface Listener extends Serializable {
    void action(TankEvent event);
}
