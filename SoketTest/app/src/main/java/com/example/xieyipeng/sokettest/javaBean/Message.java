package com.example.xieyipeng.sokettest.javaBean;

public class Message {

    public static final String MESSAGE_TYPE_WW = "web-web";
    public static final String MESSAGE_TYPE_WQ_NO = "web-qq-no";
    public static final String MESSAGE_TYPE_WQ_OK = "web-qq-ok";
    public static final String MESSAGE_TYPE_QW = "qq-web";
    public static final String MESSAGE_TYPE_QQ = "qq-qq";



    private String type;
    private String my_type;
    private String message;
    private String room;

    public Message(String my_type, String message, String room) {
        this.type = "chat_message";
        this.message = message;
        this.room = room;
        this.my_type = my_type;
    }

    public Message(){

    }

    public String getMy_type() {
        return my_type;
    }

    public void setMy_type(String my_type) {
        this.my_type = my_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
