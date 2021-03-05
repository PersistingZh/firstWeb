package com.pq.location;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.alibaba.fastjson.JSONObject;
import com.cmmap.api.location.CmccLocation;
import com.cmmap.api.location.CmccLocationClient;
import com.cmmap.api.location.CmccLocationClientOption;
import com.cmmap.api.location.CmccLocationListener;
import com.cmmap.api.location.CoordinateConverter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Location extends WXModule {
    private Handler handler = new Handler();

    private static final String TAG = "Location";

    // 线程参数
    private boolean isGetLocation = false;
    private int timerCount = 6; // 定时器总共运行次数（要进行一次-1）

    // 定位配置
    private Integer locationMode = 2; // 定位精度模式
    private Boolean openGps = false; // 是否开gps
    private long timeOut = 3000; // 超时

    private JSCallback callback111 = null;
    //声明CmccLocationClient类对象
    public CmccLocationClient mLocationClient = null;
    //声明CmccLocationClientOption对象
    private CmccLocationClientOption mLocationOptions = new CmccLocationClientOption();
    //声明定位回调监听器
    public CmccLocationListener locationListener = new CmccLocationListener() {
        @Override
        public void onLocationChanged(CmccLocation cmccLocation) {
            if (cmccLocation != null) {
//                pushEvent(cmccLocation);
                // 声明返回的data数据
                JSONObject data = new JSONObject();
                String locationModes = "";
                switch (locationMode) {
                    case 0:
                        locationModes = "Battery_Saving";
                        break;
                    case 1:
                        locationModes = "Device_Sensors";
                        break;
                    case 2:
                        locationModes = "Hight_Accuracy";
                        break;
                    default:
                        locationModes = "Hight_Accuracy";
                        break;
                }
                if (cmccLocation.getErrorCode() == 0) {
                    // 设置  为true表示获取到了定位信息，停止定时器
                    isGetLocation = true;
                    //可在其中解析cmccLocation获取相应内容。
                    if (callback111 != null) {
                        data.put("massage", "定位成功");
                        data.put("code", 0);
                        data.put("locationMode", locationModes);
                        data.put("isOpenGps", openGps);
                        data.put("data", cmccLocation);
                        callback111.invoke(data);
                    }
                } else {
                    // 获取地理位置失败，设置isGetLocation为false，让定时器调低精度去获取
                    isGetLocation = false;
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                    if (callback111 != null) {
                        data.put("massage", "定位失败");
                        data.put("code", cmccLocation.getErrorCode());
                        data.put("locationMode", locationModes);
                        data.put("isOpenGps", openGps);
                        data.put("data", cmccLocation);
                        callback111.invoke(data);
                    }
                }
            }
        }
    };


    //run ui thread
    @JSMethod(uiThread = false)
    public void getLocation(JSONObject options, JSCallback callback) {
        Log.i(TAG, "getLocation: " + options);
        // 设置定位配置信息
        locationMode = options.getInteger("locationMode");
        openGps = options.getBoolean("openGps");
        timeOut = options.getLong("timeOut");
        // 设置回掉方法
        this.callback111 = callback;
        mLocationClient = new CmccLocationClient(mWXSDKInstance.getContext());
        mLocationClient.setLocationListener(locationListener);


        // 定时器
        isGetLocation = false;
        new MyThread().start();
    }

    // 定位配置信息
    private CmccLocationClientOption getDefaultOption() {
        CmccLocationClientOption.CmccLocationMode locationModes = null;
        switch (locationMode) {
            case 0:
                locationModes = CmccLocationClientOption.CmccLocationMode.Battery_Saving;
                break;
            case 1:
                locationModes = CmccLocationClientOption.CmccLocationMode.Device_Sensors;
                break;
            case 2:
                locationModes = CmccLocationClientOption.CmccLocationMode.Hight_Accuracy;
                break;
            default:
                locationModes = CmccLocationClientOption.CmccLocationMode.Hight_Accuracy;
                break;
        }

        CmccLocationClientOption mOption = new CmccLocationClientOption();
//        mOption.setGpsFirst(true);
//        mOption.setHttpTimeOut(3000);
//        mOption.setInterval(2000);
//        mOption.setKillProcess(false);
//        mOption.setLocationCacheEnable(true);
//        mOption.setLocationMode(CmccLocationClientOption.CmccLocationMode.Hight_Accuracy);
//        mOption.setLocationProtocol(CmccLocationClientOption.CmccLocationProtocol.HTTP.HTTP);
//        mOption.setLocationPurpose(null);
//        mOption.setMockEnable(false);
//        mOption.setNeedAddress(true);
//        mOption.setOnceLocation(false);
//        mOption.setOnceLocationLatest(false);
//        mOption.setWifiScan(true);

        mOption.setLocationMode(locationModes);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(openGps);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(timeOut);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
//        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
//        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
//        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
//        mOption.setLocationProtocol(CmccLocationClientOption.CmccLocationProtocol.HTTP.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
//        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
//        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        mOption.setMockEnable(false);
        mOption.enableLog(false);
        mOption.convert(CoordinateConverter.CoordType.GOOGLE);
        return mOption;
    }

    // 主动向前端页面发送消息
    public void pushEvent(CmccLocation cmccLocation) {
        Map<String, Object> params = new HashMap<>();
        params.put("data", cmccLocation);
        mWXSDKInstance.fireGlobalEventCallback("myEvent", params);
    }

    // 定时器
    class MyThread extends Thread {
        private Timer timer = new Timer(); // 定时器
        private int count = 0; //从0开始计数，每运行一次timertask次数加一，运行制定次数后结束。

        @Override
        public void run() {

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (mLocationClient != null) {
                        Log.i(TAG, "run: 停止上一次的定位");
                        mLocationClient.stopLocation();

                    }
                    if ((count < timerCount) && !isGetLocation) {
                        // 停止定位，重新开始
                        Log.d(TAG, "第" + count + "次" + "设置定位信息");
                        switch (count) {
                            case 0:
                                locationMode = 2;
                                openGps = true;
                                break;
                            case 1:
                                locationMode = 2;
                                openGps = false;
                                break;
                            case 2:
                                locationMode = 1;
                                openGps = true;
                                break;
                            case 3:
                                locationMode = 1;
                                openGps = false;
                                break;
                            case 4:
                                locationMode = 0;
                                openGps = true;
                                break;
                            case 5:
                                locationMode = 0;
                                openGps = false;
                                break;
                            default:
                                locationMode = 2;
                                openGps = true;
                                break;
                        }
                        //初始化定位
                        mLocationOptions = getDefaultOption();
                        mLocationClient.setLocationOption(mLocationOptions);
                        Log.i(TAG, "run: mLocationOptions" +"是否开启gps"+mLocationOptions.isGpsFirst()+"定位模式"+mLocationOptions.getLocationMode());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mLocationClient.startLocation();
                                Log.d(TAG, "开始定位");
                            }
                        });


                    } else {
                        timer.cancel();
                        Log.i(TAG, "run:  获取到定位结果，结束定位");
                    }
                    count++;
                }
            };
            timer.schedule(task, 0, 2000);//每隔1000毫秒即一秒运行一次该程序
        }
    }

}
