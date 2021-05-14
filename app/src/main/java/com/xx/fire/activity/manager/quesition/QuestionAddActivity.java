package com.xx.fire.activity.manager.quesition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.maning.mndialoglibrary.MProgressDialog;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.activity.PicShowActivity;
import com.xx.fire.activity.dynamic.DynamicDetailViewModel;
import com.xx.fire.adapter.AnswerAdapter;
import com.xx.fire.adapter.ItemDynamicAdapter;
import com.xx.fire.adapter.ItemPicSelectAdapter;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;
import com.xx.fire.util.GlideEngine;
import com.xx.fire.view.InputCommentDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public class QuestionAddActivity extends BaseActivity {
    @BindView(R.id.tid_content)
    TextInputEditText editText;
    @BindView(R.id.add_answer)
    TextView add_answer;
    @BindView(R.id.answer_recycler)
    RecyclerView answerRecycler;

    private QuestionAddViewModel questionAddViewModel;
    private InputCommentDialog.Builder inputContent;
    private AnswerAdapter answerAdapter;

    @Override
    public void initExtras(Bundle bundle) {
        super.initExtras(bundle);
        questionAddViewModel = new ViewModelProvider(this).get(QuestionAddViewModel.class);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_qeustion_add;
    }

    @Override
    protected String initTitle() {
        return "添加问答";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitleHelper.setOnRightTxClickListener("发布", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (StringUtils.isEmpty(content)) {
                    ToastUtils.showShort("请输入内容");
                    return;
                }
                if (!questionAddViewModel.checkSave()) {
                    ToastUtils.showShort("添加答案至少两个");
                    return;
                }
                MProgressDialog.showProgress(mContext);
                questionAddViewModel.save(content);
                Observable.timer(1, TimeUnit.SECONDS)
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull Long aLong) {
                                MProgressDialog.dismissProgress();
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 200) {
                    ToastUtils.showShort("超过了最大字数");
                    editable.delete(editable.length() - 1, editable.length());
                }
            }
        });
        questionAddViewModel.getData().observe(this, new androidx.lifecycle.Observer<List<QuestionAnswer>>() {
            @Override
            public void onChanged(List<QuestionAnswer> questionAnswers) {
                if (questionAnswers.size() > 3) {
                    add_answer.setVisibility(View.GONE);
                } else {
                    add_answer.setVisibility(android.view.View.VISIBLE);
                }
                if (answerAdapter == null) {
                    answerRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                    answerAdapter = new AnswerAdapter(questionAnswers);
                    answerAdapter.setOnItemActionListener(new AnswerAdapter.OnItemActionListener() {
                        @Override
                        public void delete(QuestionAnswer answer, int position) {
                            questionAddViewModel.deleteAnswer(answer);
                        }
                    });
                    answerRecycler.setAdapter(answerAdapter);
                }
                answerAdapter.notifyDataSetChanged();
            }
        });

        add_answer.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputContent.setMessageHit("请输入内容")
                        .setSureClickListener(new InputCommentDialog.InputDialogBtnClickListener() {
                            @Override
                            public void onClick(String content) {
                                questionAddViewModel.addAnswer(content);
                            }
                        }).create().show();
            }
        });
        inputContent = new InputCommentDialog.Builder(this);

    }

    @Override
    public void doBusiness(Context context) {
        editText.requestFocus();
    }


}
