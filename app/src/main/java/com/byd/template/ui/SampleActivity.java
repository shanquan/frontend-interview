package com.byd.template.ui;

import android.os.Bundle;

import com.byd.template.R;
import com.byd.template.base.BaseActivity;
import com.byd.template.databinding.ActivitySampleBinding;
import com.byd.template.util.BaseUtil;

//示例页面
public class SampleActivity extends BaseActivity<ActivitySampleBinding>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        initData();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showContentView();

    }

    private void showErrorMsg(String msg) {
        BaseUtil.showNoCancelDialog(this, "提示", msg);
    }

    protected boolean hasToolBar() {
        return false;
    }

    private void initListener() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("initListener异常:" + e.toString());
        }
    }

    private void initData() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("initData异常:" + e.toString());
        }
    }

}
