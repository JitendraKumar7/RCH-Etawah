package com.rch.etawah.ModelClass;

import org.json.JSONArray;

public class HomeCate {
    String name,id,detail;
    String images;

    JSONArray sub_array = new JSONArray();

    public JSONArray getSub_array() {
        return sub_array;
    }

    public void setSub_array(JSONArray sub_array) {
        this.sub_array = sub_array;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
