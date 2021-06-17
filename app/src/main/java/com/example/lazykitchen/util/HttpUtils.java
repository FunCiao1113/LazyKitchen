package com.example.lazykitchen.util;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtils {
    public void getSyn(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //创建OkHttpClient对象
                    OkHttpClient client = new OkHttpClient();
                    //创建Request
                    Request request = new Request.Builder()
                            .url(url)//访问连接
                            .get()
                            .build();
                    //创建Call对象
                    Call call = client.newCall(request);
                    //通过execute()方法获得请求响应的Response对象
                    Response response = call.execute();
                    if (response.isSuccessful()) {
                        //处理网络请求的响应，处理UI需要在UI线程中处理
                        ResponseBody body = response.body();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
    public void getAsyn(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    //处理UI需要切换到UI线程处理
                }
            }
        });
    }
    */

    public void post(String url, Map<String,String> paraMap){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder=new FormBody.Builder();
        for(Map.Entry<String,String> e: paraMap.entrySet()) {
            builder.add(e.getKey(), e.getValue());
        }
        FormBody body=builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    //处理UI需要切换到UI线程处理
                }
            }
        });
    }

    public RequestBody json2RequestBody(String jsonStr){
        MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(jsonType, jsonStr);
    }
}
