package com.example.easybuy.model;

import java.io.Serializable;

public class Products implements Serializable {
    private String id;
    private String title;
    private String product;
    private int quantify;
    private Double price;

    public Products(String title, String product, int quantify, double price){
        this.title = title;
        this.product = product;
        this.price = price;
        this.quantify = quantify;
    }

    public Products(){}

    public Products(String product, int quantify, double price){
        this.quantify = quantify;
        this.price = price;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantify() {
        return quantify;
    }

    public String getProduct() {
        return product;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setQuantify(int quantify) {
        this.quantify = quantify;
    }
}
