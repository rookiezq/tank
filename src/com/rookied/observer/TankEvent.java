package com.rookied.observer;

import com.rookied.Tank;

/**
 * @author zhangqiang
 * @date 2021/4/29
 */
public class TankEvent{
    private Tank source;
    public TankEvent(Tank source){
        this.source = source;
    }
    public Tank getSource(){
        return source;
    }
}
