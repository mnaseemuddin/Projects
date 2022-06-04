package com.app.cryptok.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app.cryptok.Api.MyApi;
import com.app.cryptok.R;
import com.app.cryptok.adapter.HostGiftAdapter;
import com.app.cryptok.databinding.FragmentHostGiftSheetBinding;
import com.app.cryptok.go_live_module.LiveGiftPOJO;
import com.app.cryptok.utils.Commn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HostGiftSheet  extends BottomSheetDialogFragment {

    private List<LiveGiftPOJO> liveGiftPOJOS = new ArrayList<>();
    private HostGiftAdapter giftAdapter;

    private FragmentHostGiftSheetBinding binding;
    public HostGiftSheet() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_host_gift_sheet, container, false);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        return binding.getRoot();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new Handler(Looper.getMainLooper())
                .postDelayed(() -> getGift(),100);

        handleClick();
    }

    private void handleClick() {
        binding.ivClose.setOnClickListener(view -> dismiss());
    }

    private void iniLAYOUT() {


        giftAdapter = new HostGiftAdapter(getActivity(), liveGiftPOJOS);
        binding.rvGiftList.setAdapter(giftAdapter);
    }
    private void getGift() {
        iniLAYOUT();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MyApi.diamond_list_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Commn.showDebugLog("diamond_list_api:"+response);
                binding.progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if ("1".equalsIgnoreCase(status)) {

                        JSONArray jsonArray = jsonObject.getJSONArray("stiker_data");

                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String tbl_product_id = jsonObject1.getString("tbl_product_id");
                            String name = jsonObject1.getString("name");
                            String image = jsonObject1.getString("image");
                            String value = jsonObject1.getString("value");

                            liveGiftPOJOS.add(new LiveGiftPOJO(tbl_product_id, image, name,value));

                        }
                        giftAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /*Intent intent=new Intent("abs");
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
        Commn.requestQueue(getActivity(), stringRequest);

    }

}