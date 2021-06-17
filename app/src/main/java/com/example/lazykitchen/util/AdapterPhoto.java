package com.example.lazykitchen.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazykitchen.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.PhotoViewHolder>{

    List<PhotoItem> photos;
    View inflater;

    public AdapterPhoto(List<PhotoItem> photos) {
        this.photos = photos;
    }

    @NonNull
    @NotNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,parent,false);
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(inflater);
        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PhotoViewHolder holder, int position) {
        PhotoItem photoItem = photos.get(position);
        if(photoItem.getPhotoUrl()!=null) {
            holder.imageView.setImageURI(photoItem.getPhotoUrl());
        }else if(photoItem.getBitmap()!=null){
            holder.imageView.setImageBitmap(photoItem.getBitmap());
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public PhotoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo);
        }
    }
}
