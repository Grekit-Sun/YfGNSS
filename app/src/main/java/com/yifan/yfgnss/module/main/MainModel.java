package com.yifan.yfgnss.module.main;


import com.yifan.yfgnss.manager.RetrofitManager;
import com.yifan.yfgnss.module.main.api.MainService;

/**
 * @Author sun
 * @Date 2020-11-16
 * Description:
 */
class MainModel {

    private MainService mMainService;

    MainModel(){
        mMainService =  RetrofitManager.getInstance().getDefaultRetrofit().create(MainService.class);
    }
}
