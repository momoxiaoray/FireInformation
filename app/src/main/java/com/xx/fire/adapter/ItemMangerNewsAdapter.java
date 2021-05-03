package com.xx.fire.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xx.fire.App;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.model.News;
import com.xx.fire.model.User;

import java.io.File;
import java.util.List;

public class ItemMangerNewsAdapter extends RecyclerView.Adapter<ItemMangerNewsAdapter.ViewHolder> {

    private final List<News> mValues;
    private OnItemActionListener onItemActionListener;

    public ItemMangerNewsAdapter(List<News> items) {
        mValues = items;
    }

    public OnItemActionListener getOnItemActionListener() {
        return onItemActionListener;
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener) {
        this.onItemActionListener = onItemActionListener;
    }

    @Override
    public int getItemViewType(int position) {
        //0表示消防知识，1表示火灾新闻
        return mValues.get(position).getNews_type();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_item_manger_news, parent, false);
            return new ViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_manager_news_fire, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        News news = mValues.get(position);
        holder.title.setText(news.getTitle());
        holder.content.setText(news.getContent());
        holder.date.setText(news.getDate());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemActionListener != null) {
                    onItemActionListener.onItemClick(news, position);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemActionListener != null) {
                    onItemActionListener.onItemDeleteClick(news, position);
                }
            }
        });

        if (holder instanceof DataViewHolder) {
            Glide.with(App.getInstance())
                    .load(new File(news.getCoverPath()))
                    .into(((DataViewHolder) holder).item_pic);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, date;
        public ImageView delete;
        public ConstraintLayout layout;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_title);
            content = (TextView) view.findViewById(R.id.item_content);
            date = (TextView) view.findViewById(R.id.item_date);
            delete = view.findViewById(R.id.btn_delete);
            layout = view.findViewById(R.id.layout);
        }
    }


    public class DataViewHolder extends ViewHolder {
        public ImageView item_pic;

        public DataViewHolder(View view) {
            super(view);
            item_pic = view.findViewById(R.id.item_pic);
        }
    }


    public interface OnItemActionListener {
        void onItemClick(News news, int position);

        void onItemDeleteClick(News news, int position);

    }
}