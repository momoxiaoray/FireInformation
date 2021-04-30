package com.xx.fire.view;

import android.content.Context;


import androidx.appcompat.widget.AppCompatImageView;

import com.xx.fire.util.DataUtil;

import java.util.List;
import java.util.Random;

public class SplashView extends AppCompatImageView {
    public Context mContext;

    public SplashView(Context context) {
        super(context);
        mContext = context;
        setScaleType(ScaleType.FIT_XY);
        showSplashView();
    }

    /**
     * 显示启动页图片
     */
    private void showSplashView() {
        List<Integer> splashModelList = DataUtil.getSplashList();
        int Max = splashModelList.size();
        int Min = 1;
        Random random = new Random();
        int s = random.nextInt(Max - Min + 1) + Min;
        int imgSrc = splashModelList.get(s - 1);
        setImageResource(imgSrc);
    }
}
