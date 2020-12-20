package com.yifan.yfgnss.utils;

/**
 * @Author sun
 * @Date 2020-12-20
 * Description: 在IO线程中执行的任务
 */
public abstract class AsynIOTaskUtil<T> {

    private T t;

    public AsynIOTaskUtil(T t) {
        setT(t);
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public abstract void doInIOThread();
}
