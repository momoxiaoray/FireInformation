package com.xx.fire.activity.information;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.xx.fire.R;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.model.News;

import java.io.File;

import butterknife.BindView;

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.news_title)
    TextView newsTitle;
    @BindView(R.id.scan_count)
    TextView newsScanCount;
    @BindView(R.id.date)
    TextView newsDate;
    @BindView(R.id.content)
    TextView newsContent;
    @BindView(R.id.img_cover)
    ImageView img_cover;
    private DetailViewModel viewModel;
    private News newsData;

    @Override
    public void initExtras(Bundle bundle) {
        super.initExtras(bundle);
        newsData = (News) bundle.getSerializable("data");
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        viewModel.setData(newsData);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected String initTitle() {
        return getString(R.string.news_title_2);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        viewModel.getData().observe(this, new Observer<News>() {
            @Override
            public void onChanged(News news) {
                newsTitle.setText(news.getTitle());
                newsScanCount.setText(getString(R.string.scan_count, news.getScan_count()));
                newsDate.setText(news.getDate());
                newsContent.setText(news.getContent());
                Glide.with(mContext)
                        .load(new File(news.getCoverPath()))
                        .into(img_cover);
            }
        });
    }

    @Override
    public void doBusiness(Context context) {

    }
}
