package com.example.lazykitchen.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazykitchen.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    private List<VideoItem> list;
    private Context context;
    private View inflater;


    public Adapter(List<VideoItem> videoList) {
        list = videoList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder( @NotNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        myViewHolder myViewHolder = new myViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder( @NotNull myViewHolder holder, int position) {
        VideoItem videoItem = list.get(position);
        holder.videoImage.setImageResource(videoItem.getImageId());
        holder.videoName.setText(videoItem.getName());

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
