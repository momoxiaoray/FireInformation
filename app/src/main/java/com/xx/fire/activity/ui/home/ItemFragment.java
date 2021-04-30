package com.xx.fire.activity.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.R;
import com.xx.fire.adapter.ItemNewsAdapter;
import com.xx.fire.model.News;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {
    public static final String ITEM_STYLE = "item_style";
    private int style;
    private RecyclerView dataRecycler;
    private ItemNewsAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        dataRecycler = view.findViewById(R.id.list);
        initData();
        return view;
    }

    private void initData() {
        List<News> newList = LitePal.where("news_type = ?", style + "").find(News.class);
        if (newList == null || newList.size() == 0) {
            newList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                News news = new News();
                news.setTitle((style == 0 ? "消防知识" : "新闻") + i);
                news.setContent((style == 0 ? "消防内容" : "新闻内容") + i);
                news.setDate(TimeUtils.getNowString());
                news.setScan_count(i);
                news.setNews_type(style);
                news.save();
                newList.add(news);
            }
        }
        adapter = new ItemNewsAdapter(newList);
        dataRecycler.setAdapter(adapter);
    }
}