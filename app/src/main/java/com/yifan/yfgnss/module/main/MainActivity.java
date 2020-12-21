package com.yifan.yfgnss.module.main;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yanzhenjie.permission.Action;
import com.yifan.yfgnss.R;
import com.yifan.yfgnss.TestService;
import com.yifan.yfgnss.base.BaseActivity;
import com.yifan.yfgnss.helper.PermissionHelper;
import com.yifan.yfgnss.utils.CommonRxTaskUtil;
import com.yifan.yfgnss.utils.RxJavaUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter, IMainView> implements IMainView {

    @BindView(R.id.et_time)
    EditText mEtTime;
    @BindView(R.id.btn_start_test1)
    Button mBtnStartTest1;
    @BindView(R.id.tv_times)
    TextView mTvTimes;

    private TestService.GnssTestBinder mBinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initPermission();
    }

    @OnClick(R.id.btn_start_test1)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_test1:
                bindService(new Intent(MainActivity.this, TestService.class), connection, BIND_AUTO_CREATE);
                setCnt();
                break;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        //可交互的后台服务与普通服务的不同之处，就在于这个connection建立起了两者的联系
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (TestService.GnssTestBinder) service;
        }//onServiceConnected()方法关键，在这里实现对服务的方法的调用
    };

    private void setCnt(){
        RxJavaUtil.executeRxTask(new CommonRxTaskUtil<Integer>() {
            @Override
            public void doInIOThread() {
                setT(mBinder.getCount());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void doInUIThread() {
                mTvTimes.setText("" + getT());
            }
        });
    }


    private void initPermission() {
        PermissionHelper.requestMultiPermission(this, new Action() {
                    @Override
                    public void onAction(Object data) {
                    }
                }, new Action() {
                    @Override
                    public void onAction(Object data) {
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null) {
            unbindService(connection);
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Class<IMainView> getViewClass() {
        return IMainView.class;
    }

    @Override
    protected Class<MainPresenter> getPresenterClass() {
        return MainPresenter.class;
    }
}