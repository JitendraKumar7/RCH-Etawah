package com.rch.etawah.ModelClass;

import java.io.Serializable;

public class CategoryGrid implements Serializable {
    String name,id ,varient_id,quantity,unit,mrp,price,description,varient_image;
    String  image;

    public String getVarient_id() {
        return varient_id;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVarient_image() {
        return varient_image;
    }

    public void setVarient_image(String varient_image) {
        this.varient_image = varient_image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
