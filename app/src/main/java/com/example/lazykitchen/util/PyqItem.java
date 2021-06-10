package com.example.lazykitchen.util;

import java.util.List;

public class PyqItem {
    String title;
    String content;
    String date;
    int headId;
    List<PhotoItem> photos;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public int getHeadId() {
        return headId;
    }

    public List<PhotoItem> getPhotos() {
        return photos;
    }

    public PyqItem(String title, String content, String date, int headId, List<PhotoItem> photos) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.headId = headId;
        this.photos = photos;
    }
}
