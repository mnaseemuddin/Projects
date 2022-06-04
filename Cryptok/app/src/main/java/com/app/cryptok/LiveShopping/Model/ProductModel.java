package com.app.cryptok.LiveShopping.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductModel implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Products_data")
    @Expose
    private List<ProductsDatum> productsData = null;
    private final static long serialVersionUID = 5230756028963526288L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductsDatum> getProductsData() {
        return productsData;
    }

    public void setProductsData(List<ProductsDatum> productsData) {
        this.productsData = productsData;
    }

    public static class ProductsDatum implements Serializable
    {

        @SerializedName("tbl_user_products_id")
        @Expose
        private String tblUserProductsId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("pro_name")
        @Expose
        private String proName;
        @SerializedName("pro_price")
        @Expose
        private String proPrice;
        @SerializedName("pro_description")
        @Expose
        private String proDescription;
        @SerializedName("proimages")
        @Expose
        private List<Proimage> proimages = null;
        private final static long serialVersionUID = 306558662504104794L;
        @SerializedName("isSelected")
        @Expose
        private boolean isSelected;


        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getTblUserProductsId() {
            return tblUserProductsId;
        }

        public void setTblUserProductsId(String tblUserProductsId) {
            this.tblUserProductsId = tblUserProductsId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }

        public String getProPrice() {
            return proPrice;
        }

        public void setProPrice(String proPrice) {
            this.proPrice = proPrice;
        }

        public String getProDescription() {
            return proDescription;
        }

        public void setProDescription(String proDescription) {
            this.proDescription = proDescription;
        }

        public List<Proimage> getProimages() {
            return proimages;
        }

        public void setProimages(List<Proimage> proimages) {
            this.proimages = proimages;
        }

    }


    public  class Proimage implements Serializable
    {

        @SerializedName("tbl_user_products_image_id")
        @Expose
        private String tblUserProductsImageId;
        @SerializedName("images")
        @Expose
        private String images;
        private final static long serialVersionUID = -7129079152274362939L;

        public String getTblUserProductsImageId() {
            return tblUserProductsImageId;
        }

        public void setTblUserProductsImageId(String tblUserProductsImageId) {
            this.tblUserProductsImageId = tblUserProductsImageId;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

    }

}
