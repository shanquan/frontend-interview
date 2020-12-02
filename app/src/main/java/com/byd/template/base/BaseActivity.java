package com.byd.template.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byd.template.R;
import com.byd.template.databinding.ActivityBaseBinding;
import com.byd.template.listener.PerfectClickListener;

/**
 * Created by yang.yu11 on 2017/12/6.
 */

public class BaseActivity<SV extends ViewDataBinding> extends AppCompatActivity {

    //布局
    protected SV bindingView;

    private LinearLayout llProgressbar;
    private View refresh;//加载失败，重新加载
    private TextView tvError;//加载失败 或者加载数据为空的提示
    private ActivityBaseBinding mBaseBinding;
    private Animation mRotate;
    private LinearInterpolator interpolator;

    private ImageView mImg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    protected void setMyTextWatch(final EditText et) {
//        et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                et.setSelection(et.getText().length());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }


    //获取view
    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        bindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);

        //content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer = (RelativeLayout) mBaseBinding.getRoot().findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        //设置透明状态栏
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 38);

        llProgressbar = getView(R.id.ll_progress_bar);
        refresh = getView(R.id.ll_error_refresh);
        tvError = getView(R.id.tv_error);
        mImg = getView(R.id.img_progress);

        //加载动画
        mRotate = AnimationUtils.loadAnimation(this, R.anim.common_progress_cirle);
        //设置匀速旋转，在xml文件中设置会出现卡顿
        interpolator = new LinearInterpolator();
        mRotate.setInterpolator(interpolator);
        //默认进入页面就开启动画
        if (mImg != null && mRotate != null) {
            mImg.startAnimation(mRotate);
        }

        //设置toolbar
        if (hasToolBar()) {
            setToolBar();
        } else {
            mBaseBinding.toolBar.setVisibility(View.GONE);
        }

        //点击加载失败布局
        refresh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showLoading();
                onRefresh();
            }
        });
        bindingView.getRoot().setVisibility(View.GONE);

    }

    /**
     * 子类是否需要继承父类的toolbar
     *
     * @return
     */
    protected boolean hasToolBar() {
        return true;
    }

    /**
     * 设置titleBar
     */
    protected void setToolBar() {
        setSupportActionBar(mBaseBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_back);
        }
        //手动设置才有效果（子类确定要不要右边的菜单栏）
        mBaseBinding.toolBar.setTitleTextAppearance(this, R.style.ToolBar_Title);
        mBaseBinding.toolBar.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitle);
        //返回键
        mBaseBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    protected void showLoading() {
        if (llProgressbar.getVisibility() != View.VISIBLE) {
            llProgressbar.setVisibility(View.VISIBLE);
        }
        //开始动画
        if (mImg != null && mRotate != null) {
            mImg.startAnimation(mRotate);
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
    }

    protected void showContentView() {
        if (llProgressbar.getVisibility() != View.GONE) {
            llProgressbar.setVisibility(View.GONE);
        }
        //停止动画
        if (mImg != null && mRotate != null) {
            mImg.clearAnimation();
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
        if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 失败后点击刷新
     */
    protected void onRefresh() {

    }

    /**
     * 设置标题栏
     *
     * @param text
     */
    public void setTitle(CharSequence text) {
        mBaseBinding.tvTitle.setText(text);
    }

    public TextView getTvTitle() {
        return mBaseBinding.tvTitle;
    }
}
