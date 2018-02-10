package com.biubiu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author 张海彪
 * @create 2018-02-11 上午3:43
 */
@Data
public class DelayedElement<T> implements Delayed, Serializable {

    private static final long serialVersionUID = 7808422910473061058L;

    private long delay;

    private T t;

    public DelayedElement(long delay, T t) {
        this.delay = delay;
        this.t = t;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return 0;
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DelayedElement<?> that = (DelayedElement<?>) o;

        return t != null ? t.equals(that.t) : that.t == null;
    }

    @Override
    public int hashCode() {
        return t != null ? t.hashCode() : 0;
    }

}
