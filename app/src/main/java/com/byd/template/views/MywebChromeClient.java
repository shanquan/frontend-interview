package com.byd.template.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.byd.template.R;
import com.byd.template.util.DebugUtil;

public class MywebChromeClient extends WebChromeClient {

    private Context context;
    public MywebChromeClient(Context context){this.context = context;}

    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             final JsResult result) {
        // 弹窗处理
        AlertDialog.Builder b2 = new AlertDialog.Builder(context)
                .setTitle(R.string.app_name).setMessage(message)
                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });

        b2.setCancelable(false);
        b2.create();
        b2.show();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                             final JsResult result) {
        // 弹窗处理
        AlertDialog.Builder b2 = new AlertDialog.Builder(context)
                .setTitle(R.string.app_name).setMessage(message)
                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                })
                .setNegativeButton("取消",new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });

        b2.create();
        b2.show();
        return true;
    }

    // 打印js控制台消息
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        DebugUtil.debug("onConsoleMessage【"+consoleMessage.messageLevel()+"】" + "\nmessage=" + consoleMessage.message()
                +  "\nsourceId=" + consoleMessage.sourceId()
                + ",lineNumber=" + consoleMessage.lineNumber());
        return super.onConsoleMessage(consoleMessage);
    }
}
