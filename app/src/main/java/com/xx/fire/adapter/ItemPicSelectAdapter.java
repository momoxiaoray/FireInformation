package com.xx.fire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.IntentUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xx.fire.R;
import com.xx.fire.UserUtil;
import com.xx.fire.model.MediaData;
import com.xx.fire.model.News;
import com.xx.fire.model.User;

import java.io.File;
import java.util.List;

public class ItemPicSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MediaData> mValues;
    private OnItemActionListener onItemActionListener;
    private Context context;

    public ItemPicSelectAdapter(Context context, List<MediaData> items) {
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
    public int getItemViewType(int position) {
        //-1表示站位图添加图，其他表示数据图
        return mValues.get(position).getType() == -1 ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_add_media, parent, false);
            return new AddViewHolder(view, parent);
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_media_show, parent, false);
        return new DataViewHolder(view, parent);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddViewHolder) {
            AddViewHolder viewHolder = (AddViewHolder) holder;
            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemActionListener != null)
                        onItemActionListener.onAdd();
                }
            });
        } else {
            MediaData mediaData = mValues.get(position);
            DataViewHolder viewHolder = (DataViewHolder) holder;
            RequestOptions options = new RequestOptions()
                    .centerCrop();
            Glide.with(context)
                    .load(new File(mediaData.getPath()))
                    .apply(options)
                    .into(viewHolder.item_img);
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemActionListener != null)
                        onItemActionListener.onItemDelete(mediaData, position);
                }
            });
            viewHolder.item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemActionListener != null)
                        onItemActionListener.onItemClick(mediaData, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {
        public ImageView add;

        public AddViewHolder(@NonNull View itemView, ViewGroup parent) {
            super(itemView);
            add = itemView.findViewById(R.id.btn_add);
        }
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_img, delete;

        public DataViewHolder(View view, ViewGroup parent) {
            super(view);
            item_img = view.findViewById(R.id.item_img);
            delete = view.findViewById(R.id.delete);
        }
    }


    public interface OnItemActionListener {
        void onAdd();

        void onItemClick(MediaData data, int position);

        void onItemDelete(MediaData data, int position);
    }
}