package com.example.usease.bellezza;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

/**
 * Created by Usease on 5/12/2018.
 */

public class MyOrderModel {

    public String email, product_id, quantity, token_id, phone;
    public Date date;

    @Exclude
    private String order_id;

    public MyOrderModel() {
    }

    public <T extends MyOrderModel> T withId(@NonNull final String order_id) {
        this.order_id = order_id;
        return (T) this;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public MyOrderModel(String email, String product_id, String quantity, String token_id, String phone, Date date) {
        this.email = email;
        this.product_id = product_id;
        this.quantity = quantity;
        this.token_id = token_id;
        this.phone = phone;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
