package com.app.cryptok.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.app.cryptok.model.Points.PointsModel;

import java.lang.reflect.Type;

import static com.app.cryptok.utils.ConstantsKey.BROADCAST_START;
import static com.app.cryptok.utils.ConstantsKey.POINTS_DATA;
import static com.app.cryptok.utils.ConstantsKey.POINTS_TABLE;
import static com.app.cryptok.utils.ConstantsKey.PREF_DATABASE;
import static com.app.cryptok.utils.ConstantsKey.STREAM_TIME;
import static com.app.cryptok.utils.ConstantsKey.current_level;


public class SessionManager {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences points_pref;
    SharedPreferences stream_time_pref;
    SharedPreferences level_pref;
    SharedPreferences.Editor editor,editor2,editor3;


    public SessionManager(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences(PREF_DATABASE, Context.MODE_PRIVATE);
        points_pref=context.getSharedPreferences(POINTS_TABLE, Context.MODE_PRIVATE);
        stream_time_pref=context.getSharedPreferences(STREAM_TIME, Context.MODE_PRIVATE);
        level_pref=context.getSharedPreferences(current_level, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor2=stream_time_pref.edit();
        editor3=level_pref.edit();
    }
    public void setString(String KEY, String value){
        editor.putString(KEY,value).apply();
    }
    public String getString(String KEY){
        return sharedPreferences.getString(KEY,"");
    }
    public void setBoolean(String KEY, Boolean value){
        editor.putBoolean(KEY,value).apply();
    }
    public Boolean getBoolean(String KEY){
        return sharedPreferences.getBoolean(KEY,false);
    }
    public void clearData(){
        editor.clear();
        editor.apply();
    }

    public void setPoints( PointsModel pointsModel) {
        editor = points_pref.edit();
        if (pointsModel == null)
            editor.putString(POINTS_DATA, " ");
        else {
            String safetyJSONString = new Gson().toJson(pointsModel);
            editor.putString(POINTS_DATA, safetyJSONString);
        }
        editor.apply();
    }
    public void saveStreamStarted( long start_time) {
        editor2 = stream_time_pref.edit();
        editor2.putLong(BROADCAST_START,start_time);
        editor2.apply();

    }
    public long getStreamStarted() {

        return stream_time_pref.getLong(BROADCAST_START,System.currentTimeMillis());

    }
    public void saveMyLevel( String level) {
        editor3 = level_pref.edit();
        editor3.putString(current_level,level);
        editor3.apply();

    }
    public String getcurrent_level() {

        return level_pref.getString(current_level,"");

    }
    public PointsModel getPoints() {

        if (!points_pref.contains(POINTS_DATA)){
            return null;
        }
        String safetyJSONString = points_pref.getString(POINTS_DATA, " ");

        Type type = new TypeToken<PointsModel>() {
        }.getType();
        PointsModel pointsModel = new Gson().fromJson(safetyJSONString, type);
        return pointsModel;
    }


}
