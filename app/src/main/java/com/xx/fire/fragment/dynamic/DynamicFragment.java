package com.xx.fire.fragment.dynamic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xx.fire.R;
import com.xx.fire.activity.dynamic.DynamicAddActivity;
import com.xx.fire.activity.dynamic.DynamicDetailActivity;
import com.xx.fire.adapter.ItemDynamicAdapter;
import com.xx.fire.fragment.question.QuestionFragment;
import com.xx.fire.model.Dynamic;
import com.xx.fire.view.RecycleViewDivider;

import java.util.List;


public class DynamicFragment extends Fragment {

    private DynamicViewModel viewModel;
    private RecyclerView dataRecycler;
    private ItemDynamicAdapter adapter;
    private TextView no_data_view;
    private FloatingActionButton addButton;
    private boolean self = false;

    public static DynamicFragment newInstance(boolean self) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putBoolean("self", self);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            self = getArguments().getBoolean("self");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(DynamicViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dynamic, container, false);
        dataRecycler = root.findViewById(R.id.recycler);
        no_data_view = root.findViewById(R.id.no_data_view);
        addButton = root.findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.startActivity(DynamicAddActivity.class);
            }
        });
        if (self) {
            addButton.setVisibility(View.GONE);
        }
        dataRecycler.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1,
                ContextCompat.getColor(getContext(), R.color.grey_300)));
        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Dynamic>>() {
            @Override
            public void onChanged(List<Dynamic> dynamics) {
                if (dynamics == null || dynamics.size() == 0) {
                    no_data_view.setVisibility(View.VISIBLE);
                    return;
                }
                no_data_view.setVisibility(View.GONE);
                if (adapter == null) {
                    adapter = new ItemDynamicAdapter(getContext(), dynamics);
                    adapter.setOnItemActionListener(new ItemDynamicAdapter.OnActionListener() {
                        @Override
                        public void onHit(Dynamic item, int position) {
                            viewModel.hit(position);
                        }

                        @Override
                        public void onDetailClick(Dynamic item, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", item);
                            ActivityUtils.startActivity(bundle, DynamicDetailActivity.class);
                        }
                    });
                    dataRecycler.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshData(self);
    }
}