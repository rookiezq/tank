package com.rookied;

/**
 * @author zhangqiang
 * @date 2021/4/28
 */
public class TankWallColider implements Collider{
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        return false;
    }
}
