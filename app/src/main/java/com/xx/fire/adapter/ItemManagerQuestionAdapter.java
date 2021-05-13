package com.xx.fire.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.R;
import com.xx.fire.model.Question;
import com.xx.fire.model.QuestionAnswer;

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
                answer.setText(String.format("%d、%s", i + 1, answers.get(i).getAnswer_content()));
                answer.setTextColor(ContextCompat.getColor(mContext, R.color.grey_800));
                answer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                holder.answerGroup.addView(answer);
            }
        } else {
            holder.answerGroup.setVisibility(View.GONE);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
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
        }
    }

    public interface OnActionListener {
        void delete(Question item, int position);
    }

}