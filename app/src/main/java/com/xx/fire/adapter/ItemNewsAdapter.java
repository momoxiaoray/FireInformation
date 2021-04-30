package com.xx.fire.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.model.News;
import com.xx.fire.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemNewsAdapter extends RecyclerView.Adapter<ItemNewsAdapter.ViewHolder> {

    private final List<News> mValues;

    public ItemNewsAdapter(List<News> items) {
        mValues = items;
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
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_item_news_fire, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        News news = mValues.get(position);
        holder.title.setText(news.getTitle());
        holder.content.setText(news.getContent());
        holder.date.setText(news.getDate());
        holder.scanCount.setText("浏览数:" + news.getScan_count());
        User user = UserUtil.getCurrentUser();
        if (news.getUsers() != null && news.getUsers().contains(user)) {
            holder.collect.setImageResource(R.mipmap.ic_collect_ed);
        } else {
            holder.collect.setImageResource(R.mipmap.ic_collect_un);
        }
        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = UserUtil.getCurrentUser();
                News news = mValues.get(position);
                List<User> userList = news.getUsers();
                if (userList == null) {
                    userList = new ArrayList<>();
                    userList.add(user);
                } else {
                    if (userList.contains(user)) {
                        userList.remove(user);
                    } else {
                        userList.add(user);
                    }
                }
                news.setUsers(userList);
                news.save();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, date, scanCount;
        public ImageView collect;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_title);
            content = (TextView) view.findViewById(R.id.item_content);
            date = (TextView) view.findViewById(R.id.item_date);
            scanCount = (TextView) view.findViewById(R.id.item_scan_count);
            collect = view.findViewById(R.id.btn_collect);
        }
    }
}