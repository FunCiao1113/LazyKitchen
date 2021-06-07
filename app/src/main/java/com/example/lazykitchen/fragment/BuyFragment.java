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

public class BuyFragment extends Fragment {

    private BuyViewModel mViewModel;
    private List<VideoItem> videoList = new ArrayList<>();

    public static BuyFragment newInstance() {
        return new BuyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.stage3);
        initialVideo();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        Adapter adapter = new Adapter(videoList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initialVideo(){
        for(int i=0;i<8;i++){
            VideoItem video1 = new VideoItem("111",i, R.mipmap.ic_launcher);
            videoList.add(video1);
            VideoItem video2 = new VideoItem("222",i, R.mipmap.ic_launcher);
            videoList.add(video2);
            VideoItem video3 = new VideoItem("333",i, R.mipmap.ic_launcher);
            videoList.add(video3);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuyViewModel.class);
        // TODO: Use the ViewModel
    }

}