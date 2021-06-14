package com.example.lazykitchen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.ActivityUtils;
import com.example.lazykitchen.util.Adapter;
import com.example.lazykitchen.util.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity {

    private List<VideoItem> videoList = new ArrayList<>();
    Adapter adapter;
    RecyclerView recyclerView;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        recyclerView = findViewById(R.id.recommend);
        initVideoView();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        Adapter adapter = new Adapter(videoList);
        recyclerView.setAdapter(adapter);

    }

    private void initVideoView() {
        for (int i = 0; i < 8; i++) {
            //VideoItem video1 = new VideoItem("111", R.mipmap.ic_launcher);
            //videoList.add(video1);
            //VideoItem video2 = new VideoItem("222", R.mipmap.ic_launcher);
            //videoList.add(video2);
        }
    }
}