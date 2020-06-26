package com.rch.etawah.ModelClass;

import java.io.Serializable;

public class notice implements Serializable {

    public String content_id;
    public String content_name;
    public String content_date;

    public notice() {
        this.content_id = content_id;
        this.content_name = content_name;
        this.content_date = content_date;
    }


    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    public String getContent_date() {
        return content_date;
    }

    public void setContent_date(String content_date) {
        this.content_date = content_date;
    }
}
