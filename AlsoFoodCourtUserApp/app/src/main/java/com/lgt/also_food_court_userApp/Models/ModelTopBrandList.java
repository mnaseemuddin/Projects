package com.lgt.also_food_court_userApp.Models;

/**
 * Created by Ranjan on 6/29/2020.
 */
public class ModelTopBrandList {
    private String BrandId,TopBrandProductName,iv_TopBrandsItem;

    public ModelTopBrandList(String brandId, String topBrandProductName, String iv_TopBrandsItem) {
        BrandId = brandId;
        TopBrandProductName = topBrandProductName;
        this.iv_TopBrandsItem = iv_TopBrandsItem;
    }

    public String getBrandId() {
        return BrandId;
    }

    public void setBrandId(String brandId) {
        BrandId = brandId;
    }

    public String getTopBrandProductName() {
        return TopBrandProductName;
    }

    public void setTopBrandProductName(String topBrandProductName) {
        TopBrandProductName = topBrandProductName;
    }

    public String getIv_TopBrandsItem() {
        return iv_TopBrandsItem;
    }

    public void setIv_TopBrandsItem(String iv_TopBrandsItem) {
        this.iv_TopBrandsItem = iv_TopBrandsItem;
    }
}
