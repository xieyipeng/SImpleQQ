package com.example.xieyipeng.mineim.javaBean;

public class UserFriend {
    private String name;
    private String headImg;
    private Boolean isOnline;


    public UserFriend(String name, String headImg, Boolean isOnline) {
        this.name = name;
        this.headImg = headImg;
        this.isOnline = isOnline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }
}
