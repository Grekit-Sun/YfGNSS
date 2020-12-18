package com.yifan.yfgnss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.yanzhenjie.permission.Action;
import com.yifan.yfgnss.helper.PermissionHelper;

public class MainActivity extends AppCompatActivity {

    private TextView mTvTimes;
    private EditText mEtTimes;
    private Button mBtnStart1;
    private TestService.GnssTestBinder mBinder;

    private int mCnt;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    setCnt(mCnt);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        mTvTimes = (TextView) findViewById(R.id.tv_times);
        mEtTimes = (EditText) findViewById(R.id.et_time);
        mBtnStart1 = (Button) findViewById(R.id.btn_start_test1);

        mBtnStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService(new Intent(MainActivity.this, TestService.class), connection, BIND_AUTO_CREATE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            if(mBinder != null){
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
        });
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
}