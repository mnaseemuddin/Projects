package com.app.cryptok.model.Points;

public class PointsModel {
    private int checkin_points;
    private int on_recieve_gift;
    private int on_send_gift;
    private int on_share;
    private int watch_stream;
    private String min_watch_time;

    public PointsModel() {

    }

    public PointsModel(int checkin_points, int on_recieve_gift, int on_send_gift, int on_share, int watch_stream, String min_watch_time) {
        this.checkin_points = checkin_points;
        this.on_recieve_gift = on_recieve_gift;
        this.on_send_gift = on_send_gift;
        this.on_share = on_share;
        this.watch_stream = watch_stream;
        this.min_watch_time = min_watch_time;
    }

    public int getCheckin_points() {
        return checkin_points;
    }

    public void setCheckin_points(int checkin_points) {
        this.checkin_points = checkin_points;
    }

    public int getOn_recieve_gift() {
        return on_recieve_gift;
    }

    public void setOn_recieve_gift(int on_recieve_gift) {
        this.on_recieve_gift = on_recieve_gift;
    }

    public int getOn_send_gift() {
        return on_send_gift;
    }

    public void setOn_send_gift(int on_send_gift) {
        this.on_send_gift = on_send_gift;
    }

    public int getOn_share() {
        return on_share;
    }

    public void setOn_share(int on_share) {
        this.on_share = on_share;
    }

    public int getWatch_stream() {
        return watch_stream;
    }

    public void setWatch_stream(int watch_stream) {
        this.watch_stream = watch_stream;
    }

    public String getMin_watch_time() {
        return min_watch_time;
    }

    public void setMin_watch_time(String min_watch_time) {
        this.min_watch_time = min_watch_time;
    }
}
