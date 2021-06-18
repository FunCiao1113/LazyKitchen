package com.example.lazykitchen.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.RequestBuilder;
import com.example.lazykitchen.R;
import com.example.lazykitchen.activity.HomeActivity;
import com.example.lazykitchen.util.AdapterPhoto;
import com.example.lazykitchen.activity.PhotoActivity;
import com.example.lazykitchen.util.AdapterPyq;
import com.example.lazykitchen.util.OnRecyclerPhotoClickListener;
import com.example.lazykitchen.util.PhotoItem;
import com.example.lazykitchen.util.PyqItem;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShareFragment extends Fragment {

    private ShareViewModel mViewModel;
    private Context context;
    private List<PyqItem> pyqItems;
    private List<List<PhotoItem>> photos;
    private static final String pyqUrlPrefix="http://47.100.4.109:8080/article";
    AdapterPyq adapterPyq;

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
        photos=new ArrayList<>();
        adapterPyq = new AdapterPyq();
        recyclerView.setAdapter(adapterPyq);
        return view;
    }

    private void initial(){
        photos = new ArrayList<>();

        /*photos.add(new PhotoItem(R.drawable.));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));*/
        pyqItems = new ArrayList<>();
        initPyqs();
    }

    public void initPyqs(){
        OkHttpClient client=new OkHttpClient();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        String id=prefs.getString("ID","1");
        String pyqUrl=pyqUrlPrefix+"?author_id="+id;
        Request request = new Request.Builder()
                .url(pyqUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(getContext(), "朋友圈更新失败", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    Map map=gson.fromJson(response.body().string(),Map.class);
                    // 所有朋友圈
                    /*ArrayList pyqs=(ArrayList)map.get("articles");
                    System.out.println(pyqs);
                    System.out.println(pyqs.get(0));
                    // 一条朋友圈
                    Map pyq=(Map)pyqs.get(0);
                    // 发布时间
                    System.out.println(pyq.get("create_time"));
                    System.out.println(pyq.get("ArticlePics"));
                    // 一条朋友圈的所有图
                    ArrayList pyqphotos= (ArrayList) pyq.get("ArticlePics");
                    System.out.println(pyqphotos.get(0));
                    // 一张朋友圈图
                    Map photo=(Map)pyqphotos.get(0);
                    System.out.println(photo.get("Url"));*/
                    ArrayList pyqs=(ArrayList)map.get("articles");
                    for(int i=0;i<pyqs.size();i++){
                        Map pyq=(Map)(pyqs.get(i));
                        String createTime= (String) pyq.get("create_time");
                        String content= (String) pyq.get("body");
                        ArrayList pyqphotos= (ArrayList) pyq.get("ArticlePics");
                        List<PhotoItem> pts=new ArrayList<>();
                        for(int j=0;j<pyqphotos.size();j++){
                            Map photo=(Map)pyqphotos.get(j);
                            pts.add(new PhotoItem(Uri.parse((String) photo.get("Url")),null));
                        }

                        PyqItem pyqItem=new PyqItem("",content,createTime.toString(),R.drawable.ic_baseline_camera_24,pts);
                        List<PyqItem> tmp1=adapterPyq.getPyqItems();
                        tmp1.add(pyqItem);
                        adapterPyq.setPyqItems(tmp1);

                        AdapterPhoto adapterPhoto=new AdapterPhoto();
                        adapterPhoto.setPhotos(pts);
                        List<AdapterPhoto> tmp2=adapterPyq.getMultiAdapterPhoto();
                        tmp2.add(adapterPhoto);
                        adapterPyq.setMultiAdapterPhoto(tmp2);

                    }

                    //处理UI需要切换到UI线程处理
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //adapterPyq.notifySon();
                            adapterPyq.notifyDataSetChanged();
                            // 更新逻辑写在这里
                            Toast toast = Toast.makeText(getContext(), "朋友圈已更新", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        // TODO: Use the ViewModel
    }

}