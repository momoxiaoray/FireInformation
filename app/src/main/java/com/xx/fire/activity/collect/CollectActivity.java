package com.xx.fire.activity.collect;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.xx.fire.R;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.activity.information.KnowledgeDetailActivity;
import com.xx.fire.activity.information.NewsDetailActivity;
import com.xx.fire.adapter.ItemCollectAdapter;
import com.xx.fire.model.News;
import com.xx.fire.view.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;

public class CollectActivity extends BaseActivity {
    @BindView(R.id.list)
    RecyclerView dataRecycler;
    @BindView(R.id.no_data_view)
    TextView no_data_view;
    private ItemCollectAdapter adapter;
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
                no_data_view.setVisibility(news == null || news.size() == 0 ? View.VISIBLE : View.GONE);
                if (adapter == null) {
                    adapter = new ItemCollectAdapter(mContext, news);
                    adapter.setOnItemActionListener(new ItemCollectAdapter.OnItemActionListener() {
                        @Override
                        public void onItemClick(News news, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", news);
                            if (news.getNews_type() == 0) {
                                ActivityUtils.startActivity(bundle, KnowledgeDetailActivity.class);
                            } else {
                                ActivityUtils.startActivity(bundle, NewsDetailActivity.class);
                            }
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
