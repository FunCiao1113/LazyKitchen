package com.example.lazykitchen.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.example.lazykitchen.R;
import com.example.lazykitchen.util.FileProviderUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class HeadActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO = 11;// 拍照
    private static final int CROP_PHOTO = 12;// 裁剪图片
    private static final int LOCAL_CROP = 13;// 本地图库

    private static final String HEAD_CHANGE= "head_change";//头像更新

    private ImageView imageView;
    private Button change;
    private Uri photoURI;
    VODUploadClient uploader;
    String uploadTitle;
    String uploadAuth;
    String uploadAddress;
    String uploadAddressAndAuthUrl="http://47.100.4.109:8080/user/head";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);
        setViews();// 初始化控件
        uploaderInit();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoOrSelectPicture();
            }
        });
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


    private void setViews() {
        change = findViewById(R.id.change);
        imageView = findViewById(R.id.headImage);
    }

    private void takePhotoOrSelectPicture() {
        CharSequence[] items = {"拍照", "图库"};// 裁剪items选项
        // 弹出对话框提示用户拍照或者是通过本地图库选择图片
        new AlertDialog.Builder(HeadActivity.this).setItems(items, new DialogInterface.OnClickListener() {
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
                                photoURI = FileProvider.getUriForFile(HeadActivity.this, "com.example.lazykitchen.fileprovider", photoFile);
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
                    // 创建intent用于裁剪图片
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    // 设置数据为文件uri，类型为图片格式
                    intent.setDataAndType(photoURI, "image/*");
                    // 允许裁剪
                    intent.putExtra("crop", "true");
                    intent.putExtra("scale", true);

                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 300);
                    intent.putExtra("outputY", 300);
                    intent.putExtra("return-data", false);

                    // 指定输出到文件uri中
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    intent.putExtra("noFaceDetection", true); // no face detection

                    // 授予intent读写权限
                    FileProviderUtils.grantUriPermission(this,intent,photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // 启动intent，开始裁剪
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case LOCAL_CROP:// 系统图库
                if (resultCode == RESULT_OK) {
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
                    intent1.putExtra("outputX", 300);
                    intent1.putExtra("outputY", 300);
                    // 裁剪后返回数据
                    intent1.putExtra("return-data", true);
                    // 启动intent，开始裁剪
                    startActivityForResult(intent1, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:// 裁剪后展示图片
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap=null;
                    try {
                        // 展示拍照后裁剪的图片
                        if (photoURI != null) {
                            // 创建BitmapFactory.Options对象
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            // 属性设置，用于压缩bitmap对象
                            option.inSampleSize = 2;
                            option.inPreferredConfig = Bitmap.Config.RGB_565;
                            // 根据文件流解析生成Bitmap对象
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoURI), null, option);
                            // 展示图片
                            imageView.setImageBitmap(bitmap);
                        }
                        // 展示图库中选择裁剪后的图片
                        // 裁剪成功不能直接进入此逻辑
                        else if (data != null) {
                            // 根据返回的data，获取Bitmap对象
                            bitmap = data.getExtras().getParcelable("data");
                            // 展示图片
                            imageView.setImageBitmap(bitmap);
                        }
                        File uploadPhotoFile = createImageFile();
                        if(uploadPhotoFile.exists()){
                            uploadPhotoFile.delete();}
                        try {
                            FileOutputStream fos = new FileOutputStream(uploadPhotoFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        uploadHead(uploadTitle,uploadPhotoFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
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

    public void uploadHead(String title,File photo){
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
                        Toast toast = Toast.makeText(HeadActivity.this, "上传失败！", Toast.LENGTH_SHORT);
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
                            Toast.makeText(HeadActivity.this,"上传成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HEAD_CHANGE);
                            intent.putExtra("imageUrl", uploadImageUri);
                            LocalBroadcastManager.getInstance(HeadActivity.this).sendBroadcast(intent);
                        }
                    });
                }
            }
        });
    }

    public void setPhotoURI(Uri uri){
        photoURI=uri;
    }
}