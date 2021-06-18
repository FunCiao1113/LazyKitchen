package com.example.lazykitchen.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.ActivityUtils;
import com.example.lazykitchen.util.Adapter;
import com.example.lazykitchen.util.OnRecyclerItemClickListener;
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
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        System.out.println(widthPixels+","+heightPixels);
        intent = getIntent();
        // 添加自适应方法
        long width = intent.getLongExtra("width",widthPixels);
        long height = intent.getLongExtra("height",300);
        System.out.println(width+","+height);
        double ratio1 = 1.0 * widthPixels/width;
        double ratio2 = 1.0 * width/height;
        LinearLayout videoContainer = findViewById(R.id.videoContainer);
        ViewGroup.LayoutParams lp = videoContainer.getLayoutParams();
        lp.width = widthPixels;
        lp.height = (int)(height*ratio1);
        if (lp.height>1440){
            lp.height = 1440;
            lp.width = (int)((heightPixels) * ratio2);
        }
        System.out.println(lp.width+","+lp.height);
        System.out.println(height*ratio1);
        videoContainer.setLayoutParams(lp);
        ImageButton share = findViewById(R.id.finish);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoActivity.this,ShareActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.recommend);
        initExtras();
        initVideoView();
        initVideoListView();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new Adapter(videoList);
        adapter.setRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(int Position, List<VideoItem> videoItemList) {
                Intent intent = new Intent(VideoActivity.this, VideoActivity.class);
                intent.putExtra("author_id",videoItemList.get(Position).getAuthorId());
                intent.putExtra("author_name",videoItemList.get(Position).getAuthorName());
                intent.putExtra("video_name",videoItemList.get(Position).getMaterialName());
                intent.putExtra("play_url",videoItemList.get(Position).getPlayUrl());
                intent.putExtra("description",videoItemList.get(Position).getDescription());
                intent.putExtra("width",videoItemList.get(Position).getWidth());
                intent.putExtra("height",videoItemList.get(Position).getHeight());
                startActivity(intent);
            }
        });
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