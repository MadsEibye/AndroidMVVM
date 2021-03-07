package com.example.androidmvvm.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URI;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmvvm.Models.News;
import com.example.androidmvvm.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewSimpleAdapter<T> extends RecyclerView.Adapter<RecyclerViewSimpleAdapter<T>.ViewHolder> {
    private static final String LOG_TAG = "NEWS";
    private final List<News> data;
    private OnItemClickListener onItemClickListener;
    private final int viewId = View.generateViewId();

    public RecyclerViewSimpleAdapter(List<News> data) {
        this.data = data;
        Log.d(LOG_TAG, data.toString());
    }

    @NonNull
    @Override
    public RecyclerViewSimpleAdapter<T>.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News dataItem = data.get(position);
        Log.d(LOG_TAG, "onBindViewHolder " + data.toString());
        holder.Title.setText(dataItem.getTitle());
        holder.Title.setTextColor(Color.parseColor("#FFFFFF"));
        holder.Author.setText(dataItem.getAuthor());
        holder.Author.setTextColor(Color.parseColor("#FFFFFF"));
        holder.PublishedAt.setText(dataItem.getPublishedAt());
        holder.PublishedAt.setTextColor(Color.parseColor("#FFFFFF"));
        if (dataItem.getUrlToImage() == null) {
            dataItem.setUrlToImage("https://4.bp.blogspot.com/-OCutvC4wPps/XfNnRz5PvhI/AAAAAAAAEfo/qJ8P1sqLWesMdOSiEoUH85s3hs_vn97HACLcBGAsYHQ/s1600/no-image-found-360x260.png");
        }
        Uri myUri = Uri.parse(dataItem.getUrlToImage());
        try {
            Picasso.get().load(myUri).into(holder.mImg);
        } catch (Exception e) {
            Log.d("URISTRINGFAIL", myUri.toString());
        }
        Log.d(LOG_TAG, "onBindViewHolder called " + position);

    }
    @Override
    public int getItemCount() {
        int count = data.size();
        Log.d(LOG_TAG, "getItemCount called: " + count);
        return count;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T item);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Title;
        public TextView Author;
        public TextView PublishedAt;
        public ImageView mImg;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.title);
            Author = itemView.findViewById(R.id.author);
            PublishedAt = itemView.findViewById(R.id.publishedAt);
            mImg = (ImageView) itemView.findViewById(R.id.img_item);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition(), data.get(getAdapterPosition()));
            }
        }
    }
}

