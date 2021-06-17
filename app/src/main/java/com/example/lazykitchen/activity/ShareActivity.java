package com.example.lazykitchen.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.example.lazykitchen.R;
import com.example.lazykitchen.util.AdapterPhoto;
import com.example.lazykitchen.util.FileProviderUtils;
import com.example.lazykitchen.util.PhotoItem;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShareActivity extends AppCompatActivity {

    VODUploadClient uploader;
    String uploadTitle;
    String uploadAuth;
    String uploadAddress;
    String uploadAddressAndAuthUrl="http://47.100.4.109:8080/article";

    private static final int TAKE_PHOTO = 11;// 拍照
    private static final int CROP_PHOTO = 12;// 裁剪图片
    private static final int LOCAL_CROP = 13;// 本地图库

    private ImageButton add;
    private Uri photoURI;
    private RecyclerView recyclerView;
    private List<PhotoItem> photos;
    private Button submit;
    private ImageButton cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        add = findViewById(R.id.add);
        submit = findViewById(R.id.submit);
        uploaderInit();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传

            }
        });
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.pyqPhoto);
        initial();
        AdapterPhoto adapterPhoto = new AdapterPhoto(photos);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapterPhoto);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photos.size()<9) {
                    takePhotoOrSelectPicture();
                }
                else{
                    Toast toast = Toast.makeText(ShareActivity.this, "最多上传9张图片", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void uploaderInit(){
        uploader = new VODUploadClientImpl(getApplicationContext());
        uploader.init(new VODUploadCallback() {
            public void onUploadSucceed(UploadFileInfo info) {
                //上传成功
            }
            public void onUploadFailed(UploadFileInfo info, String code, String message) {
                //上传失败
            }
            public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
                //上传进度
            }
            public void onUploadTokenExpired() {
                //上传凭证过期，需要调用刷新凭证接口。
            }
            public void onUploadRetry(String code, String message) {
                //重试回调
            }
            public void onUploadRetryResume() {
            }
            public void onUploadStarted(UploadFileInfo uploadFileInfo) {
                System.out.println("upload start");
                getUploadAddressAndAuth();
                try {
                    while(uploadAddress==null){
                        Thread.sleep(100);
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                uploader.setUploadAuthAndAddress(uploadFileInfo, uploadAuth, uploadAddress);
            }
        });
    }

    public void uploadPhoto(String title,File photo){
        String filePath = photo.getAbsolutePath();
        VodInfo vodInfo = new VodInfo();
        vodInfo.setTitle(title);
        vodInfo.setDesc("");
        vodInfo.setCateId(-1);
        List<String> tags=new ArrayList<>();
        tags.add("tag1");
        vodInfo.setTags(tags);
        uploader.addFile(filePath,vodInfo);
        uploader.start();
    }

    public void getUploadAddressAndAuth(){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder=new FormBody.Builder();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String id=prefs.getString("ID","1");
        builder.add("id", String.valueOf(id));
        FormBody body=builder.build();
        Request request = new Request.Builder()
                .url(uploadAddressAndAuthUrl)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast toast = Toast.makeText(ShareActivity.this, "发表失败！", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    Map map=gson.fromJson(response.body().string(),Map.class);
                    System.out.println(map.get("title").toString());
                    System.out.println(map.get("uploadAddress").toString());
                    System.out.println(map.get("uploadAuth").toString());
                    System.out.println(map.get("imageUrl").toString());

                    uploadTitle=map.get("title").toString();
                    uploadAddress=map.get("uploadAddress").toString();
                    uploadAuth=map.get("uploadAuth").toString();
                    String uploadImageUri = map.get("imageUrl").toString();

                    //处理UI需要切换到UI线程处理
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            /*Toast.makeText(HeadActivity.this,"签到成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HEAD_CHANGE);
                            intent.putExtra("imageUrl", uploadImageUri);*/
                            /*LocalBroadcastManager.getInstance(HeadActivity.this).sendBroadcast(intent);*/
                        }
                    });
                }
            }
        });
    }

    private void initial() {
        photos = new ArrayList<>();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    private void takePhotoOrSelectPicture() {
        CharSequence[] items = {"拍照", "图库"};// 裁剪items选项
        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
        new AlertDialog.Builder(ShareActivity.this).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    // 选择了拍照
                    case 0:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 创建文件保存拍照的图片
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                photoURI = FileProvider.getUriForFile(ShareActivity.this, "com.example.lazykitchen.fileprovider", photoFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                                startActivityForResult(intent, TAKE_PHOTO);
                            }
                        }
                        break;
                    // 调用系统图库
                    case 1:
                        // 创建Intent，用于打开手机本地图库选择图片
                        Intent intent1 = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // 启动intent打开本地图库
                        startActivityForResult(intent1, LOCAL_CROP);
                        break;
                }
            }
        }).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:// 拍照
                if (resultCode == RESULT_OK) {
                    // 拍照photo加入recycleView
                    PhotoItem photo=new PhotoItem(photoURI,null);
                    photos.add(photo);
                }
                break;
            case LOCAL_CROP:// 系统图库
                if (resultCode == RESULT_OK) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);// Explain to the user why we need to read the contacts
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    }
                    // 边界逻辑 拍照图片uri刷新为空
                    setPhotoURI(null);
                    // 创建intent用于裁剪图片
                    Intent intent1 = new Intent("com.android.camera.action.CROP");
                    // 获取图库所选图片的uri
                    Uri uri = data.getData();
                    intent1.putExtra("crop", "true");
                    intent1.putExtra("scale", true);
                    intent1.setDataAndType(uri, "image/*");
                    //  设置裁剪图片的宽高
                    intent1.putExtra("outputX", 480);
                    intent1.putExtra("outputY", 480);
                    // 裁剪后返回数据
                    intent1.putExtra("return-data", true);
                    // 启动intent，开始裁剪
                    startActivityForResult(intent1, CROP_PHOTO);
                }
                break;
             case CROP_PHOTO:// 系统图库裁剪后展示图片
                if (resultCode == RESULT_OK) {
                    try {
                        if (data != null) {
                            // 根据返回的data，获取Bitmap对象
                            Bitmap bitmap = data.getExtras().getParcelable("data");
                            PhotoItem photo=new PhotoItem(null,bitmap);
                            photos.add(photo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void setPhotoURI(Uri uri) {
        photoURI = uri;
    }
}