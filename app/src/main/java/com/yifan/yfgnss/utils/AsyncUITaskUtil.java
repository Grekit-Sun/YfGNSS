package com.yifan.yfgnss.utils;

/**
 * @Author sun
 * @Date 2020-12-20
 * Description: 在主线程中执行的任务
 */
public abstract class AsyncUITaskUtil<T> {

    private T t;

    public AsyncUITaskUtil(){}

    public AsyncUITaskUtil(T t){
        setT(t);
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public abstract void doInUIThread();
}
