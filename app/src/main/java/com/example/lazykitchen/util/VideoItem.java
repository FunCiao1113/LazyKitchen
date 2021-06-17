package com.example.lazykitchen.util;

import com.google.gson.annotations.SerializedName;

public class VideoItem {

    @SerializedName("id")
    private int id;
    @SerializedName("author_id")
    private String authorId;
    @SerializedName("author_name")
    private String authorName;

    private String description;
    @SerializedName("create_time")
    private String createTime;

    @SerializedName("material_id")
    private String materialId;
    @SerializedName("material_type")
    private String materialType;
    @SerializedName("material_name")
    private String materialName;

    @SerializedName("material_url")
    private String picUrl;

    @SerializedName("material_uri")
    private String video_uri;
    @SerializedName("cover_url")
    private String coverUrl;
    @SerializedName("video_url")
    private String playUrl;

    @SerializedName("width")
    private long width;
    @SerializedName("height")
    private long height;

    public VideoItem(int id, String authorId, String authorName, String description, String createTime, String materialId, String materialType, String materialName, String picUrl, String video_uri, String coverUrl, String playUrl, long width, long height) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.description = description;
        this.createTime = createTime;
        this.materialId = materialId;
        this.materialType = materialType;
        this.materialName = materialName;
        this.picUrl = picUrl;
        this.video_uri = video_uri;
        this.coverUrl = coverUrl;
        this.playUrl = playUrl;
        this.width = width;
        this.height = height;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateAt() {
        return createTime;
    }

    public void setCreateAt(String createAt) {
        this.createTime = createAt;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }
}
