package com.example.lazykitchen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
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
    Intent intent;
    TextView videoTitle;
    TextView textContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        intent = getIntent();
        recyclerView = findViewById(R.id.recommend);
        initExtras();
        initVideoView();
        initVideoListView();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        Adapter adapter = new Adapter(videoList);
        recyclerView.setAdapter(adapter);
    }

    private void initExtras(){
        videoTitle=findViewById(R.id.videoTitle);
        textContent=findViewById(R.id.textContent);
        videoTitle.setText(intent.getStringExtra("video_name"));
        textContent.setText(intent.getStringExtra("description"));
    }

    private void initVideoView() {
        videoView = findViewById(R.id.video_view);
        MediaController mediaController=new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(intent.getStringExtra("play_url"));
        videoView.start();
    }

    private void initVideoListView() {
    }
}