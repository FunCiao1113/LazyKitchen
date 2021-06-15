package com.example.lazykitchen.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lazykitchen.R;
import com.example.lazykitchen.activity.VideoActivity;
import com.example.lazykitchen.util.Adapter;
import com.example.lazykitchen.util.GsonUtils;
import com.example.lazykitchen.util.OnRecyclerItemClickListener;
import com.example.lazykitchen.util.VideoItem;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BuyFragment extends Fragment {

    private BuyViewModel mViewModel;
    private List<VideoItem> videoList = new ArrayList<>();
    private static final String DefaultBuyUrl ="http://47.100.4.109:8080/choice";
    Adapter adapter;
    RecyclerView recyclerView;

    public static BuyFragment newInstance() {
        return new BuyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buy_fragment, container, false);
        recyclerView = view.findViewById(R.id.stage3);
        initialVideo();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new Adapter(videoList);
        adapter.setRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(int position, List<VideoItem> videoItemList) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra("author_id",videoItemList.get(position).getAuthorId());
                intent.putExtra("author_name",videoItemList.get(position).getAuthorName());
                intent.putExtra("video_name",videoItemList.get(position).getMaterialName());
                intent.putExtra("play_url",videoItemList.get(position).getPlayUrl());
                intent.putExtra("description",videoItemList.get(position).getDescription());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initialVideo(){
        loadDefaultBuy();
    }

    private void loadDefaultBuy() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(DefaultBuyUrl)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "获取默认选购失败！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    Map map=gson.fromJson(response.body().string(),Map.class);
                    System.out.println(map.get("choices").toString());
                    videoList= GsonUtils.getResultList(gson.toJson(map.get("choices")),VideoItem.class);
                    //处理UI需要切换到UI线程处理
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            adapter.setList(videoList);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuyViewModel.class);
        // TODO: Use the ViewModel
    }

}