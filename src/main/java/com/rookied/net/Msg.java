package com.rookied.net;

/**
 * @author zhangqiang
 * @date 2021/5/8
 */
public abstract class Msg {
    public abstract void handle();
    public abstract byte[] toBytes();
    public abstract void parse(byte[] bytes);
    public abstract MsgType getMsgType();
}
