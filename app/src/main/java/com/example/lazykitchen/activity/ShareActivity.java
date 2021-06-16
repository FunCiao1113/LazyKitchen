package com.example.lazykitchen.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.lazykitchen.R;
import com.example.lazykitchen.util.AdapterPhoto;
import com.example.lazykitchen.util.FileProviderUtils;
import com.example.lazykitchen.util.PhotoItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    private void initial() {
        photos = new ArrayList<>();
        /*
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));
        photos.add(new PhotoItem(R.drawable.ic_baseline_camera_24));*/
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