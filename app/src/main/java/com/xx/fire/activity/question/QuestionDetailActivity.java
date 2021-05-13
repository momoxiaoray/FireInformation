package com.xx.fire.activity.question;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.xx.fire.adapter.ItemChildPicAdapter;
import com.xx.fire.adapter.ItemCommentAdapter;
import com.xx.fire.model.Comment;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;
import com.xx.fire.view.InputCommentDialog;
import com.xx.fire.view.RecycleViewDivider;

import org.litepal.LitePal;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public
class QuestionDetailActivity extends BaseActivity {
    @BindView(R.id.answer_layout)
    RadioGroup answerGroup;
    @BindView(R.id.item_time)
    TextView time;
    @BindView(R.id.item_comment)
    TextView commentCount;
    @BindView(R.id.item_zan)
    TextView zanCount;
    @BindView(R.id.item_content)
    TextView content;
    @BindView(R.id.item_recycler_comment)
    RecyclerView recyclerComment;
    @BindView(R.id.no_comment_data_view)
    TextView noCommentView;
    @BindView(R.id.item_delete)
    TextView item_delete;
    private QuestionDetailViewModel viewModel;
    private InputCommentDialog.Builder commentDialog;
    private ItemCommentAdapter commentAdapter;
    private LinearLayoutManager commentLayoutManager;

    @Override
    public void initExtras(Bundle bundle) {
        super.initExtras(bundle);
        viewModel = new ViewModelProvider(this).get(QuestionDetailViewModel.class);
        viewModel.initData((Question) bundle.getSerializable("data"));
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_question_detail;
    }

    @Override
    protected String initTitle() {
        return "问答详情";
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        commentDialog = new InputCommentDialog.Builder(this);
        viewModel.getData().observe(this, new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
//                if (question.getUser().getAccount().equals(UserUtil.getCurrentUser().getAccount())) {
//                    item_delete.setVisibility(View.VISIBLE);
//                    item_delete.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            new AlertDialog.Builder(mContext)
//                                    .setMessage("是否删除")
//                                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            dialogInterface.dismiss();
//                                        }
//                                    })
//                                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            LitePal.delete(Question.class, question.getId());
//                                            finish();
//                                        }
//                                    }).create().show();
//                        }
//                    });
//                } else {
//                    item_delete.setVisibility(View.GONE);
//                }

                time.setText(TimeUtils.getFriendlyTimeSpanByNow(question.getDate()));
                //得到drawable对象，即所要插入的图片
                Drawable d = mContext.getResources().getDrawable(R.mipmap.ic_zan);
                zanCount.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                zanCount.setCompoundDrawablePadding(mContext.getResources().getDimensionPixelOffset(R.dimen.dp_4));
                zanCount.setText(String.valueOf(question.getZan()));
                zanCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.hit();
                    }
                });
                //回复
                commentCount.setText(String.valueOf(question.getComment_list().size()));
                content.setText(question.getContent());
                //展示评论列表
                List<Comment> comments = question.getComment_list();
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
                                showCommentDialog(comment, "回复@" + comment.getUser().getNickname() + ":");
                            }
                        });
                        recyclerComment.setAdapter(commentAdapter);
                    } else {
                        commentAdapter.notifyDataSetChanged();
                    }
                    commentLayoutManager.scrollToPosition(0);
                }

                //答案
                List<QuestionAnswer> answers = question.getAnswer();
                if (answers != null && answers.size() > 0) {
                    if (answerGroup.getChildCount() > 0) {
                        return;
                    }
                    for (int i = 0; i < answers.size(); i++) {
                        RadioButton radioButton = new RadioButton(mContext);
                        radioButton.setTag(i);
                        radioButton.setText(answers.get(i).getAnswer_content());
                        radioButton.setTextColor(ContextCompat.getColor(mContext, R.color.grey_700));
                        radioButton.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        answerGroup.addView(radioButton);
                        radioButton.setChecked(answers.get(i).getUser_ids().contains(UserUtil.getCurrentUser().getId()));
                        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (!compoundButton.isPressed()) {
                                    return;
                                }
                                if (b)
                                    viewModel.selectAnswer((Integer) compoundButton.getTag());
                            }
                        });
                    }
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
