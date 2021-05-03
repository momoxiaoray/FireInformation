package com.xx.fire.activity.manager.edit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.xx.fire.R;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.activity.dynamic.DynamicDetailViewModel;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.News;
import com.xx.fire.util.GlideEngine;
import com.xx.fire.util.T;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

public
class MangerNewsEditActivity extends BaseActivity {
    @BindView(R.id.tid_title)
    TextInputEditText titleEdit;
    @BindView(R.id.tid_content)
    TextInputEditText contentEdit;
    @BindView(R.id.add_pic_layout)
    LinearLayout addPicLayout;
    @BindView(R.id.btn_add)
    ImageView addPic;
    @BindView(R.id.img_pic)
    ImageView imgPic;
    @BindView(R.id.btn_delete)
    ImageView btnDelete;
    @BindView(R.id.img_data_layout)
    Group dataGroupLayout;
    private NewsEditViewModel viewModel;
    private int style, edit_action;

    @Override
    public void initExtras(Bundle bundle) {
        super.initExtras(bundle);
        viewModel = new ViewModelProvider(this).get(NewsEditViewModel.class);
        style = bundle.getInt("style");
        edit_action = bundle.getInt("edit_action");
        viewModel.initData((News) bundle.getSerializable("data"));
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_manger_news_edit;
    }

    @Override
    protected String initTitle() {
        return edit_action == 0 ? (style == 0 ? "新增消防知识" : "新增火灾新闻") : "内容编辑";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        addPicLayout.setVisibility(style == 0 ? View.GONE : View.VISIBLE);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataGroupLayout.setVisibility(View.GONE);
                addPic.setVisibility(View.VISIBLE);
                viewModel.removePic();
            }
        });
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyPhotos
                        .createAlbum((Activity) mContext, true, false, GlideEngine.getInstance())
                        //参数说明：见下方`FileProvider的配置`
                        .setFileProviderAuthority("com.xx.fire.fileprovider")
                        .start(new SelectCallback() {
                            @Override
                            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                                dataGroupLayout.setVisibility(View.VISIBLE);
                                addPic.setVisibility(View.GONE);
                                viewModel.addPic(photos.get(0).path);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });
        viewModel.getLiveData().observe(this, new Observer<News>() {
            @Override
            public void onChanged(News news) {
                titleEdit.setText(news.getTitle());
                contentEdit.setText(news.getContent());
                titleEdit.setSelection(news.getTitle().length());
                if (style == 1) {
                    if (news.getCoverPath() != null) {
                        dataGroupLayout.setVisibility(View.VISIBLE);
                        addPic.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(new File(news.getCoverPath()))
                                .into(imgPic);
                    } else {
                        dataGroupLayout.setVisibility(View.GONE);
                        addPic.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mTitleHelper.setOnRightTxClickListener("保存", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEdit.getText().toString();
                String content = contentEdit.getText().toString();
                if (StringUtils.isEmpty(title)) {
                    T.showToast("请输入标题");
                    return;
                }
                if (title.length() < 5) {
                    T.showToast("标题太短啦，至少五个字");
                    return;
                }
                if (StringUtils.isEmpty(content)) {
                    T.showToast("请输入内容");
                    return;
                }
                if (content.length() < 20) {
                    T.showToast("内容太短啦，至少20个字");
                    return;
                }
                if (style == 1) {
                    String path = viewModel.getData().getCoverPath();
                    if (StringUtils.isEmpty(path)) {
                        T.showToast("请添加一张封面图");
                        return;
                    }
                }
                viewModel.save(title, content, style);
                finish();
            }
        });

        titleEdit.requestFocus();
    }

    @Override
    public void doBusiness(Context context) {
        titleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 50) {
                    ToastUtils.showShort("超过了最大字数");
                    editable.delete(editable.length() - 1, editable.length());
                }
            }
        });
        contentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 400) {
                    ToastUtils.showShort("超过了最大字数");
                    editable.delete(editable.length() - 1, editable.length());
                }
            }
        });
    }
}
