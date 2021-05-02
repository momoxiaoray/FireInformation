package com.xx.fire.activity.collect;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.activity.newsdetail.NewsDetailActivity;
import com.xx.fire.activity.newsdetail.NewsDetailViewModel;
import com.xx.fire.adapter.ItemNewsAdapter;
import com.xx.fire.model.News;
import com.xx.fire.model.User;
import com.xx.fire.view.RecycleViewDivider;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CollectActivity extends BaseActivity {
    @BindView(R.id.list)
    RecyclerView dataRecycler;
    private ItemNewsAdapter adapter;
    private CollectViewModel viewModel;

    @Override
    public int bindLayout() {
        return R.layout.activity_collect_list;
    }

    @Override
    protected String initTitle() {
        return "我的收藏";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        dataRecycler.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 1,
                ContextCompat.getColor(this, R.color.grey_300)));
        viewModel = new ViewModelProvider(this).get(CollectViewModel.class);
        viewModel.getData().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                if (adapter == null) {
                    adapter = new ItemNewsAdapter(news);
                    adapter.setOnItemActionListener(new ItemNewsAdapter.OnItemActionListener() {
                        @Override
                        public void onItemClick(News news, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", news);
                            bundle.putInt("style", news.getNews_type());
                            ActivityUtils.startActivity(bundle, NewsDetailActivity.class);
                        }

                        @Override
                        public void onItemCollectClick(News news, int position) {
                            viewModel.remove(position);
                        }
                    });
                    dataRecycler.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void doBusiness(Context context) {

    }
}
