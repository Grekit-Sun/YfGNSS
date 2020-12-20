package com.yifan.yfgnss.utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

/**
 * @Author sun
 * @Date 2020-12-21
 * Description:
 */
public class RxJavaUtil {

    public static <T> void doInUIThread(AsyncUITaskUtil uiTask) {
        Observable.just(uiTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action<AsyncUITaskUtil<T>>())
//                .subscribe(new Action<AsyncUITaskUtil<T>>() {
//                },new Action<Throwable>());
    }
}
