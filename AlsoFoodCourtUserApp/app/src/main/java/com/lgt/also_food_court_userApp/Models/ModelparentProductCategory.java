package com.lgt.also_food_court_userApp.Models;


import java.util.List;

public class ModelparentProductCategory {
    String categoryName;
    private List<ModelProductList> listProducts;

    public ModelparentProductCategory() {
    }

    public ModelparentProductCategory(String categoryName, List<ModelProductList> listProducts) {
        this.categoryName = categoryName;
        this.listProducts = listProducts;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ModelProductList> getListProducts() {
        return listProducts;
    }

    public void setListProducts(List<ModelProductList> listProducts) {
        this.listProducts = listProducts;
    }
}
