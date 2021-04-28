package com.rookied;

/**
 * @author zhangqiang
 * @date 2021/4/27
 */
public class BulletTankCollider implements Collider {
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Bullet && o2 instanceof Tank) {
            Bullet bullet = (Bullet) o1;
            Tank tank = (Tank) o2;

            if (tank.getGroup() == bullet.getGroup()) {
                return true;
            }
            if (bullet.rect.intersects(tank.rect)) {
                tank.die();
                bullet.die();
                int eX = tank.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
                int eY = tank.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
                bullet.gm.add(new Explode(eX, eY, bullet.gm));
                return false;
            }
        } else if (o1 instanceof Tank && o2 instanceof Bullet) {
            return collide(o2, o1);
        }
        return true;
    }
}
