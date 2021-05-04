package com.xx.fire.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.R;
import com.xx.fire.activity.PicShowActivity;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.Question;

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
                .inflate(R.layout.layout_item_dynamic, parent, false);
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
        holder.name.setText(question.getUser().getNickname());
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
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView time;
        TextView commentCount, zanCount;
        TextView content;
        View itemLayout;
        RecyclerView recycler;

        public ViewHolder(View view) {
            super(view);
            itemLayout = view;
            icon = itemView.findViewById(R.id.item_icon);
            name = itemView.findViewById(R.id.item_name);
            time = itemView.findViewById(R.id.item_time);
            commentCount = itemView.findViewById(R.id.item_comment);
            zanCount = itemView.findViewById(R.id.item_zan);
            content = itemView.findViewById(R.id.item_content);
            recycler = itemView.findViewById(R.id.item_recycler);
        }
    }

    public interface OnActionListener {
        void onHit(Question item, int position);

        void onDetailClick(Question item, int position);

    }

}