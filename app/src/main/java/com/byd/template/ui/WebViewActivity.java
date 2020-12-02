package com.byd.template.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.byd.template.R;
import com.byd.template.base.BaseActivity;
import com.byd.template.util.BaseUtil;
import com.byd.template.views.MywebChromeClient;
import com.byd.template.databinding.ActivityWebviewBinding;

//webview
public class WebViewActivity extends BaseActivity<ActivityWebviewBinding> implements View.OnClickListener {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        bindingView.btnRefresh.setOnClickListener(this);
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showContentView();

    }

    private void initData() {
        try {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString("TITLE");
            String url = bundle.getString("URL");
            bindingView.tvTitle.setText(title);
            webView = new WebView(this);
            WebSettings settings = webView.getSettings();
            settings.setDomStorageEnabled(true);
            //解决一些图片加载问题
            settings.setJavaScriptEnabled(true);
            settings.setBlockNetworkImage(false);
            settings.setDefaultTextEncodingName("utf-8");
            //允许XMLHttpRequest来读取本地档案内容
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                settings.setAllowFileAccessFromFileURLs(true);
            }
            //允许页面缩放
//            settings.setSupportZoom(true);
//            settings.setUseWideViewPort(true);
//            settings.setBuiltInZoomControls(true);
            //页面缓存
            settings.setDomStorageEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            //不支持alert弹窗 webView.setWebViewClient(new WebViewClient());
            //支持弹窗显示 webView.setWebChromeClient(new WebChromeClient());
            // 自定义弹窗，修改“来自http://**的提示”
            webView.setWebChromeClient(new MywebChromeClient(WebViewActivity.this));
            // 设置链接跳转(webview内跳转)
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
//                    DebugUtil.debug("loadUrl:"+url);
                    return false;
                }
            });
            bindingView.webFrame.addView(webView);
            webView.loadUrl(url);
        } catch (Exception e) {
            showErrorMsg("initData异常:" + e.toString());
        }
    }

    //监听BACK按键，有可以返回的页面时返回页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.setTag(null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    private void showErrorMsg(String msg) {
        BaseUtil.showNoCancelDialog(this, "提示", msg);
    }

    protected boolean hasToolBar() {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == bindingView.btnRefresh) {
            //刷新页面
            if(webView != null)
            webView.reload();
        }
    }

}
