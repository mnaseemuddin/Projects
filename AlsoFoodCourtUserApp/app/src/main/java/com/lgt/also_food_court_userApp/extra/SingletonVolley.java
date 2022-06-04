package com.lgt.also_food_court_userApp.extra;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonVolley {



    private static SingletonVolley mSingletonRequestQueue;
    private Context mContext;
    private RequestQueue mRequestQueue;

    public SingletonVolley(Context mContext) {
        this.mContext = mContext;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized SingletonVolley getInstance(Context mContext){
        if(mSingletonRequestQueue == null){
            mSingletonRequestQueue = new SingletonVolley(mContext);
        }
        return mSingletonRequestQueue;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }

        return mRequestQueue;
    }
}
