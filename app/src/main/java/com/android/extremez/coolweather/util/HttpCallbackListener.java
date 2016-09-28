package com.android.extremez.coolweather.util;

/**
 * Created by tingzong on 2016/9/28.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
