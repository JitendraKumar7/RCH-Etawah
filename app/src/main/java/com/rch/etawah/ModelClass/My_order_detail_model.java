package com.rch.etawah.ModelClass;

/**
 * Created by Rajesh Dabhi on 30/6/2017.
 */

public class My_order_detail_model {

    String product_name;
    String varient_image;
    String qty;
    String description;
    String unit;
    String quantity;
    String order_cart_id , price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getVarient_image() {
        return varient_image;
    }

    public void setVarient_image(String varient_image) {
        this.varient_image = varient_image;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrder_cart_id() {
        return order_cart_id;
    }

    public void setOrder_cart_id(String order_cart_id) {
        this.order_cart_id = order_cart_id;
    }
}
