package com.example.lazykitchen.util;

public class UploadInfo {
    String uploadAddress;
    String uploadAuth;

    public UploadInfo(String uploadAddress, String uploadAuth) {
        this.uploadAddress = uploadAddress;
        this.uploadAuth = uploadAuth;
    }

    public String getUploadAddress() {
        return uploadAddress;
    }

    public void setUploadAddress(String uploadAddress) {
        this.uploadAddress = uploadAddress;
    }

    public String getUploadAuth() {
        return uploadAuth;
    }

    public void setUploadAuth(String uploadAuth) {
        this.uploadAuth = uploadAuth;
    }
}
