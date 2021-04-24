package com.rookied;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author zhangqiang
 * @date 2021/4/24
 */
public class TankFrame extends Frame {
    Tank myTank = new Tank(200, 200, Dir.DOWN);
    Bullet bullet = new Bullet(225, 225, Dir.DOWN);

    public TankFrame() {
        setSize(800, 600);
        //能否改变的大小
        //setResizable(false);
        setTitle("Tank");
        setVisible(true);

        this.addKeyListener(new MyKeyListener());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * 窗口创建 改变大小时自动调用
     * 背景先清一遍再重新绘图
     */
    @Override
    public void paint(Graphics g) {
        //如果把tank的属性取出来再画,那就破坏了对象的封装,所以需要类自己实现这个方法
        myTank.paint(g);
        bullet.paint(g);
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
                default:
                    break;
            }
            setMainTankDir();
        }

        private void setMainTankDir() {
            if(!bL && !bR && !bU && !bD){
                myTank.setMoving(false);
            }else{
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
