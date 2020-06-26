package com.rch.etawah.ui.modal;

import android.os.Parcel;
import android.os.Parcelable;

import com.rch.etawah.R;

import java.io.Serializable;

public class CartHome implements Parcelable {

    private String mrp;

    private String unit;

    private String price;

    private String quantity;

    private String varient_id;

    private String product_id;

    private String description;

    private String product_name;

    private String varient_image;

    private String product_image;

    protected CartHome(Parcel in) {
        mrp = in.readString();
        unit = in.readString();
        price = in.readString();
        quantity = in.readString();
        varient_id = in.readString();
        product_id = in.readString();
        description = in.readString();
        product_name = in.readString();
        varient_image = in.readString();
        product_image = in.readString();
        cartValue = in.readInt();
        addInCart = in.readByte() != 0;
    }

    public String getMrp() {
        return mrp;
    }

    public String getUnit() {
        return unit;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getVarientId() {
        return varient_id;
    }

    public String getProductId() {
        return product_id;
    }

    public String getDescription() {
        return description;
    }

    public String getProductName() {
        return product_name;
    }

    public String getVarientImage() {
        return varient_image;
    }

    public String getProductImage() {
        return product_image;
    }

    private int cartValue = 1;
    private boolean addInCart = false;

    public int getCartValue() {

        return cartValue;
    }

    public double getCartPrice() {

        return cartValue * Double.parseDouble(price);
    }

    public boolean isAddInCart() {

        return addInCart;
    }

    public String getDiscountOff() {
        int totalOff = Integer.parseInt(mrp) - Integer.parseInt(price);
        return "\u20B9 " + totalOff + " " + "Off";
    }

    public CartHome setCartValue(int cartValue) {

        this.cartValue = cartValue;
        return this;

    }

    public CartHome setAddInCart(boolean addInCart) {

        this.addInCart = addInCart;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mrp);
        dest.writeString(unit);
        dest.writeString(price);
        dest.writeString(quantity);
        dest.writeString(varient_id);
        dest.writeString(product_id);
        dest.writeString(description);
        dest.writeString(product_name);
        dest.writeString(varient_image);
        dest.writeString(product_image);
        dest.writeInt(cartValue);
        dest.writeByte((byte) (addInCart ? 1 : 0));
    }

    public static final Creator<CartHome> CREATOR = new Creator<CartHome>() {
        @Override
        public CartHome createFromParcel(Parcel in) {
            return new CartHome(in);
        }

        @Override
        public CartHome[] newArray(int size) {
            return new CartHome[size];
        }
    };
}
