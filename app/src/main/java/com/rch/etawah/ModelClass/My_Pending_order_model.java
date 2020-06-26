package com.rch.etawah.ModelClass;

/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class My_Pending_order_model {

    String cart_id;
    String user_id;
    String delivery_date;
    String time_slot;
    String order_status;
    String note;
    String is_paid;
    String price;
    String total_kg;
    String total_items;
    String socity_id;
    String delivery_address;
    String location_id;
    String delivery_charge;
    String payment_method , payment_status;

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal_kg() {
        return total_kg;
    }

    public void setTotal_kg(String total_kg) {
        this.total_kg = total_kg;
    }

    public String getTotal_items() {
        return total_items;
    }

    public void setTotal_items(String total_items) {
        this.total_items = total_items;
    }

    public String getSocity_id() {
        return socity_id;
    }

    public void setSocity_id(String socity_id) {
        this.socity_id = socity_id;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
}
