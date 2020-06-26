package com.rch.etawah.ui;

import java.io.Serializable;

public class User implements Serializable {

    String user_id;
    String user_name;
    String user_email;
    String user_phone;
    String password;

    public String getUserId() {
        return user_id;
    }

    public String getUserName() {
        return user_name;
    }

    public String getUserEmail() {
        return user_email;
    }

    public String getUserPhone() {
        return user_phone;
    }

    public String getPassword() {
        return password;
    }

}
