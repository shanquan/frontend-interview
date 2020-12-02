package com.byd.template.base;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.StrictMode;

import com.byd.template.util.APIService;
import com.byd.template.util.mockService.RetrofitMock;
import com.byd.template.util.mockService.SimpleMockInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by yang.yu11 on 2017/10/30.
 */

public class TheApplication extends Application {
    private static TheApplication myApplication;
    private OkHttpClient okHttpClient;

    public static TheApplication getInstance() {
        return myApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //因为7.0之后直接用parseuri会报错
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        //初始化网络请求
        APIService.getInstance().init();
        RetrofitMock.init(this, "mock.json");

        //使其系统更改字体大小无效
        initTextSize();

    }


    /**
     * 使其系统更改字体大小无效
     */
    private void initTextSize() {
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    /**
     * 获取唯一的okhttp对象
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    // mock service
                    .addInterceptor(new SimpleMockInterceptor(true))
                    .build();
        }
        return okHttpClient;
    }

}
