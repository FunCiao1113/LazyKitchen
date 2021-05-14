package com.example.lazykitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initVideoView();
    }

    private void initVideoView() {
        videoView = findViewById(R.id.video_view);
        MediaController mediaController=new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath("https://outin-a57d677ab0aa11ebbe1400163e1c35d5.oss-cn-shanghai.aliyuncs.com/sv/22c94c36-17950bbb43c/22c94c36-17950bbb43c.mp4?Expires=1620979788&OSSAccessKeyId=LTAI4FfD63zoqnm6ckiBFfXZ&Signature=y2T0eybF1GO66ebwpGLPJYSSgmA%3D");
        videoView.start();
    }
}