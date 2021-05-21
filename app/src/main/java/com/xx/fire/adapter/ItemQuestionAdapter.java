package com.xx.fire.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.activity.PicShowActivity;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;

import org.litepal.LitePal;

import java.io.Serializable;
import java.util.List;

public class ItemQuestionAdapter extends RecyclerView.Adapter<ItemQuestionAdapter.ViewHolder> {

    private final List<Question> mValues;
    private OnActionListener actionListener;
    private Context mContext;

    public ItemQuestionAdapter(Context context, List<Question> items) {
        mValues = items;
        mContext = context;
    }

    public void setOnItemActionListener(OnActionListener onItemActionListener) {
        this.actionListener = onItemActionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Question question = mValues.get(position);
        refreshItemView(holder, question, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void refreshItemView(ViewHolder holder, Question question, int position) {
        holder.time.setText(TimeUtils.getFriendlyTimeSpanByNow(question.getDate()));
        //得到drawable对象，即所要插入的图片
        Drawable d = mContext.getResources().getDrawable(R.mipmap.ic_zan);
        holder.zanCount.setCompoundDrawablesWithIntrinsicBounds(d,
                null, null, null);
        holder.zanCount.setCompoundDrawablePadding(mContext.getResources().getDimensionPixelOffset(R.dimen.dp_4));
        holder.zanCount.setText(String.valueOf(question.getZan()));
        holder.zanCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener != null) {
                    actionListener.onHit(question, position);
                }
            }
        });
        //回复
        holder.commentCount.setText(String.valueOf(question.getComment_list().size()));
        holder.content.setText(question.getContent());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener != null) {
                    actionListener.onDetailClick(question, position);
                }
            }
        });

        List<QuestionAnswer> answers = question.getAnswer();
        if (answers != null && answers.size() > 0) {
            holder.answerGroup.removeAllViews();
            holder.answerGroup.setVisibility(View.VISIBLE);
            for (int i = 0; i < answers.size(); i++) {
                TextView textView = new TextView(mContext);
                textView.setTag(i);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                textView.setText(String.format("%d、%s", i + 1, answers.get(i).getAnswer_content()));
//                textView.setText(String.format("%d、%s(%s人选择)", i + 1, answers.get(i).getAnswer_content(),answers.get(i).getUser_ids().size()));
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.grey_700));
                textView.setLayoutParams(new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                holder.answerGroup.addView(textView);
            }
        } else {
            holder.answerGroup.setVisibility(View.GONE);
        }
        // TODO: 5/21/21 列表屏蔽正确答案，正确答案在详情里面去显示
        holder.publish_right_content.setVisibility(View.GONE);
//        if (question.getRight_answer_id() > 0) {
//            //表示记录的是正确答案id，去数据库中查这个答案即可
//            holder.publish_right_content.setVisibility(View.VISIBLE);
//            QuestionAnswer answer = LitePal.find(QuestionAnswer.class, question.getRight_answer_id());
//            holder.publish_right_content.setText("正确答案:" + answer.getAnswer_content());
//        } else {
//            if (StringUtils.isEmpty(question.getRight_answer())) {
//                holder.publish_right_content.setVisibility(View.GONE);
//            }else {
//                holder.publish_right_content.setVisibility(View.VISIBLE);
//                holder.publish_right_content.setText("正确答案:" + question.getRight_answer());
//            }
//        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView commentCount, zanCount;
        TextView content,publish_right_content;
        View itemLayout;
        RecyclerView recycler;
        LinearLayout answerGroup;

        public ViewHolder(View view) {
            super(view);
            itemLayout = view;
            time = itemView.findViewById(R.id.item_time);
            commentCount = itemView.findViewById(R.id.item_comment);
            zanCount = itemView.findViewById(R.id.item_zan);
            content = itemView.findViewById(R.id.item_content);
            recycler = itemView.findViewById(R.id.item_recycler);
            answerGroup = itemView.findViewById(R.id.answer_layout);
            publish_right_content = itemView.findViewById(R.id.publish_right_content);
        }
    }

    public interface OnActionListener {
        void onHit(Question item, int position);

        void onDetailClick(Question item, int position);

        void onAnswerSelect(Question item, int position, int childPosition);
    }

}