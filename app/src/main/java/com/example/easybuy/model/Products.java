package com.example.easybuy.model;

import java.io.Serializable;

public class Products implements Serializable {
    private int id;
    private String title;
    private String product;
    private String description;
    private int quantify;
    private Double price;

    public Products(String title, String product, String description, int quantify, double price){
        this.description = description;
        this.title = title;
        this.product = product;
        this.price = price;
        this.quantify = quantify;
    }

    public Products(){}

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
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
