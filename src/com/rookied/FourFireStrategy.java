package com.rookied;

/**
 * @author zhangqiang
 * @date 2021/4/26
 */
public class FourFireStrategy implements FireStrategy<Tank> {
    @Override
    public void fire(Tank tank) {
        //设置子弹的起始位置,从坦克的中心打出
        int bX = tank.getX() + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = tank.getY() + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
        //朝四个方向打子弹
        for (Dir dir : Dir.VALUES) {
            new Bullet(bX, bY, dir, tank.group, tank.tf);
        }
    }

    private static class FourInstance{
        private static final  FourFireStrategy INSTANCE = new FourFireStrategy();
    }

    public static FourFireStrategy getInstence(){
        return FourInstance.INSTANCE;
    }
}
