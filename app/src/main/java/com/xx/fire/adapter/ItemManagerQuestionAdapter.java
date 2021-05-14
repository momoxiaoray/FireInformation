package com.xx.fire.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.xx.fire.R;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;

import org.litepal.LitePal;

import java.util.List;

public class ItemManagerQuestionAdapter extends RecyclerView.Adapter<ItemManagerQuestionAdapter.ViewHolder> {

    private final List<Question> mValues;
    private OnActionListener actionListener;
    private Context mContext;

    public ItemManagerQuestionAdapter(Context context, List<Question> items) {
        mValues = items;
        mContext = context;
    }

    public void setOnItemActionListener(OnActionListener onItemActionListener) {
        this.actionListener = onItemActionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_question_manger, parent, false);
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
        holder.content.setText("问题内容：" + question.getContent());
        holder.time.setText(question.getDate());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener != null) {
                    actionListener.delete(question, position);
                }
            }
        });
        List<QuestionAnswer> answers = question.getAnswer();
        if (answers != null && answers.size() > 0) {
            holder.answerGroup.removeAllViews();
            holder.answerGroup.setVisibility(View.VISIBLE);
            for (int i = 0; i < answers.size(); i++) {
                TextView answer = new TextView(mContext);
                answer.setTag(i);
                answer.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                answer.setText(String.format("%d、%s", i + 1, answers.get(i).getAnswer_content()));
                answer.setTextColor(ContextCompat.getColor(mContext, R.color.grey_800));
                answer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                holder.answerGroup.addView(answer);
            }
        } else {
            holder.answerGroup.setVisibility(View.GONE);
        }

        if (question.getRight_answer_id() > 0) {
            //表示记录的是正确答案id，去数据库中查这个答案即可
            holder.publish_right_content.setVisibility(View.VISIBLE);
            QuestionAnswer answer = LitePal.find(QuestionAnswer.class, question.getRight_answer_id());
            holder.publish_right_content.setText("正确答案:" + answer.getAnswer_content());
        } else {
            if (StringUtils.isEmpty(question.getRight_answer())) {
                holder.publish_right_content.setVisibility(View.GONE);
            }else {
                holder.publish_right_content.setVisibility(View.VISIBLE);
                holder.publish_right_content.setText("正确答案:" + question.getRight_answer());
            }
        }

        holder.publish_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener != null) {
                    actionListener.selectRight(question, position);
                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, publish_right, publish_right_content;
        TextView content;
        View itemLayout;
        LinearLayout answerGroup;
        ImageView delete;

        public ViewHolder(View view) {
            super(view);
            itemLayout = view;
            time = itemView.findViewById(R.id.item_time);
            content = itemView.findViewById(R.id.item_content);
            answerGroup = itemView.findViewById(R.id.answer_layout);
            delete = itemView.findViewById(R.id.btn_delete);
            publish_right = itemView.findViewById(R.id.publish_right);
            publish_right_content = itemView.findViewById(R.id.publish_right_content);
        }
    }

    public interface OnActionListener {
        void delete(Question item, int position);

        void selectRight(Question item, int position);
    }

}