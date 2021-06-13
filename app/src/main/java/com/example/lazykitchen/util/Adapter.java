package com.example.lazykitchen.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.lazykitchen.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    private List<VideoItem> list;
    private Context context;
    private View inflater;
    myViewHolder myViewHolder;

    public Adapter(List<VideoItem> videoList) {
        list = videoList;
    }

    public void setList(List<VideoItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder( @NotNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        myViewHolder = new myViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder( @NotNull myViewHolder holder, int position) {
        VideoItem videoItem = list.get(position);
        Uri url=Uri.parse(videoItem.getCoverUrl());
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.baseline_edit_off_red_300_24dp)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(720, 1080);
        Glide.with(myViewHolder.videoView.getContext())
                .load(url)
                .apply(options)
                .fitCenter()
                .into(holder.videoImage);
        holder.videoName.setText(videoItem.getMaterialName());
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        View videoView;
        ImageView videoImage;
        TextView videoName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView;
            videoImage = itemView.findViewById(R.id.imageView);
            videoName = itemView.findViewById(R.id.textView);
        }
    }
}
