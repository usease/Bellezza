package com.example.usease.bellezza;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class ProductModel {

    public String name, desc, spec, additional_info, main_image;
    public int available, price;
    public Date date;
    String image1;
    String image2;
    String image3;

    @Exclude
    private String product_id;

    public ProductModel() {
    }

    public ProductModel(String name, String desc, String spec, String additional_info, String main_image, int available, int price, Date date, String image1, String image2, String image3) {
        this.name = name;
        this.desc = desc;
        this.spec = spec;
        this.additional_info = additional_info;
        this.main_image = main_image;
        this.available = available;
        this.price = price;
        this.date = date;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    public <T extends ProductModel> T withId(@NonNull final String product_id) {
        this.product_id = product_id;
        return (T) this;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
