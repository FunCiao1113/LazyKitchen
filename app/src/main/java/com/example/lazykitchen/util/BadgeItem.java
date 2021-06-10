package com.example.lazykitchen.util;

public class BadgeItem {
    int badgeId;
    String description;

    public BadgeItem(int badgeId,String description) {
        this.badgeId = badgeId;
        this.description = description;
    }

    public int getBadgeId() {
        return badgeId;
    }

    public String getDescription() {
        return description;
    }
}
