package com.rookied;

/**
 * @author zhangqiang
 * @date 2021/4/27
 */
public class TankTankCollider implements Collider {
    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        if (o1 instanceof Tank && o2 instanceof Tank) {
            Tank tank1 = (Tank) o1;
            Tank tank2 = (Tank) o2;

            //
            if (tank1.rect.intersects(tank2.rect)) {
                if (tank1.getGroup() == tank2.getGroup()) {
                    //同是敌方坦克
                    tank1.back();
                    tank2.back();
                    return true;
                }
                tank1.die();
                tank2.die();
                int e1X = tank1.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
                int e1Y = tank1.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
                int e2X = tank2.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
                int e2Y = tank2.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
                tank1.gm.add(new Explode(e1X, e1Y, tank1.gm));
                tank2.gm.add(new Explode(e2X, e2Y, tank2.gm));
                return false;
            }
        }
        return true;
    }
}
