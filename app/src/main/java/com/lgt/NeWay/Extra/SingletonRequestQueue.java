package com.lgt.NeWay.Extra;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Ranjan on 11/26/2019.
 */
public class SingletonRequestQueue {

    private static com.lgt.NeWay.Extra.SingletonRequestQueue mSingletonRequestQueue;
    private Context mContext;
    private RequestQueue mRequestQueue;

    public SingletonRequestQueue(Context mContext) {
        this.mContext = mContext;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized com.lgt.NeWay.Extra.SingletonRequestQueue getInstance(Context mContext){
        if(mSingletonRequestQueue == null){
            mSingletonRequestQueue = new com.lgt.NeWay.Extra.SingletonRequestQueue(mContext);
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
