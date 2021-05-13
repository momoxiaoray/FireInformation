package com.xx.fire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xx.fire.R;
import com.xx.fire.model.MediaData;

import java.io.File;
import java.util.List;

public class ItemChildPicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MediaData> mValues;
    private OnItemActionListener onItemActionListener;
    private Context context;

    public ItemChildPicAdapter(Context context, List<MediaData> items) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_media_list, parent, false);
        return new DataViewHolder(view, parent);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        MediaData mediaData = mValues.get(position);
        DataViewHolder viewHolder = (DataViewHolder) holder;
        RequestOptions options = new RequestOptions()
                .centerCrop();
        Glide.with(context)
                .load(new File(mediaData.getPath()))
                .apply(options)
                .into(viewHolder.item_img);
        if (mediaData.getType() == 1) {
            viewHolder.item_type.setVisibility(View.VISIBLE);
        } else {
            viewHolder.item_type.setVisibility(View.GONE);
        }
        viewHolder.item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemActionListener != null)
                    onItemActionListener.onItemClick(mediaData, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        public ImageView item_img, item_type;

        public DataViewHolder(View view, ViewGroup parent) {
            super(view);
            item_img = view.findViewById(R.id.item_img);
            item_type = view.findViewById(R.id.item_type);
        }
    }


    public interface OnItemActionListener {
        void onItemClick(MediaData data, int position);
    }
}