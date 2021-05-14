package com.xx.fire.fragment.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.xx.fire.adapter.ItemManagerQuestionAdapter;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;
import com.xx.fire.view.AnswerInputDialog;
import com.xx.fire.view.AnswerSelectDialog;
import com.xx.fire.view.RecycleViewDivider;

import java.util.List;


public class ManagerQuestionFragment extends Fragment {

    private ManagerQuestionViewModel viewModel;
    private RecyclerView dataRecycler;
    private ItemManagerQuestionAdapter adapter;
    private TextView no_data_view;
    private FloatingActionButton addButton;
    private AnswerInputDialog.Builder answerInputDialog;
    private AnswerSelectDialog.Builder answerSelectDialog;

    public static ManagerQuestionFragment newInstance() {
        ManagerQuestionFragment fragment = new ManagerQuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        answerInputDialog = new AnswerInputDialog.Builder(getContext());
        answerSelectDialog = new AnswerSelectDialog.Builder(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(ManagerQuestionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dynamic, container, false);
        dataRecycler = root.findViewById(R.id.recycler);
        no_data_view = root.findViewById(R.id.no_data_view);
        addButton = root.findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.startActivity(QuestionAddActivity.class);
            }
        });
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
                    adapter = new ItemManagerQuestionAdapter(getContext(), questions);
                    adapter.setOnItemActionListener(new ItemManagerQuestionAdapter.OnActionListener() {
                        @Override
                        public void delete(Question item, int position) {
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

                        @Override
                        public void selectRight(Question item, int position) {
                            if (item.getAnswer().size() > 0) {
                                answerSelectDialog.setData(item.getAnswer()).setOnSelectListener(new AnswerSelectDialog.OnSelectListener() {
                                    @Override
                                    public void onSelect(QuestionAnswer answer) {
                                        viewModel.answerPublish(item, answer);
                                        answerSelectDialog.cancel();
                                    }
                                }).create().show();
                            } else {
                                answerInputDialog.setSureClickListener(new AnswerInputDialog.InputDialogBtnClickListener() {

                                    @Override
                                    public void onClick(String content) {
                                        viewModel.answerPublish(item, content);
                                        answerInputDialog.cancel();
                                    }
                                }).create().show();
                            }
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
        viewModel.refreshData();
    }
}