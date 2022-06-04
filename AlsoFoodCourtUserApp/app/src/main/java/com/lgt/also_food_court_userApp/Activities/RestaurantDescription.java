package com.lgt.also_food_court_userApp.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.tabs.TabLayout;
import com.lgt.also_food_court_userApp.Adapters.AdapterRestaurantParentProductCategory;
import com.lgt.also_food_court_userApp.Adapters.AdapterSliderImage;
import com.lgt.also_food_court_userApp.Adapters.RestaurentMenuAdapter;
import com.lgt.also_food_court_userApp.Fragments.FragmentCart;
import com.lgt.also_food_court_userApp.Fragments.ModalBottomSheetProducts;
import com.lgt.also_food_court_userApp.Models.ModelProductList;
import com.lgt.also_food_court_userApp.Models.ModelparentProductCategory;
import com.lgt.also_food_court_userApp.Models.RestaurentMenuModel;
import com.lgt.also_food_court_userApp.extra.Common;
import com.lgt.also_food_court_userApp.extra.ItemClickListener;
import com.lgt.also_food_court_userApp.extra.SingletonVolley;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RestaurantDescription extends AppCompatActivity implements ModalBottomSheetProducts.BottomSheetListener, ItemClickListener {

    private List<ModelProductList> listProducts = new ArrayList<>();

    private AdapterRestaurantParentProductCategory adapterRestaurantParentProductCategory;
    private RecyclerView rv_restaurantProducts;
    private List<ModelparentProductCategory> listParentProductsData;


   // private List<String>ResImage=new ArrayList<>();

    private LinearLayoutManager layoutManager;

    public String tbl_restaurant_products_id, tbl_products_category_id;
    private TextView tv_restaurant_ImageNotFound;

    private LinearLayout llBottomRestaurantDescription;


    private ImageView ivBackDescription;
    private SliderView sliderRestaurantList;
    private AdapterSliderImage adapterSliderImage;
    private ArrayList<String>ImagesSliderList=new ArrayList<>();

    private TextView tvRestaurantNameDes, tvFoodDescription, tvLocationDesc, tvOpeningTime, tvCloseTiming, tvRatingDesc, tvTotalNumberOfReviews,
            tvNoRestaurantFound, tvTotalCartPrice;

    private NestedScrollView svRestaurantDescription;
    private ProgressBar pb_RestaurantDescription;
    private SharedPreferences sharedPreferences;
    private String mUserID, category_title,RestaurantImage;

    private TextView tvTotalCartCount;

    private static String mRestaurantID = "";

    private static final String TAG = "RestaurantDescription";

    public static String total_cart_price, Delivery_charge;

    //restaurent_menu
    private CardView Restaurant_menu;
    public static AlertDialog restaurent_dialog;
    private RecyclerView rv_restaurent_menu;
    private RestaurentMenuAdapter menuAdapter;
    private ArrayList<RestaurentMenuModel> rest_menu_list = new ArrayList<>();
    //restaurent_menu//

    //switch food type
    private SwitchButton switch_food_type;
    private String food_type = "Veg";
    //
    private int CurrentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_description);

        sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(UpdateCart, new IntentFilter(Common.UPDATE_CART));

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("KEY_RESTAURANT_ID")) {
                mRestaurantID = intent.getStringExtra("KEY_RESTAURANT_ID");
                Log.e("hjkhjkhjkhjkh", mRestaurantID + "");

            }

        }

        Initialization();

       // ResImage.add(RestaurantImage);
        //->llBottomRestaurantDescription set visibility only if cart count is > 0

        loadProducts();

        getCart();


    }

    private void Initialization() {
        tv_restaurant_ImageNotFound = findViewById(R.id.tv_restaurant_ImageNotFound);
        ivBackDescription = findViewById(R.id.ivBackDescription);
        sliderRestaurantList = findViewById(R.id.sliderRestaurantList);
        tvRestaurantNameDes = findViewById(R.id.tvRestaurantNameDes);
        tvFoodDescription = findViewById(R.id.tvFoodDescription);
        tvLocationDesc = findViewById(R.id.tvLocationDesc);
        tvOpeningTime = findViewById(R.id.tvOpeningTime);
        tvCloseTiming = findViewById(R.id.tvCloseTiming);
        tvRatingDesc = findViewById(R.id.tvRatingDesc);
        tvTotalNumberOfReviews = findViewById(R.id.tvTotalNumberOfReviews);
        rv_restaurantProducts = findViewById(R.id.rv_restaurantProducts);
        svRestaurantDescription = findViewById(R.id.svRestaurantDescription);
        tvNoRestaurantFound = findViewById(R.id.tvNoRestaurantFound);
        pb_RestaurantDescription = findViewById(R.id.pb_RestaurantDescription);
        llBottomRestaurantDescription = findViewById(R.id.llBottomRestaurantDescription);
        tvTotalCartCount = findViewById(R.id.tvTotalCartCount);
        tvTotalCartPrice = findViewById(R.id.tvTotalCartPrice);
        Restaurant_menu = findViewById(R.id.Restaurant_menu);
        switch_food_type = findViewById(R.id.switch_food_type);
        ivBackDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (sharedPreferences.contains("user_id")) {
            mUserID = sharedPreferences.getString("user_id", "");
        }

        llBottomRestaurantDescription.setVisibility(View.GONE);

        llBottomRestaurantDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameRestaurantDescription, new FragmentCart()).addToBackStack("Fragment_Cart").commit();
            }
        });

        Restaurant_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRestaurantMenu();
            }
        });


    }

    private void openRestaurantMenu() {
        View custom_view = LayoutInflater.from(this).inflate(R.layout.restaurent_menu, null);
        restaurent_dialog = new AlertDialog.Builder(this, R.style.PauseDialog).create();
        restaurent_dialog.setView(custom_view);
        restaurent_dialog.show();
        restaurent_dialog.getWindow().setGravity(Gravity.BOTTOM);
        restaurent_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        rv_restaurent_menu = custom_view.findViewById(R.id.rv_restaurent_menu);
        loadRestaurantMenu();

    }

    private void loadRestaurantMenu() {
        rest_menu_list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.MenuListApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("hdgfh", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                                String tbl_products_category_id = jsonObjectData.getString("tbl_products_category_id");
                                String category_title = jsonObjectData.getString("category_title");
                                String total_products = jsonObjectData.getString("total_products");
                                // String price=jsonObjectData.getString("price");
                                rest_menu_list.add(new RestaurentMenuModel(category_title, total_products));


                            }
                            menuAdapter = new RestaurentMenuAdapter(RestaurantDescription.this, rest_menu_list, RestaurantDescription.this);
                            rv_restaurent_menu.setHasFixedSize(true);
                            rv_restaurent_menu.setLayoutManager(new LinearLayoutManager(RestaurantDescription.this, LinearLayoutManager.VERTICAL, false));

                            rv_restaurent_menu.setAdapter(menuAdapter);
                            menuAdapter.notifyDataSetChanged();
                        }
                    }else{
                        Toast.makeText(RestaurantDescription.this, message+"", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("restaurant_id", mRestaurantID);
                Log.e("ghggfskg", params + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    public void getCart() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.REVIEW_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("saddsadjhksadhuisadh", response + "");
                Log.e("mhjshfsdjfhdfjfd", "methodcalalled: ");

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {

                        total_cart_price = jsonObject.getString("total_cart_price");
                        Delivery_charge = jsonObject.getString("Delivery_charge");
                        tvTotalCartPrice.setText(total_cart_price);


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.length() > 0) {
                            llBottomRestaurantDescription.setVisibility(View.VISIBLE);
                            tvTotalCartCount.setText(String.valueOf(jsonArray.length()));
                        }
                    } else {
                        //   restaurent_menu.setVisibility(View.GONE);
                        llBottomRestaurantDescription.setVisibility(View.GONE);
                        Log.e(TAG, "visibilityshouldgone: ");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                Log.e("klkjkljkljdjsalk", mUserID + "");
                return params;
            }
        };


        RequestQueue requestQueue = SingletonVolley.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.add(stringRequest);

    }

    private void  loadProducts() {

        pb_RestaurantDescription.setVisibility(View.VISIBLE);
        listParentProductsData = new ArrayList<>();
        listParentProductsData.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.RESTAURANT_PRODUCT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                pb_RestaurantDescription.setVisibility(View.GONE);

                Log.e("sdadsadsadsada", response + "");


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    String tbl_restaurant_id = jsonObject.getString("tbl_restaurant_id");
                    String shop_name = jsonObject.getString("shop_name");
                    String restaurant_type = jsonObject.getString("restaurant_type");
                    String description = jsonObject.getString("description");
                    String address = jsonObject.getString("address");
                    String open_time = jsonObject.getString("open_time");
                    String close_time = jsonObject.getString("close_time");
                    String pincode = jsonObject.getString("pincode");
                    String block = jsonObject.getString("block");

                    JSONArray jsonArrayImage=jsonObject.getJSONArray("restaurant_image");
                    if (jsonArrayImage.length()>0){
                        for (int k=0;k<jsonArrayImage.length();k++){
                            JSONObject jsonObjectData=jsonArrayImage.getJSONObject(k);
                             RestaurantImage=jsonObjectData.getString("image");
                            Log.e("mdkfjbhd", RestaurantImage+"");

                            ImagesSliderList.add(RestaurantImage);
                            adapterSliderImage=new AdapterSliderImage(ImagesSliderList,getApplicationContext());
                            sliderRestaurantList.setSliderAdapter(adapterSliderImage);
                            sliderRestaurantList.startAutoCycle();
                            sliderRestaurantList.setIndicatorAnimation(IndicatorAnimations.SWAP);
                            sliderRestaurantList.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);


                        }
                      /*  if (!RestaurantImage.equals("")) {
                            loadBanners(String.valueOf(ResImage));

                        }*/

                    }



                  //  String restaurant_image = jsonObject.getString("restaurant_image");

                    tvRestaurantNameDes.setText(shop_name);
                    tvFoodDescription.setText(description);
                    tvLocationDesc.setText(address + ", " + block + ", " + pincode);
                    tvOpeningTime.setText(open_time);
                    tvCloseTiming.setText(close_time);



                    if (status.equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("restaurant_products");

                        if (jsonArray.length() > 0) {
                            //  listProducts.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                String tbl_products_category_id = object.getString("tbl_products_category_id");
                                category_title = object.getString("category_title");


                                JSONArray jsonArrayProductData = object.getJSONArray("products_data");
                                // listProducts.clear();

                                if (jsonArrayProductData.length() > 0) {
                                    listProducts = new ArrayList<>();
                                    for (int j = 0; j < jsonArrayProductData.length(); j++) {

                                        JSONObject Product = jsonArrayProductData.getJSONObject(j);
                                        String tbl_restaurant_products_id = Product.getString("tbl_restaurant_products_id");
                                        String products_name = Product.getString("products_name");
                                        String product_type = Product.getString("product_type");
                                        String image = Product.getString("image");
                                        String rating = Product.getString("rating");
                                        String review = Product.getString("review");
                                        String review_count = Product.getString("review_count");
                                        String price_type = Product.getString("price_type");
                                        String price = Product.getString("price");
                                        String main_price = Product.getString("main_price");

                                        listProducts.add(new ModelProductList(tbl_restaurant_products_id, products_name, product_type, image,
                                                rating, review, review_count, price,main_price));

                                    }

                                }
                                listParentProductsData.add(new ModelparentProductCategory(category_title, listProducts));


                            }

                           /* if (switch_food_type.isChecked()){
                                food_type="Veg";
                            }else {*/
                            //food_type = "NonVeg";

                            food_type = "Veg";


                            // }


                            setProducts(listParentProductsData, food_type);

                            switch_food_type.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                                    if (isChecked) {
                                        Log.e("checked", "yes");
                                       // food_type = "Veg";
                                        food_type = "NonVeg";




                                        setProducts(listParentProductsData, food_type);

                                    } else {

                                        Log.e("checked", "no");
                                       // food_type = "NonVeg";

                                        food_type = "Veg";


                                        setProducts(listParentProductsData, food_type);

                                    }
                                }
                            });

                        }

                    } else {
                        svRestaurantDescription.setVisibility(View.GONE);
                        tvNoRestaurantFound.setVisibility(View.VISIBLE);
                        Restaurant_menu.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_RestaurantDescription.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tbl_restaurant_id", mRestaurantID);
                Log.e("params", params.toString() + "");
                return params;
            }
        };


        RequestQueue requestQueue = SingletonVolley.getInstance(getApplicationContext()).getRequestQueue();
        requestQueue.add(stringRequest);


    }

    private void setProducts(List<ModelparentProductCategory> listParentProductsData, String food_type) {
        adapterRestaurantParentProductCategory = new AdapterRestaurantParentProductCategory(listParentProductsData, RestaurantDescription.this, food_type);

        layoutManager = new LinearLayoutManager(RestaurantDescription.this, LinearLayoutManager.VERTICAL, false);
        rv_restaurantProducts.setLayoutManager(layoutManager);
        rv_restaurantProducts.hasFixedSize();
        rv_restaurantProducts.setAdapter(adapterRestaurantParentProductCategory);
        rv_restaurantProducts.setNestedScrollingEnabled(false);
        adapterRestaurantParentProductCategory.notifyDataSetChanged();


    }


    @Override
    public void onButtonClicked(String text) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public BroadcastReceiver UpdateCart = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                if (intent.getAction().equalsIgnoreCase(Common.UPDATE_CART)) {
                    getCart();
                }
            }


        }

    };

    @Override
    public void itemClick(final int position) {
        Log.e("dsfasdf", "Position  :  " + position);
       // loadProducts(position);
        adapterRestaurantParentProductCategory.notifyDataSetChanged();

    if (rv_restaurantProducts!=null && rv_restaurantProducts.getChildAt(position)!=null) {
            final float y = rv_restaurantProducts.getY() + rv_restaurantProducts.getChildAt(position).getY();
            svRestaurantDescription.post(new Runnable() {
                @Override
                public void run() {
                    svRestaurantDescription.fling(0);
                    svRestaurantDescription.smoothScrollTo(0, (int) y);
                    rv_restaurantProducts.smoothScrollToPosition(position);
                }
            });
        }else {
            final float y = rv_restaurantProducts.getY()+150;
            svRestaurantDescription.post(new Runnable() {
                @Override
                public void run() {
                    svRestaurantDescription.fling(0);
                    svRestaurantDescription.smoothScrollTo(0, (int) y);
                    rv_restaurantProducts.smoothScrollToPosition(position);
                }
            });
        }

    }


}
