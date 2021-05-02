package com.xx.fire.activity.dynamic;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.maning.mndialoglibrary.MProgressDialog;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.xx.fire.R;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.adapter.ItemPicSelectAdapter;
import com.xx.fire.model.MediaData;
import com.xx.fire.util.GetPhotoFromAlbum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class DynamicAddActivity extends BaseActivity {
    public static final int OPEN_FILE_REQUEST_CODE = 101;
    @BindView(R.id.tid_content)
    TextInputEditText editText;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private ItemPicSelectAdapter picSelectAdapter;
    private RxPermissions rxPermissions;
    private DynamicAddViewModel viewModel;

    @Override
    public int bindLayout() {
        return R.layout.activity_dynamic_add;
    }

    @Override
    protected String initTitle() {
        return "添加动态";
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
                viewModel.saveDynamic(content);
                Observable.timer(2, TimeUnit.SECONDS)
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
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        rxPermissions = new RxPermissions(this);
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

        viewModel = new ViewModelProvider(this).get(DynamicAddViewModel.class);
        viewModel.getData().observe(this, new androidx.lifecycle.Observer<List<MediaData>>() {
            @Override
            public void onChanged(List<MediaData> mediaData) {
                if (picSelectAdapter == null) {
                    picSelectAdapter = new ItemPicSelectAdapter(mContext, mediaData);
                    picSelectAdapter.setOnItemActionListener(new ItemPicSelectAdapter.OnItemActionListener() {
                        @Override
                        public void onAdd() {
                            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    .subscribe(new Observer<Boolean>() {
                                        @Override
                                        public void onSubscribe(@NonNull Disposable d) {

                                        }

                                        @Override
                                        public void onNext(@NonNull Boolean aBoolean) {
                                            if (aBoolean) {
                                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                                intent.setType("image/*");
                                                startActivityForResult(intent, OPEN_FILE_REQUEST_CODE);
                                            } else {
                                                Toast.makeText(mContext, "拒绝权限后无法访问手机文件", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError(@NonNull Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });


                        }

                        @Override
                        public void onItemClick(MediaData data, int position) {

                        }

                        @Override
                        public void onItemDelete(MediaData data, int position) {
                            viewModel.remove(position);
                        }
                    });
                    recyclerView.setAdapter(picSelectAdapter);
                }
                picSelectAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void doBusiness(Context context) {
        editText.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_FILE_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                        String path = GetPhotoFromAlbum.getRealPathFromUri(mContext, data.getData());
                        viewModel.add(path, true);
                    }
                    break;
            }
        }
    }
}
