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
    static final int GAME_WIDTH = 1080, GAME_HEIGHT = 960;

    public TankFrame() {
        setSize(GAME_WIDTH, GAME_HEIGHT);
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
        GameModel.getInstance().paint(g);
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
                    GameModel.getInstance().handFireKey();
                    break;
                default:
                    break;
            }
            setMainTankDir();
        }

        private void setMainTankDir() {
            Tank myTank = GameModel.getInstance().getMainTank();
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
