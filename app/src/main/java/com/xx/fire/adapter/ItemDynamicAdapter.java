package com.xx.fire.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.activity.PicShowActivity;
import com.xx.fire.model.Comment;
import com.xx.fire.model.Dynamic;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.News;
import com.xx.fire.model.User;

import java.io.Serializable;
import java.util.List;

public class ItemDynamicAdapter extends RecyclerView.Adapter<ItemDynamicAdapter.ViewHolder> {

    private final List<Dynamic> mValues;
    private OnActionListener actionListener;
    private Context mContext;

    public ItemDynamicAdapter(Context context, List<Dynamic> items) {
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
        Dynamic dynamic = mValues.get(position);
        refreshItemView(holder, dynamic, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void refreshItemView(ViewHolder holder, Dynamic dynamic, int position) {
        holder.name.setText(dynamic.getUser().getNickname());
        holder.time.setText(TimeUtils.getFriendlyTimeSpanByNow(dynamic.getDate()));
        //得到drawable对象，即所要插入的图片
        Drawable d = mContext.getResources().getDrawable(R.mipmap.ic_zan);
        holder.zanCount.setCompoundDrawablesWithIntrinsicBounds(d,
                null, null, null);
        holder.zanCount.setCompoundDrawablePadding(mContext.getResources().getDimensionPixelOffset(R.dimen.dp_4));
        holder.zanCount.setText(String.valueOf(dynamic.getZan()));
        holder.zanCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener != null) {
                    actionListener.onHit(dynamic, position);
                }
            }
        });
        //回复
        holder.commentCount.setText(String.valueOf(dynamic.getComment_list().size()));
        holder.content.setText(dynamic.getDynamic_content());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener != null) {
                    actionListener.onDetailClick(dynamic, position);
                }
            }
        });
        List<MediaData> mediaData = dynamic.getMedia_list();
        if (mediaData == null || mediaData.size() == 0) {
            holder.recycler.setVisibility(View.GONE);
            return;
        }
        holder.recycler.setVisibility(View.VISIBLE);
        holder.recycler.setLayoutManager(new GridLayoutManager(mContext, 3));
        ItemChildPicAdapter adapter = new ItemChildPicAdapter(mContext, mediaData);
        adapter.setOnItemActionListener(new ItemChildPicAdapter.OnItemActionListener() {
            @Override
            public void onItemClick(MediaData data, int i) {
                if (data.getType() == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putSerializable("list", (Serializable) mValues.get(position).getMedia_list());
                    ActivityUtils.startActivity(bundle, PicShowActivity.class);
                }
            }
        });
        holder.recycler.setAdapter(adapter);
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
        void onHit(Dynamic item, int position);

        void onDetailClick(Dynamic item, int position);

    }

}