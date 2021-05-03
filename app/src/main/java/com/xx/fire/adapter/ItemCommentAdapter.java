package com.xx.fire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xx.fire.R;
import com.xx.fire.model.Comment;
import com.xx.fire.model.MediaData;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ItemCommentAdapter extends RecyclerView.Adapter<ItemCommentAdapter.DataViewHolder> {

    private final List<Comment> mValues;
    private OnItemActionListener onItemActionListener;
    private Context context;

    public ItemCommentAdapter(Context context, List<Comment> items) {
        mValues = items;
        this.context = context;
    }

    public OnItemActionListener getOnItemActionListener() {
        return onItemActionListener;
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener) {
        this.onItemActionListener = onItemActionListener;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_comment_list, parent, false);
        return new DataViewHolder(view, parent);
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, int position) {
        Comment comment = mValues.get(position);
        holder.name.setText(comment.getUser().getNickname());
        holder.comment.setText(comment.getComment_content());
        holder.date.setText(comment.getDate());
        //这里表示回复
        if (comment.getStyle() == 1) {
            holder.comment_base.setVisibility(View.VISIBLE);
            holder.comment_base.setText(String.format("@%s：%s", comment.getRecover_user(), comment.getRecover_content()));
        } else {
            holder.comment_base.setVisibility(View.GONE);
        }
        holder.btn_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemActionListener != null) {
                    onItemActionListener.onComment(comment, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView name, comment_base, comment, btn_add_comment, date;

        public DataViewHolder(View view, ViewGroup parent) {
            super(view);
            name = view.findViewById(R.id.item_name);
            comment_base = view.findViewById(R.id.item_content_base);
            comment = view.findViewById(R.id.item_content);
            btn_add_comment = view.findViewById(R.id.item_btn_add_comment);
            date = view.findViewById(R.id.item_date);
        }
    }


    public interface OnItemActionListener {
        void onComment(Comment comment, int position);
    }
}