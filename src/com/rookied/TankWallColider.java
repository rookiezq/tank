package com.rookied;

/**
 * @author zhangqiang
 * @date 2021/4/28
 */
public class TankWallColider implements Collider {
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Tank && o2 instanceof Wall) {
            Tank tank = (Tank) o1;
            Wall wall = (Wall) o2;
            //
            if (tank.rect.intersects(wall.rect)) {
                //同是敌方坦克
                tank.back();
            }
        } else if (o1 instanceof Wall && o2 instanceof Tank) {
            return collide(o2, o1);
        }
        return true;
    }
}
