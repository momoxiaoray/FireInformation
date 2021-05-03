package com.xx.fire.fragment.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xx.fire.R;
import com.xx.fire.activity.manager.edit.MangerNewsEditActivity;
import com.xx.fire.adapter.ItemMangerNewsAdapter;
import com.xx.fire.model.News;
import com.xx.fire.view.RecycleViewDivider;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemMangerFragment extends Fragment {
    public static final String ITEM_STYLE = "item_style";
    private int style;
    private RecyclerView dataRecycler;
    private TextView no_data_view;
    private ItemMangerNewsAdapter adapter;
    private ItemMangerViewModel viewModel;
    private FloatingActionButton addButton;


    public ItemMangerFragment() {
    }

    public static ItemMangerFragment newInstance(int style) {
        ItemMangerFragment fragment = new ItemMangerFragment();
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
        viewModel = new ViewModelProvider(this).get(ItemMangerViewModel.class);
        View view = inflater.inflate(R.layout.fragment_item_mannger_list, container, false);
        dataRecycler = view.findViewById(R.id.recycler);
        no_data_view = view.findViewById(R.id.no_data_view);
        addButton = view.findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("style", style);
                bundle.putInt("edit_action", 0);
                ActivityUtils.startActivity(bundle, MangerNewsEditActivity.class);
            }
        });
        dataRecycler.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1,
                ContextCompat.getColor(getContext(), R.color.grey_300)));
        initData();
        return view;
    }

    private void initData() {
        viewModel.getData(style).observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                if (news == null || news.size() == 0) {
                    no_data_view.setVisibility(View.VISIBLE);
                    return;
                }
                no_data_view.setVisibility(View.GONE);
                if (adapter == null) {
                    adapter = new ItemMangerNewsAdapter(news);
                    adapter.setOnItemActionListener(new ItemMangerNewsAdapter.OnItemActionListener() {
                        @Override
                        public void onItemClick(News news, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", news);
                            bundle.putInt("edit_action", 1);
                            bundle.putInt("style", style);
                            ActivityUtils.startActivity(bundle, MangerNewsEditActivity.class);
                        }

                        @Override
                        public void onItemDeleteClick(News news, int position) {
                            new AlertDialog.Builder(getContext())
                                    .setMessage("是否删除")
                                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            viewModel.delete(position);
                                        }
                                    }).create().show();
                        }
                    });
                    dataRecycler.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshData(style);
    }
}