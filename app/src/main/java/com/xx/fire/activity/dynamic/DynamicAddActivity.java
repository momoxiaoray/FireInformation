package com.xx.fire.activity.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.engine.ImageEngine;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.maning.mndialoglibrary.MProgressDialog;
import com.xx.fire.R;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.activity.PicShowActivity;
import com.xx.fire.adapter.ItemPicSelectAdapter;
import com.xx.fire.model.MediaData;
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


public class DynamicAddActivity extends BaseActivity {
    public static final int OPEN_FILE_REQUEST_CODE = 101;
    @BindView(R.id.tid_content)
    TextInputEditText editText;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private ItemPicSelectAdapter picSelectAdapter;
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
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
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
                            //参数说明：上下文，是否显示相机按钮，是否使用宽高数据（false时宽高数据为0，扫描速度更快），
                            EasyPhotos
                                    .createAlbum((Activity) mContext, true, false, GlideEngine.getInstance())
                                    //参数说明：见下方`FileProvider的配置`
                                    .setFileProviderAuthority("com.xx.fire.fileprovider")
                                    .setCount(6)//参数说明：最大可选数，默认1
                                    .setSelectedPhotos(viewModel.getImageItems())
                                    .setPuzzleMenu(false)
                                    .start(new SelectCallback() {
                                        @Override
                                        public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                                            viewModel.setImageItems(photos);
                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });

                        }

                        @Override
                        public void onItemClick(MediaData data, int position) {
                            if (data.getType() == 0) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("position", position);
                                bundle.putSerializable("list", (Serializable) viewModel.getMediaData());
                                ActivityUtils.startActivity(bundle, PicShowActivity.class);
                            }
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
//                case ImagePicker.RESULT_CODE_ITEMS:
//                    if (data != null) {
//                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                        viewModel.setImageItems(images);
//                    } else {
//                        Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
//                    }
//                    break;
            }
        }
    }
}
