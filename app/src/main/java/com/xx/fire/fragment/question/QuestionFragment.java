package com.xx.fire.fragment.question;

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
import com.xx.fire.activity.manager.quesition.QuestionAddActivity;
import com.xx.fire.activity.question.QuestionDetailActivity;
import com.xx.fire.adapter.ItemQuestionAdapter;
import com.xx.fire.model.Question;
import com.xx.fire.view.RecycleViewDivider;

import java.util.List;


public class QuestionFragment extends Fragment {

    private QuestionViewModel viewModel;
    private RecyclerView dataRecycler;
    private ItemQuestionAdapter adapter;
    private TextView no_data_view;
    private boolean self = false;

    public static QuestionFragment newInstance(boolean self) {
        QuestionFragment fragment = new QuestionFragment();
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
                new ViewModelProvider(this).get(QuestionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_question, container, false);
        dataRecycler = root.findViewById(R.id.recycler);
        no_data_view = root.findViewById(R.id.no_data_view);
        dataRecycler.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1,
                ContextCompat.getColor(getContext(), R.color.grey_300)));

        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                if (questions == null || questions.size() == 0) {
                    no_data_view.setVisibility(View.VISIBLE);
                    return;
                }
                no_data_view.setVisibility(View.GONE);
                if (adapter == null) {
                    adapter = new ItemQuestionAdapter(getContext(), questions);
                    adapter.setOnItemActionListener(new ItemQuestionAdapter.OnActionListener() {
                        @Override
                        public void onHit(Question item, int position) {
                            viewModel.hit(position);
                        }

                        @Override
                        public void onDetailClick(Question item, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", item);
                            ActivityUtils.startActivity(bundle, QuestionDetailActivity.class);
                        }

                        @Override
                        public void onAnswerSelect(Question item, int position, int childPosition) {
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