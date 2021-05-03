package com.xx.fire.adapter;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xx.fire.App;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.model.News;
import com.xx.fire.model.User;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemNewsAdapter extends RecyclerView.Adapter<ItemNewsAdapter.ViewHolder> {

    private final List<News> mValues;
    private OnItemActionListener onItemActionListener;

    public ItemNewsAdapter(List<News> items) {
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
                    .inflate(R.layout.layout_item_news, parent, false);
            return new ViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_news_fire, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        News news = mValues.get(position);
        holder.title.setText(news.getTitle());
        holder.content.setText(news.getContent());
        holder.date.setText(news.getDate());
        holder.scanCount.setText("浏览数:" + news.getScan_count());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemActionListener != null) {
                    onItemActionListener.onItemClick(news, position);
                }
            }
        });
        User user = UserUtil.getCurrentUser();
        List<User> userList = news.getUsers();
        if (userList != null && userList.contains(user)) {
            holder.collect.setImageResource(R.mipmap.ic_collect_ed);
        } else {
            holder.collect.setImageResource(R.mipmap.ic_collect_un);
        }
        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemActionListener != null) {
                    onItemActionListener.onItemCollectClick(news, position);
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
        public TextView title, content, date, scanCount;
        public ImageView collect;
        public ConstraintLayout layout;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_title);
            content = (TextView) view.findViewById(R.id.item_content);
            date = (TextView) view.findViewById(R.id.item_date);
            scanCount = (TextView) view.findViewById(R.id.item_scan_count);
            collect = view.findViewById(R.id.btn_collect);
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

        void onItemCollectClick(News news, int position);

    }
}