/*
 * Copyright (C) 2019 BYD, Inc. All Rights Reserved.
 */
package com.byd.template.util;


import java.io.IOException;

import com.byd.template.base.TheApplication;
import com.byd.template.constant.Constants;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 使用okhttp请求tokeh和调用服务
 */
public class HttpUtils {
    private Handler handler;
    private static volatile HttpUtils instance;

    private HttpUtils() {
    }

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public void init() {
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * post类请求
     * @param url
     * @param param
     * @param listener
     */
    public void post(String url, String param, final OnResultListener listener) {
        OkHttpClient client = TheApplication.getInstance().getOkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("param", param)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        // 请求添加统一的header或query
//        Request original = new Request.Builder()
//                .url(url)
//                //.addHeader("HeaderKeyA", "HeaderKeyA")// 添加统一header参数
//                .post(body)
//                .build();
//        //统一添加query参数
//        HttpUrl httpUrl = original.url()
//                .newBuilder()
//                .addQueryParameter("LOGIN_ID", ConfigurationBean.getInstance().getLoginID())
//                .build();
//        final Request request = original.newBuilder()
//                .url(httpUrl)
//                .build();
        StringBuilder sb = new StringBuilder();
        if (request.body() instanceof FormBody) {
            FormBody fbody = (FormBody) request.body();
            for (int i = 0; i < fbody.size(); i++) {
                sb.append(fbody.encodedName(i) + "=" + fbody.encodedValue(i) + ",");
            }
            sb.delete(sb.length() - 1, sb.length());
        }

        String method = request.url().queryParameter("method");
        DebugUtil.debug("POST",String.format("%s&%s",request.url(),sb.toString()));
        DebugUtil.debug("PARAM",param);
//        DebugUtil.debug("TEST",String.format("%s(){%n url=%s%n decode $url %n curl $url -d \"%s\"%n}",method,request.url().toString(),sb));
        request(client,request,listener);
    }

    /**
     * get类请求
     * @param url
     * @param listener
     */
    public void get(String url, final OnResultListener listener) {
        OkHttpClient client = TheApplication.getInstance().getOkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();

        String method = request.url().queryParameter("method");
        DebugUtil.debug("GET",request.url().toString());
//        DebugUtil.debug("TEST",String.format("%s(){%n url=%s%n decode $url %n curl $url}",method,request.url().toString()));

        request(client,request,listener);
    }

    /**
     * 请求统一发起与处理
     * @param client
     * @param request
     * @param listener
     */
    private void request(OkHttpClient client,Request request,final OnResultListener listener){
        if (client == null) {
            listener.onError(1,"client为null");
            return;
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                final String error;
                if (e.toString().contains("ConnectException")) {
                    error = "服务器请求超时";
                } else if (e.toString().contains("SocketTimeoutException")) {
                    error = "服务器响应超时";
                }else{
                    error = e.toString();
                }
                DebugUtil.debug("ERROR",String.format("{code:2,msg:$s}",error));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(2,error);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if(response == null || response.body() == null || response.code()!= 200 || TextUtils.isEmpty(response.toString())){
                    final String msg;
                    if(response.code()!=200){
                        msg = response.request().url()+"接口异常, httpCode:"+response.code();
                    }else{
                        msg = response.request().url()+"接口返回为空";
                    }
                    DebugUtil.debug("ERROR","{code:3,msg:"+msg+"}");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError(3,msg);
                        }
                    });
                    return;
                }
                final String responseString = response.body().string();
                DebugUtil.debug("RESULT",responseString);
                if(response.request().url().toString().indexOf(Constants.finalWms)!=-1){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResult(responseString);
                        }
                    });
                }else{
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        String result = jsonObject.getString("RESULT");
                        if ("PASS".equals(result)) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onResult(responseString);
                                }
                            });
                        } else {
                            final String msg = jsonObject.getString("MESSAGE");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onError(3,msg);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onError(3,response.request().url()+"接口格式异常:没有RESULT字段");
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 释放资源
     */
    public void release() {
        handler = null;
    }
}
