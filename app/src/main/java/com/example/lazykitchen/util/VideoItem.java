package com.example.lazykitchen.util;

public class VideoItem {
    private String name;
    private int id;
    private int imageId;

    public VideoItem(String name, int id, int imageId) {
        this.name = name;
        this.id = id;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return  id;
    }

    public int getImageId() {
        return imageId;
    }
}
