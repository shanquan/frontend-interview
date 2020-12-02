/*
 * Copyright (C) 2019 BYD, Inc. All Rights Reserved.
 */
package com.byd.template.util;

import android.os.Environment;
import android.util.Log;

import com.byd.template.constant.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//在代码中要打印log,就直接DebugUtil.debug(....).然后如果发布的时候,就直接把这个类的DEBUG 改成0,这样所有的log就不会再打印在控制台.
public class DebugUtil {
    public static final String TAG = "wwl";
    //后面可通过调接口开关日志，0: 不处理 1:logcat 2:logcat+file 3:logcat+websocket
    public static int DEBUG = 0;
    public static String versionName = "";
    public static boolean hasPermission = false;
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void debug(String tag, String msg) {
        if (DEBUG>0) {
            if(tag.equals("TEST")||tag.equals("GET")||tag.equals("POST")||tag.equals("PARAM")||tag.equals("RESULT")||tag.equals("ERROR")){
                Log.d(TAG+"_"+tag, msg);
                if(hasPermission&& DEBUG == 2){
                    writeToFile(tag,msg);
                }else if(DEBUG == 3){
                    uploadLog(tag,msg);
                }
            }else{
                Log.d(tag, msg);
            }
        }
    }

    public static void debug(String msg) {
        if (DEBUG>0) {
            Log.d(TAG, msg);
        }
    }

    public static void error(String tag, String error) {
        if (DEBUG>0) {
            Log.e(tag, error);
        }
    }

    public static void error(String error) {
        if (DEBUG>0) {
            Log.e(TAG, error);
        }
    }

    /**
     * 将log数据写入本地文件/sdcard/pdadebug/info-yyyy-mm-dd.log，需要手动清理
     * filename-idx 1.0 yyyy-mm-dd-hh-mm-ss-millis
     * GET(请求方法):URL(地址)
     * PARAM:(参数)
     * TEST:测试函数(method(){...})
     * RESULT:(返回)
     * ERROR: (错误)
     */
    public static void writeToFile(String tag,String msg){
        StringBuffer sb = new StringBuffer();
        BufferedWriter bw = null;
        try {
            String time = formatter.format(new Date());
            sb.append(Constants.filename+"-"+Constants.idx);
            sb.append(" ");
            sb.append(versionName);
            sb.append(" ");
            sb.append(time);
            sb.append(" ");
            sb.append(tag);
            sb.append(" : ");
            sb.append(msg);
            sb.append("\n");
            String fileName = "debug-" + time.substring(0,10) +".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/pdadebug/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName,true);
                bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.write(sb.toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }finally {
            try {
                if (bw != null) {
                    bw.close();//关闭缓冲流，关闭前自动flush()
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传接口调用日志，使用websocket?
     * @param tag
     * @param msg
     * @todo
     */
    public static void uploadLog(String tag,String msg){
    }
}