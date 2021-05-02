package com.xx.fire.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xx.fire.R;
import com.xx.fire.model.MediaData;

import java.io.File;

import butterknife.BindView;

public class PicShowActivity extends BaseActivity{
    @BindView(R.id.pic)
    ImageView imageView;

    MediaData mediaData;

    @Override
    public void initExtras(Bundle bundle) {
        super.initExtras(bundle);
        mediaData = (MediaData) bundle.getSerializable("mediaData");
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_pic_show;
    }

    @Override
    protected String initTitle() {
        return "图片展示";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Glide.with(mContext)
                .load(new File(mediaData.getPath()))
                .into(imageView);
    }

    @Override
    public void doBusiness(Context context) {

    }
}
