package com.rookied;

import java.io.Serializable;

/**
 * @author zhangqiang
 * @date 2021/4/26
 */
public interface FireStrategy<T> extends Serializable {
    void fire(T t);

}
