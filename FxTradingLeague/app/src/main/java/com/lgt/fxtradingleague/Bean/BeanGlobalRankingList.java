package com.lgt.fxtradingleague.Bean;



import java.io.Serializable;

/**
 * Created by telasol1 on 26-Oct-18.
 */

public class BeanGlobalRankingList implements Serializable {

    private String user_id,name,points,rank;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
