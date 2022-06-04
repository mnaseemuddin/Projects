package Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ranjan on 1/30/2020.
 */
public class ModelPlayerInformation {

    private String name,role,rating,points,starting11,run,four,six,sr,fifty,duck,wickets,maidenover,er,catches,thirty,bonus;

    public ModelPlayerInformation(String name, String role, String rating, String points, String starting11, String run, String four, String six, String sr, String fifty, String duck, String wickets, String maidenover, String er, String catches, String thirty, String bonus) {
        this.name = name;
        this.role = role;
        this.rating = rating;
        this.points = points;
        this.starting11 = starting11;
        this.run = run;
        this.four = four;
        this.six = six;
        this.sr = sr;
        this.fifty = fifty;
        this.duck = duck;
        this.wickets = wickets;
        this.maidenover = maidenover;
        this.er = er;
        this.catches = catches;
        this.thirty = thirty;
        this.bonus = bonus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStarting11() {
        return starting11;
    }

    public void setStarting11(String starting11) {
        this.starting11 = starting11;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getSix() {
        return six;
    }

    public void setSix(String six) {
        this.six = six;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getFifty() {
        return fifty;
    }

    public void setFifty(String fifty) {
        this.fifty = fifty;
    }

    public String getDuck() {
        return duck;
    }

    public void setDuck(String duck) {
        this.duck = duck;
    }

    public String getWickets() {
        return wickets;
    }

    public void setWickets(String wickets) {
        this.wickets = wickets;
    }

    public String getMaidenover() {
        return maidenover;
    }

    public void setMaidenover(String maidenover) {
        this.maidenover = maidenover;
    }

    public String getEr() {
        return er;
    }

    public void setEr(String er) {
        this.er = er;
    }

    public String getCatches() {
        return catches;
    }

    public void setCatches(String catches) {
        this.catches = catches;
    }

    public String getThirty() {
        return thirty;
    }

    public void setThirty(String thirty) {
        this.thirty = thirty;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}
