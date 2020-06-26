package com.rch.etawah.ModelClass;

public class varient_product {

    String variant_id , variant_name , product_id, variant_price, variant_mrp, variant_unit ,variant_unit_value,varient_imqge;

    boolean ischeckd;

    public String getVariant_id() {
        return variant_id;
    }

    public String getVarient_imqge() {
        return varient_imqge;
    }

    public void setVarient_imqge(String varient_imqge) {
        this.varient_imqge = varient_imqge;
    }

    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }

    public String getVariant_name() {
        return variant_name;
    }

    public void setVariant_name(String variant_name) {
        this.variant_name = variant_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getVariant_price() {
        return variant_price;
    }

    public void setVariant_price(String variant_price) {
        this.variant_price = variant_price;
    }

    public String getVariant_mrp() {
        return variant_mrp;
    }

    public void setVariant_mrp(String variant_mrp) {
        this.variant_mrp = variant_mrp;
    }

    public String getVariant_unit() {
        return variant_unit;
    }

    public void setVariant_unit(String variant_unit) {
        this.variant_unit = variant_unit;
    }

    public String getVariant_unit_value() {
        return variant_unit_value;
    }

    public void setVariant_unit_value(String variant_unit_value) {
        this.variant_unit_value = variant_unit_value;
    }

    public boolean getIscheckd(){
        return ischeckd;
    }

    public void setIscheckd(boolean ischeckd){
        this.ischeckd = ischeckd;
    }
}
