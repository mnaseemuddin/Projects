package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ranjan on 2/11/2020.
 */
public class ModelTeamA implements Serializable {
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("point")
    @Expose
    private String point;
    @SerializedName("starting11")
    @Expose
    private String starting11;
    @SerializedName("run")
    @Expose
    private String run;
    @SerializedName("four")
    @Expose
    private String four;
    @SerializedName("six")
    @Expose
    private String six;
    @SerializedName("sr")
    @Expose
    private String sr;
    @SerializedName("fifty")
    @Expose
    private String fifty;
    @SerializedName("duck")
    @Expose
    private String duck;
    @SerializedName("wkts")
    @Expose
    private String wkts;
    @SerializedName("maidenover")
    @Expose
    private String maidenover;
    @SerializedName("er")
    @Expose
    private String er;
    @SerializedName("catch")
    @Expose
    private String _catch;
    @SerializedName("runoutstumping")
    @Expose
    private String runoutstumping;
    @SerializedName("runoutthrower")
    @Expose
    private String runoutthrower;
    @SerializedName("runoutcatcher")
    @Expose
    private String runoutcatcher;
    @SerializedName("directrunout")
    @Expose
    private String directrunout;
    @SerializedName("stumping")
    @Expose
    private String stumping;
    @SerializedName("thirty")
    @Expose
    private String thirty;
    @SerializedName("bonus")
    @Expose
    private String bonus;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
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

    public String getWkts() {
        return wkts;
    }

    public void setWkts(String wkts) {
        this.wkts = wkts;
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

    public String getCatch() {
        return _catch;
    }

    public void setCatch(String _catch) {
        this._catch = _catch;
    }

    public String getRunoutstumping() {
        return runoutstumping;
    }

    public void setRunoutstumping(String runoutstumping) {
        this.runoutstumping = runoutstumping;
    }

    public String getRunoutthrower() {
        return runoutthrower;
    }

    public void setRunoutthrower(String runoutthrower) {
        this.runoutthrower = runoutthrower;
    }

    public String getRunoutcatcher() {
        return runoutcatcher;
    }

    public void setRunoutcatcher(String runoutcatcher) {
        this.runoutcatcher = runoutcatcher;
    }

    public String getDirectrunout() {
        return directrunout;
    }

    public void setDirectrunout(String directrunout) {
        this.directrunout = directrunout;
    }

    public String getStumping() {
        return stumping;
    }

    public void setStumping(String stumping) {
        this.stumping = stumping;
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