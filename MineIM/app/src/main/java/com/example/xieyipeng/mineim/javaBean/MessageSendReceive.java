package com.example.xieyipeng.mineim.javaBean;

public class MessageSendReceive {
    private String type;
    private String my_type;
    private String message;
    private String room;
    private String send_to;
    private String my_sender;

    public MessageSendReceive(String type, String my_type, String message, String room, String send_to, String my_sender) {
        this.type = type;
        this.my_type = my_type;
        this.message = message;
        this.room = room;
        this.send_to = send_to;
        this.my_sender = my_sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMy_type() {
        return my_type;
    }

    public void setMy_type(String my_type) {
        this.my_type = my_type;
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

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
    }

    public String getMy_sender() {
        return my_sender;
    }

    public void setMy_sender(String my_sender) {
        this.my_sender = my_sender;
    }
}
