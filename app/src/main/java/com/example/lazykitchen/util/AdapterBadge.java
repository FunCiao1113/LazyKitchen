package com.example.lazykitchen.util;

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

public class AdapterBadge extends RecyclerView.Adapter<AdapterBadge.BadgeViewHolder>{
    List<BadgeItem> badgeItems;
    View inflater;

    public AdapterBadge(List<BadgeItem> badgeItems) {
        this.badgeItems = badgeItems;
    }

    @NonNull
    @NotNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_item,parent,false);
        BadgeViewHolder badgeViewHolder= new BadgeViewHolder(inflater);
        return badgeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BadgeViewHolder holder, int position) {
        BadgeItem badgeItem = badgeItems.get(position);
        holder.textView.setText(badgeItem.getDescription());
        holder.imageView.setImageResource(badgeItem.getBadgeId());
    }


    @Override
    public int getItemCount() {
        return badgeItems.size();
    }


    class BadgeViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public BadgeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.badge);
        }
    }
}
