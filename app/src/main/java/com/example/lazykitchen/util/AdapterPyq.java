package com.example.lazykitchen.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.lazykitchen.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterPyq extends RecyclerView.Adapter<AdapterPyq.PyqViewHolder>{

    List<PyqItem> pyqItems;
    List<PhotoItem> photoItems;
    View inflater;
    public AdapterPhoto adapterPhoto;

    public AdapterPyq(List<PyqItem> pyqItems, List<PhotoItem> photoItems) {
        this.pyqItems = pyqItems;
        this.photoItems = photoItems;
    }

    @NonNull
    @NotNull
    @Override
    public PyqViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.pyq_item,parent,false);
        PyqViewHolder pyqViewHolder = new PyqViewHolder(inflater);
        return pyqViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PyqViewHolder holder, int position) {
        PyqItem pyqItem = pyqItems.get(position);
        holder.content.setText(pyqItem.getContent());
        holder.date.setText(pyqItem.getDate());
        holder.title.setText(pyqItem.getTitle());
        holder.imageView.setImageResource(pyqItem.getHeadId());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapterPhoto = new AdapterPhoto(photoItems);
        holder.recyclerView.setAdapter(adapterPhoto);
    }

    @Override
    public int getItemCount() {
        return pyqItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class PyqViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView title;
        TextView content;
        TextView date;
        RecyclerView recyclerView;
        ImageButton button;
        public PyqViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.head);
            title = itemView.findViewById(R.id.title_name);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            recyclerView = itemView.findViewById(R.id.photoRecyclerView);
            button= itemView.findViewById(R.id.shareButton);
        }
    }
}
