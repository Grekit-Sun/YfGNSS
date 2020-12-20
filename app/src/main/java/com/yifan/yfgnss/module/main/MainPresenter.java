package com.yifan.yfgnss.module.main;

import android.app.DownloadManager;

import com.yifan.yfgnss.base.IPresenter;


/**
 * @Author sun
 * @Date 2020-11-16
 * Description:
 */
public class MainPresenter implements IPresenter {

    private IMainView mView;
    private MainModel mModel;
    private DownloadManager mDownloadManager;
    private long mReference;

    public MainPresenter(IMainView view) {
        mView = view;
        mModel = new MainModel();
    }

}
