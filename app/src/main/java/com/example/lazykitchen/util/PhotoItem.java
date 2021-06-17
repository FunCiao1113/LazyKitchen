package com.example.lazykitchen.util;

import android.graphics.Bitmap;
import android.net.Uri;

public class PhotoItem {
    Uri photoUrl;
    Bitmap bitmap;

    public PhotoItem(Uri photoUrl,Bitmap bitmap) {
        this.photoUrl = photoUrl;
        this.bitmap=bitmap;
    }

    public  Uri getPhotoUrl() {
        return photoUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
