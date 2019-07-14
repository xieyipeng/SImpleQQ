package com.example.xieyipeng.mineim.javaBean;

public class DynamicReceive {

    String headImage;
    String username;
    String context;
    String image;
    String time;

    public DynamicReceive(String headImage, String username, String context, String image, String time) {
        this.headImage = headImage;
        this.username = username;
        this.context = context;
        this.image = image;
        this.time = time;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
