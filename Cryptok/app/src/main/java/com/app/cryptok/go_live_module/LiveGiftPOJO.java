package com.app.cryptok.go_live_module;

import java.io.Serializable;

public class LiveGiftPOJO implements Serializable {
    private String gift_id;
    public String giftImage;
    public String giftName;
    public String value;

    public LiveGiftPOJO() {
    }

    public LiveGiftPOJO(String gift_id, String giftImage, String giftName, String value) {
        this.gift_id = gift_id;
        this.giftImage = giftImage;
        this.giftName = giftName;
        this.value = value;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(String giftImage) {
        this.giftImage = giftImage;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
