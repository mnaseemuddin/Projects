package com.lgt.also_food_court_userApp.Models;

import java.util.ArrayList;

/**
 * Created by Ranjan on 25/09/2019.
 */
public class ModelVertical {
    private String category_name,id;
    private ArrayList<ModelHorizontal> modelHorizontals;

    public ModelVertical(String category_name, String id, ArrayList<ModelHorizontal> modelHorizontals) {
        this.category_name = category_name;
        this.id = id;
        this.modelHorizontals = modelHorizontals;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ModelHorizontal> getModelHorizontals() {
        return modelHorizontals;
    }

    public void setModelHorizontals(ArrayList<ModelHorizontal> modelHorizontals) {
        this.modelHorizontals = modelHorizontals;
    }
}
