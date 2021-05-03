package com.xx.fire.fragment.home;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.xx.fire.R;
import com.xx.fire.activity.information.KnowledgeDetailActivity;
import com.xx.fire.activity.information.NewsDetailActivity;
import com.xx.fire.adapter.ItemNewsAdapter;
import com.xx.fire.model.News;
import com.xx.fire.view.RecycleViewDivider;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {
    public static final String ITEM_STYLE = "item_style";
    private int style;
    private RecyclerView dataRecycler;
    private ItemNewsAdapter adapter;
    private ItemViewModel viewModel;
    private TextView no_data_view;

    public ItemFragment() {
    }

    public static ItemFragment newInstance(int style) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ITEM_STYLE, style);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            style = getArguments().getInt(ITEM_STYLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        dataRecycler = view.findViewById(R.id.list);
        no_data_view = view.findViewById(R.id.no_data_view);
        dataRecycler.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1,
                ContextCompat.getColor(getContext(), R.color.grey_300)));
        initData();
        return view;
    }

    private void initData() {
        viewModel.getData(style).observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                no_data_view.setVisibility(news == null || news.size() == 0 ? View.VISIBLE : View.GONE);
                if (adapter == null) {
                    adapter = new ItemNewsAdapter(news);
                    adapter.setOnItemActionListener(new ItemNewsAdapter.OnItemActionListener() {
                        @Override
                        public void onItemClick(News news, int position) {
                            viewModel.addScanCount(position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", news);
                            if (style == 0) {
                                ActivityUtils.startActivity(bundle, KnowledgeDetailActivity.class);
                            } else {
                                ActivityUtils.startActivity(bundle, NewsDetailActivity.class);
                            }
                        }

                        @Override
                        public void onItemCollectClick(News news, int position) {
                            viewModel.collectAction(position);
                        }
                    });
                    dataRecycler.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}