package com.rookied;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangqiang
 * @date 2021/4/27
 */
public class GameModel {
    Tank myTank = new Tank(200, 400, Dir.DOWN, Group.GOOD, this);
    //敌方坦克
    List<Tank> tanks = new ArrayList<>();
    //Bullet bullet = new Bullet(225, 250, Dir.DOWN);
    List<Bullet> bullets = new ArrayList<>();
    List<Explode> explodes = new ArrayList<>();

    public GameModel() {
        int count = Integer.parseInt(PropertyMgr.get("initTankCount"));
        for (int i = 0; i < count; i++) {
            tanks.add(new Tank(50 + i * 80, 200, Dir.DOWN, Group.BAD, this));
        }
    }

    public void paint(Graphics g){
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("敌人的数量:" + tanks.size(), 10, 60);
        g.drawString("子弹的数量:" + bullets.size(), 10, 80);
        g.drawString("爆炸的数量:" + explodes.size(), 10, 100);
        g.setColor(c);
        //如果把tank的属性取出来再画,那就破坏了对象的封装,所以需要类自己实现这个方法
        myTank.paint(g);
        //多颗子弹 用foreach 在删除子弹时会报错,因为迭代器内部指针会混乱
        //bullets.forEach(x->x.paint(g));
        //这样写由于每次bullets.size()会重新计算,所以不会报错
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }

        for (int i = 0; i < tanks.size(); i++) {
            tanks.get(i).paint(g);
        }

        for (int i = 0; i < explodes.size(); i++) {
            explodes.get(i).paint(g);
        }

        //碰撞检测 所有子弹和所有坦克互相检测
        for (int i = 0; i < bullets.size(); i++) {
            //敌方坦克可以伤害自己
            //bullets.get(i).collideWith(myTank);
            for (int j = 0; j < tanks.size(); j++) {
                bullets.get(i).collideWith(tanks.get(j));
            }
        }
        /*for (Iterator<Bullet> it = bullets.listIterator();it.hasNext();){
            Bullet b = it.next();
            if(!b.isLive()){
                it.remove();
                continue;
            }
            b.paint(g);
        }*/
    }

    public Tank getMainTank() {
        return myTank;
    }
}
