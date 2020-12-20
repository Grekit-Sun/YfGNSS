package com.yifan.yfgnss.utils;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * @Author sun
 * @Date 2020-12-20
 * Description:
 */
public abstract class CommonRxTaskUtil<T> {

    private T t;

    public CommonRxTaskUtil(){}

    public CommonRxTaskUtil(T t) {
        setT(t);
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public abstract void doInIOThread();

    public abstract void doInUIThread();

    public abstract class MyOnSubscribe<C> implements ObservableOnSubscribe<C>{

        private C c;

        public MyOnSubscribe(C c){
            setC(c);
        }

        public C getC() {
            return c;
        }

        public void setC(C c) {
            this.c = c;
        }

    }
}
