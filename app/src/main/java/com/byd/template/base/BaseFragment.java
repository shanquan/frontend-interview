package com.byd.template.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yy2591033 on 2017/2/3.
 *
 * 基础Fragment
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        initViews();
//        initData();
        bindListener();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        initViews();
        initData();
//        bindListener();
    }

    protected abstract void bindListener();

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract int getLayoutId();
}

