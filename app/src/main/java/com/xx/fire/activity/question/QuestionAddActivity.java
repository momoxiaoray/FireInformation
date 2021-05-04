package com.xx.fire.activity.question;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.xx.fire.adapter.ItemPicSelectAdapter;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.Question;
import com.xx.fire.util.GlideEngine;

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
                MProgressDialog.showProgress(mContext);
                Question question = new Question();
                question.setDate(TimeUtils.getNowString());
                question.setUser_id(UserUtil.getCurrentUser().getId());
                question.setContent(content);
                question.setZan(0);
                question.save();
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
    }

    @Override
    public void doBusiness(Context context) {
        editText.requestFocus();
    }

}
