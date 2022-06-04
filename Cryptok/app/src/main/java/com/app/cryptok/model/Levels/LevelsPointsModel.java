package com.app.cryptok.model.Levels;

public class LevelsPointsModel {
    private String level;
    private String point_id;
    private int each;
    private int point;


    public LevelsPointsModel() {
    }

    public LevelsPointsModel(String level, String point_id, int each, int point) {
        this.level = level;
        this.point_id = point_id;
        this.each = each;
        this.point = point;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPoint_id() {
        return point_id;
    }

    public void setPoint_id(String point_id) {
        this.point_id = point_id;
    }

    public int getEach() {
        return each;
    }

    public void setEach(int each) {
        this.each = each;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
