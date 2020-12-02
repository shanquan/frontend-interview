package com.byd.template.ui;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.byd.template.R;
import com.byd.template.base.BaseActivity;
import com.byd.template.constant.Constants;
import com.byd.template.util.APIService;
import com.byd.template.util.BaseUtil;
import com.byd.template.databinding.ActivityLoginBinding;
import com.byd.template.util.DebugUtil;
import com.byd.template.util.OnResultListener;

import org.json.JSONException;
import org.json.JSONObject;

//登录页面
public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private String user, password,fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initData();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindingView.tvMsg.setText("");
        bindingView.etUser.setText("");
        bindingView.etPwd.setText("");
        showContentView();
    }

    protected boolean hasToolBar() {
        return false;
    }

    private void initListener() {
        try {
            bindingView.btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = bindingView.etUser.getText().toString().trim();
                    password = bindingView.etPwd.getText().toString().trim();
                    if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password)) {
                        BaseUtil.showDialog(LoginActivity.this, "提示", "用户名或密码不能为空", null);
                    }else{
                        BaseUtil.showProgressDialog(LoginActivity.this,"正在登录中");
                        APIService.getInstance().login(new OnResultListener() {
                            @Override
                            public void onResult(String result) {
                                BaseUtil.cancelProgress();
                                bindingView.tvMsg.setText("");
                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(int code, String msg) {
                                BaseUtil.cancelProgress();
                                bindingView.tvMsg.setText(msg);
                            }
                        },user,password);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("initListener异常:" + e.toString());
        }
    }

    private void showErrorMsg(String msg) {
        BaseUtil.showNoCancelDialog(this, "提示", msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        try {
            //获取PDA的ip
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            Constants.clientIp = (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF) + "." + (ipAddress >> 24 & 0xFF);
            // 获取调试开关
            APIService.getInstance().GetDebug(new OnResultListener() {
                @Override
                public void onResult(String result) {
                    try{
                        JSONObject jsonObject = new JSONObject(result);
                        DebugUtil.DEBUG = jsonObject.getInt("DEBUG");
                    }catch (JSONException e){
                        DebugUtil.DEBUG = 0;
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(int code, String msg) {
                    DebugUtil.DEBUG = 0;
                }
            });
            //检查更新
            APIService.getInstance().GetUpdate(new OnResultListener() {
                @Override
                public void onResult(String result) {
                    //@todo
                    //parseVersion(result);
                }

                @Override
                public void onError(int code, String msg) {
                    BaseUtil.showNoCancelDialog(LoginActivity.this, "报错", msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("initData异常:" + e.toString());
        }
    }

}
