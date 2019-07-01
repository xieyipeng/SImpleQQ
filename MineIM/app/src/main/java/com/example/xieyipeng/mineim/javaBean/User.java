package com.example.xieyipeng.mineim.javaBean;

public class User {
    String username;
    String password;
    String last_login;
    String first_name;
    String last_name;
    String email;
    String is_active;
    String data_joined;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){

    }

    public User(String username, String password, String last_login, String first_name, String last_name, String email, String is_active, String data_joined) {
        this.username = username;
        this.password = password;
        this.last_login = last_login;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.is_active = is_active;
        this.data_joined = data_joined;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getData_joined() {
        return data_joined;
    }

    public void setData_joined(String data_joined) {
        this.data_joined = data_joined;
    }
}
