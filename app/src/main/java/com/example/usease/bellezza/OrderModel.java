package com.example.usease.bellezza;

import java.util.Date;

/**
 * Created by Usease on 5/17/2018.
 */

public class OrderModel {

    public String product_id, phone, quantity, email, telegram;
    public Date date;

    public OrderModel() {
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public OrderModel(String product_id, String phone, String quantity, String email, Date date, String telegram) {
        this.product_id = product_id;
        this.phone = phone;
        this.quantity = quantity;
        this.email = email;
        this.date = date;
        this.telegram = telegram;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
