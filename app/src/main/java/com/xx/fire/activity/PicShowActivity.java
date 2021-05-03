package com.xx.fire.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.xx.fire.R;
import com.xx.fire.model.MediaData;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class PicShowActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.pager_index)
    TextView pagerIndex;
    List<MediaData> mediaDataList;
    private int selectPosition = 0;

    @Override
    public void initExtras(Bundle bundle) {
        super.initExtras(bundle);
        mediaDataList = (List<MediaData>) bundle.getSerializable("list");
        selectPosition = bundle.getInt("position");
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
        if (mediaDataList == null || mediaDataList.size() == 0)
            return;
        viewPager.setOffscreenPageLimit(mediaDataList.size());
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }


            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ImageView pic = (ImageView) View.inflate(mContext, R.layout.layout_pic_show, null);
                Glide.with(mContext)
                        .load(new File(mediaDataList.get(position).getPath()))
                        .into(pic);
                container.addView(pic);
                return pic;
            }

            @Override
            public int getCount() {
                return mediaDataList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPosition = position;
                pagerIndex.setText((position + 1) + "/" + mediaDataList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerIndex.setText((selectPosition + 1) + "/" + mediaDataList.size());
        viewPager.setCurrentItem(selectPosition);
    }

    @Override
    public void doBusiness(Context context) {

    }
}
