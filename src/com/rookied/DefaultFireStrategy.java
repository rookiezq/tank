package com.rookied;

/**
 * @author zhangqiang
 * @date 2021/4/26
 */
public class DefaultFireStrategy implements FireStrategy<Tank> {
    @Override
    public void fire(Tank tank) {
        //设置子弹的起始位置,从坦克的中心打出
        int bX = tank.getX() + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = tank.getY() + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
        new Bullet(bX, bY, tank.dir, tank.group);
    }

    private static class DefaultInstance{
        private static final  DefaultFireStrategy INSTANCE = new DefaultFireStrategy();
    }

    public static DefaultFireStrategy getInstence(){
        return DefaultInstance.INSTANCE;
    }
}
