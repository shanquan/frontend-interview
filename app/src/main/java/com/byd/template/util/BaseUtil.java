package com.byd.template.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import com.byd.template.base.TheApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;

/**
 * Created by wwl on 2020/01/10.
 */

public class BaseUtil {

    private static Toast mToast;
    private static ProgressDialog dialog;
    /**
     * 获取现有版本号
     *
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionName;
    }

    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(TheApplication.getInstance(), text, Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();
    }

    public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveClick) {

        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", positiveClick)
                .setNegativeButton("取消", null)
                .setCancelable(false)
                .show();
    }

    public static void showHaveCDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveClick, DialogInterface.OnClickListener negativeClick) {

        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", positiveClick)
                .setNegativeButton("取消", negativeClick)
                .setCancelable(false)
                .show();
    }

    public static void showNoCancelDialog(Context context, String title, String message) {

        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .setCancelable(false)
                .show();
    }

    /**
     * 显示加载进度条
     * @param context
     * @param message
     * @return
     */
    public static void showProgressDialog(Context context,String message) {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
            dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
            dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
            dialog.setTitle("提示");
        }
        dialog.setMessage(message);
        dialog.show();
    }

    public static void cancelProgress() {
        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }


    /**
     * 从服务端下载新版本
     *
     * @param path
     * @param pd
     * @param fileSize
     * @return
     */
    public static File downFileFromServer(String path, ProgressDialog pd, String fileSize) throws Exception {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setConnectTimeout(15000);
            conn.connect();
            //获取文件的大小
            int length = Integer.parseInt(fileSize);
            pd.setMax(length);
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "D9OneFacWms.apk");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024 * 1000];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            pd.dismiss();
            return null;
        }
    }

    /**
     * 重新获取焦点
     *
     * @param et
     */
    public static void BackToTheEdit(final EditText et) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et.requestFocus();
//                et.setSelection(et.getText().length(), et.getText().length());
                et.selectAll();
//                et.setText("");
            }
        }, 300);
    }

    public static void BackToClearEdit(final EditText et) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et.requestFocus();
//                et.setSelection(et.getText().length(), et.getText().length());
                et.setText("");
            }
        }, 300);
    }

    /**
     * 拼接接口请求参数
     *
     * @param names
     * @param values
     * @return
     */
    public static String getParam(String[] names, String[] values) {
        String param = null;
        Map<String, String> map = new TreeMap<>();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        param = new JSONObject(map).toString();
        return param;
    }

    public static String getJsonParam(JSONObject jsonObject, String key) throws JSONException {
        String value = "";
        if (jsonObject.has(key)) {
            value = jsonObject.getString(key);
            if ("null".equals(value))
                value = "";
        }
        return value;
    }

    public static int getIntJsonParam(JSONObject jsonObject, String key) throws JSONException {
        int value = 0;
        if (jsonObject.has(key)) {
            try {
                value = jsonObject.getInt(key);
            } catch (Exception e) {
                return value;
            }
        }
        return value;
    }

    public static JSONObject getJsonObject(JSONObject json, String key) throws JSONException {
        JSONObject jsonObject = null;
        if (json.has(key)) {
            try {
                jsonObject = json.getJSONObject(key);
            } catch (Exception e) {
                return jsonObject;
            }
        }
        return jsonObject;
    }

    public static JSONArray getJsonArray(JSONObject json, String key) throws JSONException {
        JSONArray array = null;
        if (json.has(key)) {
            try {
                array = json.getJSONArray(key);
            } catch (Exception e) {
                return array;
            }
        }
        return array;
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 适合播放声音短，文件小
     * 可以同时播放多种音频
     * 消耗资源较小
     */
    public static void playSound(Context context, final int rawId) {
        SoundPool soundPool;
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频的数量
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的类
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        }
        //第一个参数Context,第二个参数资源Id，第三个参数优先级
        soundPool.load(context, rawId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(final SoundPool soundPool, int sampleId, int status) {
                soundPool.play(1, 1, 1, 0, 0, 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        soundPool.release();
                    }
                }, 1000);

            }
        });
        //第一个参数id，即传入池中的顺序，第二个和第三个参数为左右声道，第四个参数为优先级，第五个是否循环播放，0不循环，-1循环
        //最后一个参数播放比率，范围0.5到2，通常为1表示正常播放
//        soundPool.play(1, 1, 1, 0, 0, 1);
        //回收Pool中的资源
        //soundPool.release();
    }
}
