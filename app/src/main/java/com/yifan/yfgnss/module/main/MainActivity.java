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
    private TextView mTvTimes;
    private EditText mEtTimes;
    private Button mBtnStart1;
    private TestService.GnssTestBinder mBinder;

    private int mCnt;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    setCnt(mCnt);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initPermission();

        mBtnStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @OnClick(R.id.btn_start_test1)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_start_test1:
                bindService(new Intent(MainActivity.this, TestService.class), connection, BIND_AUTO_CREATE);
                mPresenter.
                break;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mBinder != null) {
                        mCnt = mBinder.getCount();
                        mHandler.sendEmptyMessage(0x01);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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

    private void setCnt(int cnt) {
        mTvTimes.setText("" + cnt);
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