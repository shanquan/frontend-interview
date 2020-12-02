package com.byd.template.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.byd.template.R;
import com.byd.template.base.BaseActivity;
import com.byd.template.constant.Constants;
import com.byd.template.util.BaseUtil;
import com.byd.template.databinding.ActivityMenuBinding;

//菜单页面
public class MenuActivity extends BaseActivity<ActivityMenuBinding> implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
            bindingView.btnPage.setOnClickListener(this);
            bindingView.btnSample.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("initListener异常:" + e.toString());
        }
    }

    private void initData() {
        try {
            //获取账号权限
            bindingView.tvTitle.setText(Constants.getServerUrls()[0]+"菜单");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("initData异常:" + e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if(v == bindingView.btnPage){
                Bundle bundle = new Bundle();
                bundle.putString("TITLE", bindingView.btnPage.getText().toString());
                bundle.putString("URL", "file:///android_asset/www/index.html");
                Intent intent = new Intent(this,WebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent,bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("onClick异常:" + e.toString());
        }
    }

}
