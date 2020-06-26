package com.rch.etawah.ModelClass;

/**
 * Created by Rajesh Dabhi on 24/6/2017.
 */

public class Category_model {

    String cat_id;
    String title;

    String description;
    String image;

    String Count;

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }
}
