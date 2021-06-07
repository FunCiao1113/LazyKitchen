package com.example.lazykitchen.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.Adapter;
import com.example.lazykitchen.util.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class SkillFragment extends Fragment {

    private SkillViewModel mViewModel;
    private List<VideoItem> videoList = new ArrayList<>();

    public static SkillFragment newInstance() {
        return new SkillFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skill_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.stage2);
        initialVideo();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        Adapter adapter = new Adapter(videoList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initialVideo() {
        for (int i = 0; i < 8; i++) {
            VideoItem video1 = new VideoItem("111", i, R.drawable.ic_baseline_collections_bookmark_24);
            videoList.add(video1);
            VideoItem video2 = new VideoItem("222", i, R.drawable.ic_baseline_collections_bookmark_24);
            videoList.add(video2);
            VideoItem video3 = new VideoItem("333", i, R.drawable.ic_baseline_collections_bookmark_24);
            videoList.add(video3);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SkillViewModel.class);
        // TODO: Use the ViewModel
    }

}