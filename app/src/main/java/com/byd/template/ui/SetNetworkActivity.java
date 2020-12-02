package com.byd.template.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.byd.template.R;
import com.byd.template.base.BaseActivity;
import com.byd.template.constant.Constants;
import com.byd.template.util.BaseUtil;
import com.byd.template.databinding.ActivitySetNetworkBinding;
import com.byd.template.util.DebugUtil;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

//网络设置页面
public class SetNetworkActivity extends BaseActivity<ActivitySetNetworkBinding> implements EasyPermissions.PermissionCallbacks, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private String url;
    private String [] addrs = Constants.getServerUrls();
    private String[] servers = Constants.getServers();

    private boolean isOffice = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_network);

        showContentView();
        initData();
        initListener();
    }

    private void initData(){
        try{
            bindingView.etOfficeIp.setText(addrs[1]);
            bindingView.etPro.setText(addrs[2]);
            ArrayAdapter serverAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, servers);
            serverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bindingView.spServer.setAdapter(serverAdapter);
        }catch (Exception e){
            BaseUtil.showNoCancelDialog(this, "提示", "initData异常:" + e.toString());
        }
    }

    private void initListener() {
        bindingView.spServer.setOnItemSelectedListener(this);
        bindingView.rg.setOnCheckedChangeListener(this);
        bindingView.btnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindingView.rbOffice.setChecked(true);
                bindingView.etOfficeIp.setText(addrs[1]);
                bindingView.etPro.setText(addrs[2]);
            }
        });
        bindingView.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOffice) {
                    url = bindingView.etOfficeIp.getText().toString().trim();
                } else {
                    url = bindingView.etPro.getText().toString().trim();
                }
                if (TextUtils.isEmpty(url)) {
                    BaseUtil.showDialog(SetNetworkActivity.this, "提示", "URL不能为空", null);
                } else {
                    //保存到配置中,是否需要保存到sp
                    if (url.charAt(url.length() - 1) != '/') {
                        url = url + "/";
                    }
                    Constants.finalWms = url;
                    Constants.setUrl(isOffice);
                    Intent intent = new Intent(SetNetworkActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        bindingView.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//                Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
//                rt.play();
                BaseUtil.playSound(SetNetworkActivity.this, R.raw.okvoice);
            }
        });

        bindingView.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//                Ringtone rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
//                rt.play();
                BaseUtil.playSound(SetNetworkActivity.this, R.raw.wrongvoice);
            }
        });
    }

    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            DebugUtil.hasPermission = true;
        } else {
            EasyPermissions.requestPermissions(this,
                    "此应用需要开启某些权限才能正常使用",
                    0,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //处理权限名字字符串
        StringBuffer sb = new StringBuffer();
        for (String str : perms) {
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 1, sb.length(), "");

        String noPermission = sb.toString();
        StringBuffer finalNo = new StringBuffer();
        if (noPermission.contains("WRITE_EXTERNAL_STORAGE"))
            finalNo.append("存储;");
        if (noPermission.contains("CAMERA"))
            finalNo.append("相机;");
        finalNo.deleteCharAt(finalNo.length() - 1);

        switch (requestCode) {
            case 0:
                new AppSettingsDialog
                        .Builder(this)
                        .setRationale("此功能需要" + finalNo + "权限，否则无法正常使用，是否打开设置")
                        .setPositiveButton("好")
                        .setNegativeButton("不好", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SetNetworkActivity.this.finish();
                            }
                        })
                        .build()
                        .show();
                break;
        }
    }

    @AfterPermissionGranted(0)
    private void afterGranted(){
        DebugUtil.hasPermission = true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (bindingView.rbOffice.isChecked()) {
            bindingView.etOfficeIp.setEnabled(true);
            bindingView.etPro.setEnabled(false);
            isOffice = true;
        } else if (bindingView.rbPro.isChecked()) {
            bindingView.etPro.setEnabled(true);
            bindingView.etOfficeIp.setEnabled(false);
            isOffice = false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == bindingView.spServer) {
            Constants.idx = parent.getSelectedItemPosition();
            addrs = Constants.getServerUrls();
            bindingView.etOfficeIp.setText(addrs[1]);
            bindingView.etPro.setText(addrs[2]);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
