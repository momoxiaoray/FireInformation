package com.xx.fire.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.xx.fire.util.T;
import com.xx.fire.util.TitleHelper;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 绑定布局
     *
     * @return 布局Id
     */
    public abstract int bindLayout();

    /**
     * 设置标题，如果没有则传入null
     *
     * @return title标题
     */
    protected abstract String initTitle();

    /**
     * 初始化view
     */
    public abstract void initView(Bundle savedInstanceState);

    /**
     * 业务操作
     *
     * @param context 上下文
     */
    public abstract void doBusiness(Context context);

    protected Context mContext;
    protected TitleHelper mTitleHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        T.cancel();
        initExtras(getIntent().getExtras());
        setContentView(bindLayout(), initTitle());
        ButterKnife.bind(this);
        initView(savedInstanceState);
        doBusiness(mContext);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    public void initExtras(Bundle bundle) {
        //接收参数
    }

    /**
     * 设置ContentView
     *
     * @param layoutResID contentLayoutId
     * @param title       title 如果不使用公用的TitleBar，则传null
     */
    public void setContentView(int layoutResID, String title) {
        if (title == null) {
            setContentView(bindLayout());
        } else {
            mTitleHelper = new TitleHelper(this, layoutResID);
            mTitleHelper.setTitle(title);
            setContentView(mTitleHelper.getContentView());
        }
    }

    /**
     * 沉浸式状态栏
     */
    public void setStatusBar(boolean isImmersive) {
        UltimateBar.Companion.with(this)
                .statusDark(isImmersive)                  // 状态栏灰色模式(Android 6.0+)，默认 flase
                .applyNavigation(false)              // 应用到导航栏，默认 flase
                .navigationDark(false)              // 导航栏灰色模式(Android 8.0+)，默认 false
                .create()
                .immersionBar();
        if (isImmersive && mTitleHelper != null) {
            mTitleHelper.dealContentView();
        }
    }

}
