package com.lgt.also_food_court_userApp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.tabs.TabLayout;
import com.lgt.also_food_court_userApp.Adapters.AdapterOffers;
import com.lgt.also_food_court_userApp.Models.ModelOffer;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentOffer extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, TabLayout.OnTabSelectedListener{

    private SliderLayout slider_offers;
    private RecyclerView rv_offers;
    private List<ModelOffer> list = new ArrayList<>();
    private AdapterOffers adapterOffers;
    private int i;
    private String PinCode;
    private ImageView iv_back_offer;
    private TextView tv_noRestaurantOffer;

    public FragmentOffer() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer, container, false);

        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey("PinCode")) {
                PinCode=bundle.getString("PinCode");
                Log.e("MJNHBGY",PinCode+"");
            }
        }




        slider_offers = view.findViewById(R.id.slider_offers);
        rv_offers = view.findViewById(R.id.rv_offers);
        iv_back_offer = view.findViewById(R.id.iv_back_offer);
        tv_noRestaurantOffer=view.findViewById(R.id.tv_noRestaurantOffer);

        iv_back_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack("Fragment_Offers",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

       // bannerSlider();
        loadOfferRestaurant();


        return view;
    }

    private void loadOfferRestaurant() {
        list.clear();
       StringRequest stringRequest=new StringRequest(Request.Method.POST, AlsoFoodCourtApi.Restaurant_OfferApi, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               Log.e("KMJNH",response+"");
               try {
                   JSONObject jsonObject=new JSONObject(response);
                   String message=jsonObject.getString("message");
                   String status=jsonObject.getString("status");
                   if (status.equals("1")){
                       JSONArray jsonArray=jsonObject.getJSONArray("data");
                       if (jsonArray.length()>0){
                           for (i=0;i<jsonArray.length();i++){

                               JSONObject jsonObjectRestaurantData=jsonArray.getJSONObject(i);
                               String tbl_restaurant_id=jsonObjectRestaurantData.getString("tbl_restaurant_id");
                               String shop_name=jsonObjectRestaurantData.getString("shop_name");
                               String offer=jsonObjectRestaurantData.getString("offer");
                               String description=jsonObjectRestaurantData.getString("description");
                               String address=jsonObjectRestaurantData.getString("address");
                               String restaurant_image=jsonObjectRestaurantData.getString("restaurant_image");

                               list.add(new ModelOffer(tbl_restaurant_id,restaurant_image,shop_name ,offer,description));

                               adapterOffers = new AdapterOffers(list,getActivity());
                               GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, rv_offers.VERTICAL, false);

                               rv_offers.setLayoutManager(manager);
                               rv_offers.hasFixedSize();
                               rv_offers.setNestedScrollingEnabled(false);
                               rv_offers.setAdapter(adapterOffers);
                               adapterOffers.notifyDataSetChanged();
                           }
                       }else {
                           tv_noRestaurantOffer.setVisibility(View.VISIBLE);
                           rv_offers.setVisibility(View.GONE);

                       }


                   }else {
                       tv_noRestaurantOffer.setVisibility(View.VISIBLE);
                       rv_offers.setVisibility(View.GONE);

                   }



               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String>params=new HashMap<>();
               params.put("pincode",PinCode);
               return params;
           }
       };
       RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
       requestQueue.add(stringRequest);


    }

    private void bannerSlider() {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Veg Burger", R.drawable.burger1);
        file_maps.put("Pizza", R.drawable.kfc);
        file_maps.put("Cold Coffee", R.drawable.coffee1);
        file_maps.put("Chicken Burger", R.drawable.kfc);
        file_maps.put("Veg Burger", R.drawable.burger2);
        file_maps.put("Veg Burger", R.drawable.burger3);
        file_maps.put("Cold Coffee", R.drawable.coffee2);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());

            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            slider_offers.addSlider(textSliderView);
        }
        slider_offers.setPresetTransformer(SliderLayout.Transformer.Default);
        slider_offers.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider_offers.setCustomAnimation(new DescriptionAnimation());
        slider_offers.setDuration(4000);
        slider_offers.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
