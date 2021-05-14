package com.xx.fire.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xx.fire.R;
import com.xx.fire.model.QuestionAnswer;

import java.util.List;

public class RightAnswerSelectAdapter extends RecyclerView.Adapter<RightAnswerSelectAdapter.ViewHolder> {
    private List<QuestionAnswer> answerList;
    private OnItemActionListener listener;

    public RightAnswerSelectAdapter(List<QuestionAnswer> answerList) {
        this.answerList = answerList;
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_answer_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionAnswer answer = answerList.get(position);
        holder.content.setText(String.format("%d、%s", position + 1, answer.getAnswer_content()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.delete(answer, position);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView content;
        public ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.item_content);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnItemActionListener {
        void delete(QuestionAnswer answer, int position);
    }

}

