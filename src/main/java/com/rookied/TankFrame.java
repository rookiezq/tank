package com.rookied;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.*;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class TankFrame extends Frame {

    public static final TankFrame INSTANCE = new TankFrame();
    static final int GAME_WIDTH = 800, GAME_HEIGHT = 600;
    Random r = new Random();

    Tank myTank = new Tank(r.nextInt(GAME_WIDTH), r.nextInt(GAME_HEIGHT), Dir.DOWN, Group.GOOD, this);

    //所有坦克
    Map<UUID, Tank> tanks = new HashMap<>();
    //Bullet bullet = new Bullet(225, 250, Dir.DOWN);
    List<Bullet> bullets = new ArrayList<>();
    List<Explode> explodes = new ArrayList<>();

    private TankFrame() {
        setSize(GAME_WIDTH, GAME_HEIGHT);
        //能否改变的大小
        //setResizable(false);
        setTitle("Tank");
        setAlwaysOnTop(true);
        myTank.setMoving(false);
        this.addKeyListener(new MyKeyListener());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void addTank(Tank tank) {
        tanks.put(tank.getId(), tank);
    }

    public Tank findByUUID(UUID id) {
        return tanks.get(id);
    }

    /**
     * 处理双缓冲,解决屏幕闪烁问题
     * 1. 运行在paint之前
     * 2. 将需要画的内容先在内存中的图片中画出,图片大小和游戏画面一致
     * 3. 把内存中的图片一次性画到屏幕上(内存的内容复制到显存中)
     */
    Image offScreenImage = null;

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    /**
     * 窗口创建 改变大小时自动调用
     * 背景先清一遍再重新绘图
     */
    @Override
    public void paint(Graphics g) {
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

        tanks.values().forEach(e -> e.paint(g));

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

    public Tank getMyTank() {
        return this.myTank;
    }

    class MyKeyListener extends KeyAdapter {
        boolean bL = false;
        boolean bU = false;
        boolean bR = false;
        boolean bD = false;

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bL = true;
                    break;
                case KeyEvent.VK_UP:
                    bU = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = true;
                    break;
                default:
                    break;
            }
            setMainTankDir();
            //自动调用paint()
            //repaint();

        }

        @Override
        public void keyReleased(KeyEvent e) {
            //System.out.println("按键抬起");
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_LEFT:
                    bL = false;
                    break;
                case KeyEvent.VK_UP:
                    bU = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = false;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = false;
                    break;
                case KeyEvent.VK_CONTROL:
                    myTank.fire();
                    break;
                default:
                    break;
            }
            setMainTankDir();
        }

        private void setMainTankDir() {
            if (!bL && !bR && !bU && !bD) {
                myTank.setMoving(false);
            } else {
                myTank.setMoving(true);
                if (bL) {
                    myTank.setDir(Dir.LEFT);
                }
                if (bR) {
                    myTank.setDir(Dir.RIGHT);
                }
                if (bU) {
                    myTank.setDir(Dir.UP);
                }
                if (bD) {
                    myTank.setDir(Dir.DOWN);
                }
            }
        }
    }
}
