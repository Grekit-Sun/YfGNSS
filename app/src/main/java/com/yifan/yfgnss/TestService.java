package com.yifan.yfgnss;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Description:
 * @Author: ZhengXiang Sun
 * @Data: 2020-12-18
 */
public class TestService extends Service {

    private LocationManager mLocationManager;
    private int status;
    private static final String TAG = "TestService";
    private int mCnt;
    private GnssTestBinder mGnssTestBinder;
    //开启GPS
    public static final String GPS_ON = "settings put secure location_providers_allowed +gps";

    //关闭GPS
    public static final String GPS_OFF = "settings put secure location_providers_allowed -gps";

    //查看GPS状态
    public static final String QUERY_GPS_STATE = "settings get secure location_providers_allowed";

    private Handler mHandler = new Handler(new Handler.Callback() {
        @SuppressLint("MissingPermission")
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 1:
                    /* 检测GPS定位模块是否开启 */
                    if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        /* 针对GPS定位模块是否开启，具体接下来做的事 */
                        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        updateLocationMessage(location);
                        /* 监听GPS的状态变化 */
                        mLocationManager.addGpsStatusListener(listener);
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
                        Log.d(TAG, "open GNSS ");
                    }
                    break;
                case 2:
                    mLocationManager.removeUpdates(myLocationListener);
                    Log.d(TAG, "remove GNSS ");
                    mCnt++;
                    break;
            }
            return false;
        }
    });

    /* 监听GPS的状态变化 */
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                /* 第一次获取到定位信息 */
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    break;
                /* 卫星状态发生变化，捕获到卫星/卫星不可见 */
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    break;
            }
        }

        ;
    };

    @Override
    public void onCreate() {
        super.onCreate();
        testLoadGnss();
         mGnssTestBinder = new GnssTestBinder();
    }


    @SuppressLint("MissingPermission")
    private void testLoadGnss() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (status != 2) {
                        mHandler.sendEmptyMessage(1);
                        status = 2;
                    } else {
                        mHandler.sendEmptyMessage(2);
                        status = 1;
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

    private void updateLocationMessage(Location location) {

    }

    /**
     * 获取GPS位置监听器，包含四个不同触发方式
     */
    LocationListener myLocationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // 当位置获取（GPS）打开时调用此方法
        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "执行，GPS的打开");
        }

        // 当位置获取（GPS）关闭时调用此方法
        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "执行，GPS关闭");
        }

        // 当坐标改变时触发此方法，如果获取到相同坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
//                latitude = location.getLatitude(); // 经度
//                longitude = location.getLongitude(); // 纬度
                Log.d(TAG, "执行，坐标发生改变 - >" + "Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
            }

        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mGnssTestBinder;
    }

    public class GnssTestBinder extends Binder {
        public int getCount() {
            return mCnt;
        }
    }
}
