package com.rookied.net;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author zhangqiang
 * @date 2021/5/6
 */
public class ServerFrame extends Frame {
    public static final ServerFrame INSTANCE = new ServerFrame();

    Button btnStart = new Button("start");
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();
    Server server = new Server();

    public ServerFrame() {
        this.setSize(600, 300);
        this.setLocation(1100, 0);
        this.setAlwaysOnTop(true);
        this.add(btnStart, BorderLayout.NORTH);
        Panel p = new Panel(new GridLayout(1, 2));
        p.add(taLeft);
        p.add(taRight);
        this.add(p);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        ServerFrame.INSTANCE.setVisible(true);
        ServerFrame.INSTANCE.server.start();
    }

    public void updateServerMsg(String msg) {
        this.taLeft.setText(taLeft.getText() + System.getProperty("line.separator") + msg);
    }

    public void updateClientMsg(String msg) {
        this.taRight.setText(taRight.getText() + System.getProperty("line.separator") + msg);
    }
}
