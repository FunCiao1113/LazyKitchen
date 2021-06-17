package com.example.lazykitchen.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.AdapterPyq;
import com.example.lazykitchen.util.PhotoItem;
import com.example.lazykitchen.util.PyqItem;

import java.util.ArrayList;
import java.util.List;

public class ShareFragment extends Fragment {

    private ShareViewModel mViewModel;
    private Context context;
    private List<PyqItem> pyqItems;
    private List<PhotoItem> photos;

    public static ShareFragment newInstance() {
        return new ShareFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.pyqStage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        initial();
        AdapterPyq adapterPyq = new AdapterPyq(pyqItems,photos);
        recyclerView.setAdapter(adapterPyq);
        return view;
    }

    private void initial(){
        photos = new ArrayList<>();/*
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));*/
        pyqItems = new ArrayList<>();
        pyqItems.add(new PyqItem("1111","11111","2020.1.1",R.drawable.ic_baseline_collections_bookmark_24,photos));
        pyqItems.add(new PyqItem("2222","22222","2020.1.1",R.drawable.ic_baseline_collections_bookmark_24,photos));
        pyqItems.add(new PyqItem("3333","33333","2020.1.1",R.drawable.ic_baseline_collections_bookmark_24,photos));
        pyqItems.add(new PyqItem("4444","44444","2020.1.1",R.drawable.ic_baseline_collections_bookmark_24,photos));
        pyqItems.add(new PyqItem("5555","55555","2020.1.1",R.drawable.ic_baseline_collections_bookmark_24,photos));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        // TODO: Use the ViewModel
    }

}