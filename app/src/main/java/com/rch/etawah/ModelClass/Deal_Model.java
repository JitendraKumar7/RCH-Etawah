package com.rch.etawah.ModelClass;

import java.io.Serializable;

public class Deal_Model implements Serializable {

    public String pId;
    public String pNAme;
    public String pDes;
    public String pQuan;
    public String pPrice;

    public String getVarient_id() {
        return varient_id;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }

    public String discountOff;
    public String pImage;
    public String pMrp;

    String category_id;
    String start_date;
    String start_time;
    String end_date;
    String end_time;
    String status;
    String in_stock;
    String unit_value;
    String unit;
    String increament;
    String rewards;
    String stock;
    public  String varient_id;



    public Deal_Model(String s, String lorem_ipsum, String s3, String s2, String s1, String splashicon, String discountOff, String pMrp) {
        this.pImage=splashicon;
        this.pNAme=lorem_ipsum;
        this.pId=s;
        this.pQuan=s1;
        this.pPrice=s2;
        this.pDes=s3;
        this.discountOff=discountOff;
        this.pMrp=pMrp;
    }

    public Deal_Model(String product_id, String product_name, String description, String s, String s1, String product_image, String s2, String s3, String count,String unit) {
        this.pImage=product_image;
        this.pNAme=product_name;
        this.pId=product_id;
        this.pQuan=s1;
        this.pPrice=s;
        this.pDes=description;
        this.discountOff=s2;
        this.pMrp=s3;
        this.stock=count;
        this.unit=unit;
    }

    public String getpMrp() {
        return pMrp;
    }

    public void setpMrp(String pMrp) {
        this.pMrp = pMrp;
    }
    public String getDiscountOff() {
        return discountOff;
    }

    public void setDiscountOff(String discountOff) {
        this.discountOff = discountOff;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpNAme() {
        return pNAme;
    }

    public void setpNAme(String pNAme) {
        this.pNAme = pNAme;
    }

    public String getpDes() {
        return pDes;
    }

    public void setpDes(String pDes) {
        this.pDes = pDes;
    }

    public String getpQuan() {
        return pQuan;
    }

    public void setpQuan(String pQuan) {
        this.pQuan = pQuan;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
    public String getIncreament() {
        return increament;
    }

    public void setIncreament(String increament) {
        this.increament = increament;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }


    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(String in_stock) {
        this.in_stock = in_stock;
    }

    public String getUnit_value() {
        return unit_value;
    }

    public void setUnit_value(String unit_value) {
        this.unit_value = unit_value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }
}

