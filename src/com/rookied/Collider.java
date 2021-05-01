package com.rookied;

import java.io.Serializable;

/**
 * @author zhangqiang
 * @date 2021/4/27
 */
public interface Collider extends Serializable {
    boolean collide(GameObject o1,GameObject o2);
}
