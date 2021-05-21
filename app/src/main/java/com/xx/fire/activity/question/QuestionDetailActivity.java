package com.xx.fire.activity.question;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.activity.BaseActivity;
import com.xx.fire.adapter.ItemCommentAdapter;
import com.xx.fire.model.QuestionAnswerUser;
import com.xx.fire.model.Comment;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;
import com.xx.fire.view.InputCommentDialog;
import com.xx.fire.view.RecycleViewDivider;

import org.litepal.LitePal;

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
    @BindView(R.id.publish_right_content)
    TextView publish_right_content;
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
                        radioButton.setGravity(Gravity.CENTER_VERTICAL);
                        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                        radioButton.setText(answers.get(i).getAnswer_content());
                        radioButton.setTextColor(ContextCompat.getColor(mContext, R.color.grey_700));
                        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.rightMargin = (int) mContext.getResources().getDimension(R.dimen.dp_12);
                        layoutParams.topMargin = (int) mContext.getResources().getDimension(R.dimen.dp_8);
                        radioButton.setLayoutParams(layoutParams);
                        answerGroup.addView(radioButton);
                        List<QuestionAnswerUser> userIds = LitePal.where("answer_id = ? and user_id =?", String.valueOf(answers.get(i).getId()),
                                String.valueOf(UserUtil.getCurrentUser().getId()))
                                .order("id desc").find(QuestionAnswerUser.class, true);
                        radioButton.setChecked(userIds.size() > 0);
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
                //正确答案
//                if (question.getRight_answer_id() > 0) {
//                    //表示记录的是正确答案id，去数据库中查这个答案即可
//                    publish_right_content.setVisibility(View.VISIBLE);
//                    QuestionAnswer answer = LitePal.find(QuestionAnswer.class, question.getRight_answer_id());
//                    publish_right_content.setText("正确答案:" + answer.getAnswer_content());
//                } else {
//                    if (StringUtils.isEmpty(question.getRight_answer())) {
//                        publish_right_content.setVisibility(View.GONE);
//                    }else {
//                        publish_right_content.setVisibility(View.VISIBLE);
//                        publish_right_content.setText("正确答案:" + question.getRight_answer());
//                    }
//                }
            }
        });

        publish_right_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long answer_id = viewModel.getData().getValue().getRight_answer_id();
                QuestionAnswer answer = LitePal.find(QuestionAnswer.class, answer_id);
                publish_right_content.setText("正确答案:" + answer.getAnswer_content());
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
