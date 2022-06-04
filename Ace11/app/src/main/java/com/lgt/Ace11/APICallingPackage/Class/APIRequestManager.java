package com.lgt.Ace11.APICallingPackage.Class;

import android.app.Activity;
import android.content.Context;

import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.APICallingPackage.Interface.ServerResponseListner;
import com.lgt.Ace11.APICallingPackage.Interface.VolleyRestClient;

import org.json.JSONException;
import org.json.JSONObject;

public class APIRequestManager implements ServerResponseListner {

    Context mContext;
    ResponseManager responseManager;
    private VolleyRestClient volleyRestClient;

    public APIRequestManager(Context mContext) {
        this.mContext = mContext;
    }


    public void callAPI(String url, JSONObject jsonObject, Context mContext, Activity activity,
                        String type, boolean isShowProgress,
                        ResponseManager responseManager) throws JSONException {
        this.responseManager = responseManager;
        volleyRestClient = new VolleyApiCalling();
        volleyRestClient.callRestApi(url, jsonObject, mContext, activity, type,
                this, isShowProgress);
    }


    @Override
    public void onSucess(JSONObject response, String type, String message) {
        //Response only consist data object/array/string
        if (response != null && !response.equals("")) {
            try {

                System.out.println("afgasfgafs" + response);
                responseManager.getResult(mContext, type, message, response);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error, String type) {
        responseManager.onError(mContext, type, error);

    }
}
