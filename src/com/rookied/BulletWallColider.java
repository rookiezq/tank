package com.rookied;

/**
 * @author zhangqiang
 * @date 2021/4/28
 */
public class BulletWallColider implements Collider {
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Bullet && o2 instanceof Wall) {
            Bullet bullet = (Bullet) o1;
            Wall wall = (Wall) o2;
            if(bullet.rect.intersects(wall.rect)){
                bullet.die();
                return false;
            }
        } else if (o1 instanceof Wall && o2 instanceof Bullet) {
            return collide(o2, o1);
        }
        return true;
    }
}
