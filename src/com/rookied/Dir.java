package com.rookied;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 坦克和子弹的方向
 *
 * @author rookied
 */

public enum Dir {
    //左
    LEFT,
    //上
    UP,
    //右
    RIGHT,
    //下
    DOWN;

    private static final List<Dir> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();

    /**
     * 随机返回一个方向
     */
    public static Dir randomDir(){
        //return VALUES.stream().filter(x->x!=dir).findAny().get();
        return VALUES.get(Tank.RANDOM.nextInt(SIZE));
    }
}
