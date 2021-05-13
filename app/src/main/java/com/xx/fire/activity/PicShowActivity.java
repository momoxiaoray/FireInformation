package com.xx.fire.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
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
        return "动态展示";
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
                View layout = View.inflate(mContext, R.layout.layout_pic_show, null);
                ImageView pic = layout.findViewById(R.id.pic);
                Glide.with(mContext)
                        .load(new File(mediaDataList.get(position).getPath()))
                        .into(pic);
                ImageView type = layout.findViewById(R.id.item_type);
                if (mediaDataList.get(position).getType() == 1) {
                    type.setVisibility(View.VISIBLE);
                    pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mediaDataList.get(position).getType() == 1) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                File file = new File(mediaDataList.get(position).getPath());
                                Uri uri = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    uri = FileProvider.getUriForFile(mContext, "com.xx.fire.fileprovider", file);
                                } else {
                                    uri = Uri.fromFile(file);
                                }
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(uri, "video/*");
                                mContext.startActivity(intent);
                            }
                        }
                    });
                } else {
                    type.setVisibility(View.GONE);
                }
                container.addView(layout);
                return layout;
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
