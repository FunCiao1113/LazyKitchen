package com.example.lazykitchen.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.lazykitchen.R;
import com.example.lazykitchen.activity.VideoActivity;
import com.example.lazykitchen.util.Adapter;
import com.example.lazykitchen.util.GsonUtils;
import com.example.lazykitchen.util.OnRecyclerItemClickListener;
import com.example.lazykitchen.util.VideoItem;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SkillFragment extends Fragment {

    private SkillViewModel mViewModel;
    private List<VideoItem> videoList = new ArrayList<>();
    private static final String DefaultSkillUrl ="http://47.100.4.109:8080/tutorial";
    private static final String SearchSkillUrl ="http://47.100.4.109:8080/tutorial/search";
    Adapter adapter;
    RecyclerView recyclerView;
    LocalBroadcastManager broadcastManager;
    LocalReceiver localReceiver;
    private static final String SKILL_SEARCH = "skill_search";

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(context, "搜索成功", Toast.LENGTH_SHORT).show();
            String query=intent.getStringExtra("query");
            System.out.println(query);
            loadSearchSkill(query);
        }
    }

    public static SkillFragment newInstance() {
        return new SkillFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialVideo();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.skill_fragment, container, false);
        recyclerView = view.findViewById(R.id.stage2);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter=new Adapter(videoList);
        adapter.setRecyclerItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(int position, List<VideoItem> videoItemList) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra("author_id",videoItemList.get(position).getAuthorId());
                intent.putExtra("author_name",videoItemList.get(position).getAuthorName());
                intent.putExtra("video_name",videoItemList.get(position).getMaterialName());
                intent.putExtra("play_url",videoItemList.get(position).getPlayUrl());
                intent.putExtra("description",videoItemList.get(position).getDescription());
                intent.putExtra("width",videoItemList.get(position).getWidth());
                intent.putExtra("height",videoItemList.get(position).getHeight());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SKILL_SEARCH);
        localReceiver = new LocalReceiver();
        broadcastManager.registerReceiver(localReceiver, intentFilter);
        return view;
    }

    private void initialVideo() {
        loadDefaultSkill();
    }

    private void loadDefaultSkill() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(DefaultSkillUrl)
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
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "获取默认教程失败！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    Map map=gson.fromJson(response.body().string(),Map.class);
                    System.out.println(map.get("tutorials").toString());
                    videoList=GsonUtils.getResultList(gson.toJson(map.get("tutorials")),VideoItem.class);
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

    private void loadSearchSkill(String query) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SearchSkillUrl+"?query="+query)
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
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "获取搜索教程失败！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    Map map=gson.fromJson(response.body().string(),Map.class);
                    System.out.println(map.get("tutorials").toString());
                    videoList= GsonUtils.getResultList(gson.toJson(map.get("tutorials")),VideoItem.class);
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
        mViewModel = new ViewModelProvider(this).get(SkillViewModel.class);
        // TODO: Use the ViewModel
    }

}