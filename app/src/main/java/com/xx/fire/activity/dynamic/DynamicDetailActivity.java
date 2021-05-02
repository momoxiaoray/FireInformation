package com.xx.fire.activity.dynamic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.activity.PicShowActivity;
import com.xx.fire.activity.newsdetail.NewsDetailViewModel;
import com.xx.fire.adapter.ItemChildPicAdapter;
import com.xx.fire.adapter.ItemCommentAdapter;
import com.xx.fire.model.Comment;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.News;
import com.xx.fire.view.InputCommentDialog;
import com.xx.fire.view.RecycleViewDivider;

import java.lang.annotation.Native;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public
class DynamicDetailActivity extends BaseActivity {
    @BindView(R.id.item_icon)
    ImageView icon;
    @BindView(R.id.item_name)
    TextView name;
    @BindView(R.id.item_time)
    TextView time;
    @BindView(R.id.item_comment)
    TextView commentCount;
    @BindView(R.id.item_zan)
    TextView zanCount;
    @BindView(R.id.item_content)
    TextView content;
    @BindView(R.id.item_recycler)
    RecyclerView recycler;
    @BindView(R.id.item_recycler_comment)
    RecyclerView recyclerComment;
    @BindView(R.id.no_comment_data_view)
    TextView noCommentView;
    @BindView(R.id.item_delete)
    TextView item_delete;
    private DynamicDetailViewModel viewModel;
    private InputCommentDialog.Builder commentDialog;
    private ItemChildPicAdapter mediaAdapter;
    private ItemCommentAdapter commentAdapter;
    private LinearLayoutManager commentLayoutManager;

    @Override
    public void initExtras(Bundle bundle) {
        super.initExtras(bundle);
        viewModel = new ViewModelProvider(this).get(DynamicDetailViewModel.class);
        viewModel.initData((Dynamic) bundle.getSerializable("data"));
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_dynamic_detail;
    }

    @Override
    protected String initTitle() {
        return "动态详情";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        commentDialog = new InputCommentDialog.Builder(this);
        viewModel.getData().observe(this, new Observer<Dynamic>() {
            @Override
            public void onChanged(Dynamic dynamic) {
                if (dynamic.getUser().getAccount().equals(UserUtil.getCurrentUser().getAccount())) {
                    item_delete.setVisibility(View.VISIBLE);
                    item_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(mContext)
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
                                            int succeed = dynamic.delete();
                                            finish();
                                        }
                                    }).create().show();
                        }
                    });
                } else {
                    item_delete.setVisibility(View.GONE);
                }
                name.setText(dynamic.getUser().getNickname());
                time.setText(TimeUtils.getFriendlyTimeSpanByNow(dynamic.getDate()));
                //得到drawable对象，即所要插入的图片
                Drawable d = mContext.getResources().getDrawable(R.mipmap.ic_zan);
                zanCount.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                zanCount.setCompoundDrawablePadding(mContext.getResources().getDimensionPixelOffset(R.dimen.dp_4));
                zanCount.setText(String.valueOf(dynamic.getZan()));
                zanCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.hit();
                    }
                });
                //回复
                commentCount.setText(String.valueOf(dynamic.getComment_list().size()));
                content.setText(dynamic.getDynamic_content());
                //展示媒体
                List<MediaData> mediaData = dynamic.getMedia_list();
                if (mediaData == null || mediaData.size() == 0) {
                    recycler.setVisibility(View.GONE);
                } else {
                    recycler.setVisibility(View.VISIBLE);
                    if (mediaAdapter == null) {
                        recycler.setLayoutManager(new GridLayoutManager(mContext, 3));
                        mediaAdapter = new ItemChildPicAdapter(mContext, mediaData);
                        mediaAdapter.setOnItemActionListener(new ItemChildPicAdapter.OnItemActionListener() {
                            @Override
                            public void onItemClick(MediaData data, int position) {
                                if (data.getType() == 0) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("mediaData", data);
                                    ActivityUtils.startActivity(bundle, PicShowActivity.class);
                                }
                            }
                        });
                        recycler.setAdapter(mediaAdapter);
                    } else {
                        mediaAdapter.notifyDataSetChanged();
                    }
                }
                //展示评论列表
                List<Comment> comments = dynamic.getComment_list();
                if (comments == null || comments.size() == 0) {
                    noCommentView.setVisibility(View.VISIBLE);
                } else {
                    noCommentView.setVisibility(View.GONE);
                    if (commentAdapter == null) {
                        recyclerComment.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, 1,
                                ContextCompat.getColor(mContext, R.color.grey_300)));
                        commentLayoutManager = new LinearLayoutManager(mContext);
                        recyclerComment.setLayoutManager(commentLayoutManager);
                        commentAdapter = new ItemCommentAdapter(mContext, comments);
                        commentAdapter.setOnItemActionListener(new ItemCommentAdapter.OnItemActionListener() {
                            @Override
                            public void onComment(Comment comment, int position) {
                                showCommentDialog(comment, "回复@" + comment.getComment_username() + ":");
                            }
                        });
                        recyclerComment.setAdapter(commentAdapter);
                    } else {
                        commentAdapter.notifyDataSetChanged();
                    }
                    commentLayoutManager.scrollToPosition(0);
                }
            }
        });
    }

    @Override
    public void doBusiness(Context context) {

    }

    private void showCommentDialog(Comment comment, String hit) {
        commentDialog
                .setMessageHit(hit)
                .setSureClickListener(new InputCommentDialog.InputDialogBtnClickListener() {
                    @Override
                    public void onClick(String content) {
                        viewModel.addComment(comment, content);
                    }
                }).create().show();
    }

    @OnClick({R.id.edit_comment})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_comment:
                showCommentDialog(null, "说点什么...");
                break;
        }
    }
}
