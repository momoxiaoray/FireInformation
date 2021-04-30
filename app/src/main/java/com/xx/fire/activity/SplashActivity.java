package com.xx.fire.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.xx.fire.R;
import com.xx.fire.view.SplashView;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_container)
    FrameLayout container;
    private IHandler handler;

    @Override
    public int bindLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected String initTitle() {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setStatusBar(true);
        SplashView splashView = new SplashView(mContext);
        splashView.setOnTouchListener((view, motionEvent) -> true);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        container.addView(splashView, 0, params);
    }

    @Override
    public void doBusiness(Context context) {
        handler = new IHandler(this);
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    static class IHandler extends Handler {
        WeakReference<SplashActivity> mActivity;

        public IHandler(SplashActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            SplashActivity activity = mActivity.get();
            if (activity == null)
                return;
            switch (msg.what) {
                case 0:
                    ActivityUtils.startActivity(LoginActivity.class);
                    activity.finish();
                    break;
            }
        }
    }
}
