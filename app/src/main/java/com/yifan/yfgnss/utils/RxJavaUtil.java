package com.yifan.yfgnss.utils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author sun
 * @Date 2020-12-21
 * Description:
 */
public class RxJavaUtil {

    /**
     * 在ui线程中工作
     *
     * @param uiTask
     * @param <T>
     */
    public static <T> void doInUIThread(AsyncUITaskUtil uiTask) {
        Observable.just(uiTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AsyncUITaskUtil>() {
                    @Override
                    public void accept(AsyncUITaskUtil asyncUITaskUtil) throws Exception {
                        asyncUITaskUtil.doInUIThread();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
//                .subscribe(new Action<AsyncUITaskUtil<T>>() {
//                },new Action<Throwable>());
//        https://blog.csdn.net/z2wenfa/article/details/51276626
//        https://www.jianshu.com/p/c7a995f3763c
//        https://www.cnblogs.com/liguanyin/p/7795737.html
    }

    /**
     * 在IO线程中执行任务
     *
     * @param ioTask
     * @param <T>
     */
    public static <T> void doInIOThread(AsynIOTaskUtil ioTask) {
        Observable.just(ioTask)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<AsynIOTaskUtil>() {
                    @Override
                    public void accept(AsynIOTaskUtil asynIOTaskUtil) throws Exception {
                        asynIOTaskUtil.doInIOThread();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 执行Rx通用任务 (IO线程中执行耗时操作 执行完成调用UI线程中的方法)
     *
     * @param t
     * @param <T>
     */
    public static <T> void executeRxTask(CommonRxTaskUtil<T> t) {
        CommonRxTaskUtil.MyOnSubscribe onsubscribe = new CommonRxTaskUtil.MyOnSubscribe(t) {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {

                getC().doInIOThread();
                emitter.onNext(getC());
                emitter.onComplete();
            }
        };

        Observable.create(onsubscribe)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommonRxTaskUtil<T>>() {
                    @Override
                    public void accept(CommonRxTaskUtil<T> t) throws Exception {
                        t.doInUIThread();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

    }
}
