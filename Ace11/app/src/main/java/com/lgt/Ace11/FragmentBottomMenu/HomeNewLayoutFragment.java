package com.lgt.Ace11.FragmentBottomMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.Adapter.HomeListAdapter;
import com.lgt.Ace11.Adapter.ResultMatchAdapter;
import com.lgt.Ace11.Bean.BannerData;
import com.lgt.Ace11.Bean.BeanHomeFixtures;
import com.lgt.Ace11.Bean.BeanHomeLive;
import com.lgt.Ace11.HomeActivity;
import com.lgt.Ace11.R;
import com.lgt.Ace11.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lgt.Ace11.Config.BANNER_OFFER_LIST_API;
import static com.lgt.Ace11.Config.HOMEFIXTURES_LIVE;
import static com.lgt.Ace11.Config.ProfileIMAGEBASEURL;
import static com.lgt.Ace11.Config.ProfileoffersIMAGEBASEURL;
import static com.lgt.Ace11.Constants.BANNERHOMETYPE;
import static com.lgt.Ace11.Constants.FIXTURESHOMETYPE;
import static com.lgt.Ace11.Constants.LIVEHOMETYPE;

public class HomeNewLayoutFragment extends Fragment implements ResponseManager {
    SliderLayout sl_HomeSlider;

    ResultMatchAdapter resultMatchAdapter;
    HomeListAdapter homeListAdapter;
    ProgressBar liveProgressBar;
    HomeActivity activity;
    Context context;
    TextView tv_NoMatchesFound,tv_NoListMatchesFound;
    RecyclerView rv_UpcomingEventList, rv_allEventList;
    ArrayList<String> mList;

    SessionManager sessionManager;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.home_new_layout_fragment, container, false);
        initView(mView);
        return mView;
    }

    private void initView(View view) {

        mList = new ArrayList<String>();
        sl_HomeSlider = view.findViewById(R.id.sl_HomeSlider);
        tv_NoMatchesFound = view.findViewById(R.id.tv_NoMatchesFound);
        tv_NoListMatchesFound = view.findViewById(R.id.tv_NoListMatchesFound);
        liveProgressBar = view.findViewById(R.id.liveProgressBar);
        rv_allEventList = view.findViewById(R.id.rv_UpcomingEventList);
        rv_UpcomingEventList = view.findViewById(R.id.rv_matchComplete);
        context = activity = (HomeActivity) getActivity();

        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        getSlider();
        callHomeFixtures(true);
        callHomeLive(true);
    }

    JSONObject createRequestFixerJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "Fixture");
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            // jsonObject.put("token", FirebaseInstanceId.getInstance().getToken());
            Log.e("tokennnn", sessionManager.getUser(context).getUser_id() + " | " + FirebaseInstanceId.getInstance().getToken() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    JSONObject createRequestLiveJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "Live");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void callHomeFixtures(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(HOMEFIXTURES_LIVE,
                    createRequestFixerJson(), context, activity, FIXTURESHOMETYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callHomeLive(boolean isShowLoader) {
        liveProgressBar.setVisibility(View.VISIBLE);
        try {
            apiRequestManager.callAPI(HOMEFIXTURES_LIVE,
                    createRequestLiveJson(), context, activity, LIVEHOMETYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setHomeListOfResult(List<BeanHomeFixtures> bean) {
        homeListAdapter = new HomeListAdapter(getContext(), bean);
        rv_allEventList.setHasFixedSize(true);
        rv_allEventList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rv_allEventList.setAdapter(homeListAdapter);
    }

    private void setListOfUpcoming(List<BeanHomeLive> beanHome) {
        SnapHelper snapHelper = new PagerSnapHelper();
        resultMatchAdapter = new ResultMatchAdapter(getContext(), beanHome);
        rv_UpcomingEventList.setHasFixedSize(true);
        rv_UpcomingEventList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        snapHelper.attachToRecyclerView(rv_UpcomingEventList);
        rv_UpcomingEventList.setAdapter(resultMatchAdapter);
    }

    private void getSlider() {
        try {
            apiRequestManager.callAPI(BANNER_OFFER_LIST_API,
                    createRequestLiveJson(), context, activity, BANNERHOMETYPE,
                    false, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        if (type.equals(FIXTURESHOMETYPE)) {
            try {
                JSONArray jsonArray = result.getJSONArray("data");
                Log.e("FixerDataResponse", result + "");
                List<BeanHomeFixtures> beanHomeFixtures = new Gson().fromJson(jsonArray.toString(),
                        new TypeToken<List<BeanHomeFixtures>>() {
                        }.getType());
                setHomeListOfResult(beanHomeFixtures);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type.equals(LIVEHOMETYPE)) {
            liveProgressBar.setVisibility(View.GONE);
            try {
                JSONArray jsonArray = result.getJSONArray("data");
                Log.e("LiveDataResponse", result + "");
                List<BeanHomeLive> beanHomeLive = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BeanHomeLive>>() {
                }.getType());
                setListOfUpcoming(beanHomeLive);
            } catch (Exception e) {
                e.printStackTrace();
                liveProgressBar.setVisibility(View.GONE);
            }
        } else if (type.equals(BANNERHOMETYPE)){
            try {
                List<BannerData> mDataList = new ArrayList<>();
                JSONArray jsonArray = result.getJSONArray("data");

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject rec = jsonArray.getJSONObject(i);
                    Log.e("LiveBannerResponse", rec.getString("banner_image") + "BannerData: " + rec);
                    BannerData bannerData = new BannerData();
                    String banner_image = rec.getString("banner_image");
                    bannerData.setBannerImage(banner_image);
                    mDataList.add(bannerData);
                }
                setSliderVisible(mDataList);

            } catch (Exception e) {
                e.printStackTrace();
                liveProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private void setSliderVisible(List<BannerData> mDataList) {
        Log.d("BannerData", "" + mDataList.size());
        for (int i = 0; i < mDataList.size(); i++) {
            Log.d("SINGLE_BANNER_IMAGE",""+ProfileoffersIMAGEBASEURL+mDataList.get(i).getBannerImage());
            DefaultSliderView textSliderView = new DefaultSliderView(getContext());
            textSliderView.image(ProfileoffersIMAGEBASEURL+mDataList.get(i).getBannerImage())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {

                        }
                    });

            sl_HomeSlider.addSlider(textSliderView);
            sl_HomeSlider.setDuration(9000);
        }
    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onError(Context mContext, String type, String message) {
        Log.d("RequestError","Type: "+type+", ErrorMessage: "+message);
        if (type.equals(FIXTURESHOMETYPE)) {
            tv_NoListMatchesFound.setVisibility(View.VISIBLE);
            tv_NoListMatchesFound.setText("No Recent Matches Found!");
        }else if (type.equals(LIVEHOMETYPE)) {
            tv_NoMatchesFound.setVisibility(View.VISIBLE);
            tv_NoMatchesFound.setText("No Live Matches Found");
        }
        liveProgressBar.setVisibility(View.GONE);
    }
}
