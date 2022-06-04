package com.lgt.also_food_court_userApp.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;

import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.google.gson.Gson;
import com.lgt.also_food_court_userApp.Adapters.AdapterBannerMain;
import com.lgt.also_food_court_userApp.Adapters.AdapterCategories;
import com.lgt.also_food_court_userApp.Adapters.AdapterNewFood;
import com.lgt.also_food_court_userApp.Adapters.AdapterRestaurantOffer;
import com.lgt.also_food_court_userApp.Adapters.AdapterTrending;
import com.lgt.also_food_court_userApp.Adapters.AdapterVegeterians;
import com.lgt.also_food_court_userApp.Adapters.AdapterVertical;
import com.lgt.also_food_court_userApp.Adapters.FamousRestaurentsAdapter;
import com.lgt.also_food_court_userApp.Fragments.ActivitySupport;
import com.lgt.also_food_court_userApp.Fragments.FragmentCart;
import com.lgt.also_food_court_userApp.Fragments.FragmentOffer;
import com.lgt.also_food_court_userApp.Fragments.FragmentOrderHistory;
import com.lgt.also_food_court_userApp.Fragments.FragmentProfile;
import com.lgt.also_food_court_userApp.Models.ModelRestaurantOffer;
import com.lgt.also_food_court_userApp.Models.ModelCategories;
import com.lgt.also_food_court_userApp.Models.ModelFamousNearYou;
import com.lgt.also_food_court_userApp.Models.ModelTrendingFoods;
import com.lgt.also_food_court_userApp.Models.ModelVegeterians;
import com.lgt.also_food_court_userApp.Models.ModelVertical;

import com.lgt.also_food_court_userApp.Models.FamousRestaurentsModel;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.beans.RestaurantOffer.Datum;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    View Nview;
    private CardView CV_track_order;
    private ImageView imageView;
    private TextView tv_user_name, tv_user_mail, tv_topBrandsTittle, tv_topBrandSubTittle,
            tv_famous_restaurant, veg_restaurantName,tv_TopDiscountedProduct,tv_noproductAvalable,
    tv_famousRestaurant,tv_topDiscountProduct,tv_tittleNewFood,tv_subTittleNewFood,tv_NewFoodProduct;

    private List<Address> addressList;

    private DrawerLayout mDrawer;
    private int i, j, k, l,m,n;
    private String pinCode,AreaName,fullAddress,MapActivityAreaName,MapActivityFullAddress;

   private LinearLayout ll_offer1, llHeader,ll_location;
    private TextView tv_currentLocation,tv_currentLocationAddress;

    //private FragmentManager mFragmentManager;
    //top_food_Offer
    private RecyclerView rv_food_offer;
    AdapterRestaurantOffer adapterRestaurantOffer;
    private List<ModelRestaurantOffer> foodOffer_list = new ArrayList<>();
    //

    //Discover new Place
    List<Datum> listBanner = new ArrayList<>();
    AdapterBannerMain adapterBannerMain;
    RecyclerView rv_banner_main;
    //Banner//

    //rvTopDiscountProduct
    private RecyclerView rvTopDiscountProduct;
    private List<ModelTrendingFoods> listTopDiscountedProduct = new ArrayList<>();
    private AdapterTrending adapterTrending;


    //TopBrands
    List<ModelCategories> listTopBrands = new ArrayList<>();
    AdapterCategories adapterCategories;
    RecyclerView rv_TopBrands;

    //Famous_restaurant
    private RecyclerView rv_Famous_Restaurants;
    private FamousRestaurentsAdapter famousRestaurentsAdapter;
    private ArrayList<FamousRestaurentsModel> listFamousRestaurant = new ArrayList<>();


    //NewFood
    List<ModelFamousNearYou> listNewFood = new ArrayList<>();
    AdapterNewFood adapterNewFood;
    RecyclerView rv_NewFood;

    //Famous items near by Vegeterians

    List<ModelVegeterians> listVegeterians = new ArrayList<>();
    AdapterVegeterians adapterVegeterians;
    RecyclerView rv_vegeterian_options;
    public static double latitude, longitude;
    //Extra work for dynamic adapter

    private ArrayList<ModelVertical> modelVerticalList;
    private AdapterVertical adapterVertical;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String user_id;


    private Fragment fragment = null;
    private FragmentManager fragmentManager;

    BottomNavigationView navigation;



    //new food

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics()
                , new CrashlyticsNdk());
        fragmentManager = getSupportFragmentManager();
        tv_currentLocationAddress=findViewById(R.id.tv_currentLocationAddress);
        ll_location=findViewById(R.id.ll_location);
        tv_currentLocation = findViewById(R.id.tv_currentLocation);
        tv_tittleNewFood=findViewById(R.id.tv_tittleNewFood);
        tv_subTittleNewFood=findViewById(R.id.tv_subTittleNewFood);




        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            getDeviceLocation();
        }


        sharedPreferences = getApplicationContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        user_id = sharedPreferences.getString("user_id", "");


        Intent intent=getIntent();
        if (intent!=null){
            if (intent.hasExtra("CITY_NAME")){
                MapActivityAreaName=intent.getStringExtra("CITY_NAME");
                MapActivityFullAddress=intent.getStringExtra("FullAddress");


                tv_currentLocation.setText(MapActivityAreaName);
                tv_currentLocationAddress.setText(MapActivityFullAddress);
            }else{
                getDeviceLocation();
            }

        }





        Initialization();


        getProfileDataApi();

        RestaurantOfferApi();
        loadBannerRecyclerView();





        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
        Nview = navigationView.getHeaderView(0);
        tv_user_name = Nview.findViewById(R.id.tv_user_name);
        tv_user_mail = Nview.findViewById(R.id.tv_user_mail);
        llHeader = Nview.findViewById(R.id.llHeader);
        imageView = Nview.findViewById(R.id.imageView);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getBackground().setAlpha(120);

        ll_offer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, new FragmentOffer()).addToBackStack("Fragment_Offers").commit();

            }
        });

        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });

        llHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.closeDrawers();
                Log.e("SAdsadadsadsad", "sadsadsadsad");
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, new FragmentProfile()).addToBackStack("Fragment_Profile").commit();
            }
        });


    }




    private void Initialization() {
        tv_NewFoodProduct=findViewById(R.id.tv_NewFoodProduct);
        tv_topDiscountProduct=findViewById(R.id.tv_topDiscountProduct);
        tv_famousRestaurant=findViewById(R.id.tv_famousRestaurant);
        tv_noproductAvalable=findViewById(R.id.tv_noproductAvalable);
        tv_TopDiscountedProduct=findViewById(R.id.tv_TopDiscountedProduct);
        veg_restaurantName = findViewById(R.id.veg_restaurantName);
        tv_famous_restaurant = findViewById(R.id.tv_famous_restaurant);
        tv_topBrandSubTittle = findViewById(R.id.tv_topBrandSubTittle);
        tv_topBrandsTittle = findViewById(R.id.tv_topBrandsTittle);
        rvTopDiscountProduct = findViewById(R.id.rvTopDiscountProduct);
        navigation = findViewById(R.id.btmNav);
        rv_banner_main = findViewById(R.id.rv_banner_main);
        rv_TopBrands = findViewById(R.id.rv_TopBrands);
        rv_NewFood = findViewById(R.id.rv_NewFood);
        rv_vegeterian_options = findViewById(R.id.rv_vegeterian_options);
        ll_offer1 = findViewById(R.id.ll_offer1);

        mDrawer = findViewById(R.id.drawer_layout);
        rv_food_offer = findViewById(R.id.rv_food_offer);
        rv_Famous_Restaurants = findViewById(R.id.rv_Famous_Restaurants);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CV_track_order = findViewById(R.id.CV_track_order);
        CV_track_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MyCurrentOrders.class);
                startActivity(intent);
            }
        });

    }


    private void RestaurantOfferApi() {
        foodOffer_list.clear();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, AlsoFoodCourtApi.Restaurant_OfferApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("GFSRE",response+"");
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equals("1")){
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        for (m=0;m<jsonArray.length();m++){
                            JSONObject RestaurantDataObject=jsonArray.getJSONObject(m);
                            String tbl_restaurant_id=RestaurantDataObject.getString("tbl_restaurant_id");
                            String restaurantName=RestaurantDataObject.getString("shop_name");
                            String offer=RestaurantDataObject.getString("offer");
                            String description=RestaurantDataObject.getString("description");
                            String address=RestaurantDataObject.getString("address");
                            String restaurant_image=RestaurantDataObject.getString("restaurant_image");

                            foodOffer_list.add(new ModelRestaurantOffer(tbl_restaurant_id,restaurant_image,restaurantName,offer));

                            adapterRestaurantOffer = new AdapterRestaurantOffer(MainActivity.this, foodOffer_list);
                            rv_food_offer.setNestedScrollingEnabled(false);
                            rv_food_offer.hasFixedSize();
                            rv_food_offer.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                            rv_food_offer.setAdapter(adapterRestaurantOffer);
                            adapterRestaurantOffer.notifyDataSetChanged();

                        }
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
                params.put("pincode",pinCode);

                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }


    /////////////////////////////////////////////////////////////////////////////////////////
    private void loadApiDataHomeApi() {
        listTopBrands.clear();
        listTopDiscountedProduct.clear();
        listFamousRestaurant.clear();
        listNewFood.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.HomeAllDataApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("KITREW", response + "");
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");

                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("top_disc_pro_data");
                                JSONObject jsonObjectTittle=jsonArray.getJSONObject(0);
                                String TopBrandTittle=jsonObjectTittle.getString("title");
                                tv_TopDiscountedProduct.setText(TopBrandTittle);

                                if (jsonArray.length()>0){
                                    listTopDiscountedProduct.clear();
                                    for (i = 1; i < jsonArray.length(); i++) {
                                        JSONObject topDiscountProductObject = jsonArray.getJSONObject(i);
                                        String productId = topDiscountProductObject.getString("tbl_restaurant_products_id");
                                        String restaurantId = topDiscountProductObject.getString("tbl_restaurant_id");
                                        String productName = topDiscountProductObject.getString("name");
                                        String productCategory = topDiscountProductObject.getString("category");
                                        String product_type = topDiscountProductObject.getString("product_type");
                                        String productImage = topDiscountProductObject.getString("image");
                                        String produPrice_type = topDiscountProductObject.getString("price_type");

                                        listTopDiscountedProduct.add(new ModelTrendingFoods(productId,productName, "Very Delicious Food",
                                                "3.5", "50%", productImage));
                                        adapterTrending = new AdapterTrending(listTopDiscountedProduct, getApplicationContext());
                                        rvTopDiscountProduct.setNestedScrollingEnabled(false);
                                        rvTopDiscountProduct.hasFixedSize();
                                        rvTopDiscountProduct.setLayoutManager
                                                (new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                        rvTopDiscountProduct.setAdapter(adapterTrending);
                                        adapterTrending.notifyDataSetChanged();


                                    }
                                }else{
                                    tv_topDiscountProduct.setVisibility(View.VISIBLE);
                                }


                                JSONArray jsonArray1 = jsonObject.getJSONArray("brand_data");
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                                String title = jsonObject1.getString("title");
                                String sub_title = jsonObject1.getString("sub_title");
                                tv_topBrandsTittle.setText(title);
                                tv_topBrandSubTittle.setText(sub_title);
                                listTopBrands.clear();
                                for (int j = 1; j < jsonArray1.length(); j++) {

                                    JSONObject topBrandDataObject = jsonArray1.getJSONObject(j);
                                    String tbl_brand_id = topBrandDataObject.getString("tbl_brand_id");
                                    String productName = topBrandDataObject.getString("name");
                                    String productImage = topBrandDataObject.getString("image");


                                    listTopBrands.add(new ModelCategories(tbl_brand_id, productName, productImage));

                                    adapterCategories = new AdapterCategories(listTopBrands, getApplicationContext());
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                    rv_TopBrands.setHasFixedSize(true);
                                    rv_TopBrands.setLayoutManager(linearLayoutManager);
                                    rv_TopBrands.setAdapter(adapterCategories);
                                    adapterCategories.notifyDataSetChanged();


                                }
                                JSONArray jsonArray2 = jsonObject.getJSONArray("restaurant_data");
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
                                String Tittle = jsonObject2.getString("title");
                                tv_famous_restaurant.setText(Tittle);
                                if (jsonArray2.length()>0){
                                    listFamousRestaurant.clear();
                                    for (k = 1; k < jsonArray2.length(); k++) {
                                        JSONObject restaurantDataObject = jsonArray2.getJSONObject(k);
                                        String tbl_restaurant_id = restaurantDataObject.getString("tbl_restaurant_id");
                                        String shop_name = restaurantDataObject.getString("shop_name");
                                        String description = restaurantDataObject.getString("description");
                                        String offer = restaurantDataObject.getString("offer");
                                        String open_time = restaurantDataObject.getString("open_time");
                                        String close_time = restaurantDataObject.getString("close_time");
                                        String address = restaurantDataObject.getString("address");
                                        String restaurant_image = restaurantDataObject.getString("restaurant_image");

                                        listFamousRestaurant.add(new FamousRestaurentsModel(tbl_restaurant_id,restaurant_image, description, shop_name, offer, open_time, close_time, address));


                                        famousRestaurentsAdapter = new FamousRestaurentsAdapter(getApplicationContext(), listFamousRestaurant);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                                        rv_Famous_Restaurants.setAdapter(famousRestaurentsAdapter);
                                        rv_Famous_Restaurants.setLayoutManager(linearLayoutManager);
                                        rv_Famous_Restaurants.setHasFixedSize(true);
                                        famousRestaurentsAdapter.notifyDataSetChanged();
                                    }
                                }else {
                                    tv_famousRestaurant.setVisibility(View.VISIBLE);
                                }


                                JSONArray jsonArray3 = jsonObject.getJSONArray("veg_restaurant");
                                JSONObject jsonObject3 = jsonArray3.getJSONObject(0);
                                String restaurantTitle = jsonObject3.getString("title");
                                veg_restaurantName.setText(restaurantTitle);
                                if (jsonArray3.length()>0){
                                    listVegeterians.clear();
                                    for (l = 1; l < jsonArray3.length(); l++) {
                                        JSONObject vegRestaurantObject = jsonArray3.getJSONObject(l);
                                        String tbl_restaurant_id = vegRestaurantObject.getString("tbl_restaurant_id");
                                        String shop_name = vegRestaurantObject.getString("shop_name");
                                        String description = vegRestaurantObject.getString("description");
                                        String offer = vegRestaurantObject.getString("offer");
                                        String open_time = vegRestaurantObject.getString("open_time");
                                        String close_time = vegRestaurantObject.getString("close_time");
                                        String address = vegRestaurantObject.getString("address");
                                        String restaurant_image = vegRestaurantObject.getString("restaurant_image");
                                        listVegeterians.add(new ModelVegeterians(tbl_restaurant_id, restaurant_image,
                                                shop_name, description, "4.5", offer, open_time, "meal", "250", close_time, address));

                                        adapterVegeterians = new AdapterVegeterians(listVegeterians, MainActivity.this);
                                        rv_vegeterian_options.hasFixedSize();
                                        rv_vegeterian_options.setNestedScrollingEnabled(false);
                                        rv_vegeterian_options.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                                        rv_vegeterian_options.setAdapter(adapterVegeterians);


                                    }
                                }else {
                                    tv_noproductAvalable.setVisibility(View.VISIBLE);
                                }

                                JSONArray jsonArray4=jsonObject.getJSONArray("new_food");
                                JSONObject TittleObject=jsonArray4.getJSONObject(0);
                                String TittleNewFood=TittleObject.getString("title");
                                String SubTittleNewFood=TittleObject.getString("sub_title");

                                if (jsonArray4.length()>0){
                                    listNewFood.clear();
                                    for (n=1;n<jsonArray4.length();n++){
                                        JSONObject newFoodObject=jsonArray4.getJSONObject(n);
                                        String tbl_restaurant_products_id=newFoodObject.getString("tbl_restaurant_products_id");
                                        String products_name=newFoodObject.getString("products_name");
                                        String category=newFoodObject.getString("category");
                                        String product_type=newFoodObject.getString("product_type");
                                        String image=newFoodObject.getString("image");
                                        String price_type=newFoodObject.getString("price_type");
                                        String quarter_price=newFoodObject.getString("quarter_price");
                                        String quarter_sprice=newFoodObject.getString("quarter_sprice");
                                        String quarter_discount=newFoodObject.getString("quarter_discount");
                                        String half_price=newFoodObject.getString("half_price");
                                        String half_sprice=newFoodObject.getString("half_sprice");
                                        String half_discount=newFoodObject.getString("half_discount");
                                        String full_price=newFoodObject.getString("full_price");
                                        String full_sprice=newFoodObject.getString("full_sprice");
                                        String full_discount=newFoodObject.getString("full_discount");
                                        String piece_price=newFoodObject.getString("piece_price");
                                        String piece_sprice=newFoodObject.getString("piece_sprice");
                                        String piece_discount=newFoodObject.getString("piece_discount");


                                        listNewFood.add(new ModelFamousNearYou(tbl_restaurant_products_id,image,products_name));

                                        adapterNewFood = new AdapterNewFood(listNewFood, MainActivity.this);
                                        rv_NewFood.hasFixedSize();
                                        rv_NewFood.setNestedScrollingEnabled(false);
                                        rv_NewFood.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                                        rv_NewFood.setAdapter(adapterNewFood);
                                        adapterNewFood.notifyDataSetChanged();

                                        tv_tittleNewFood.setText(TittleNewFood);
                                        tv_subTittleNewFood.setText(SubTittleNewFood);


                                    }
                                }else{
                                    tv_NewFoodProduct.setVisibility(View.VISIBLE);
                                    rv_NewFood.setVisibility(View.GONE);
                                }



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
                params.put("pincode", pinCode);
                Log.e("mfgh",params+"");
              //  params.put("pincode", "244222");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }



    public void getProfileDataApi() {
        Log.e("SAdsad", "hjkhjkhjkh");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.GetProfileData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        String profname = object.getString("name");
                        String profmobile = object.getString("mobile");
                        String profemail = object.getString("email");
                        String profimage = object.getString("user_image");
                        tv_user_name.setText(profname);
                        tv_user_mail.setText(profemail);
                        Glide.with(MainActivity.this).load(profimage).into(imageView);

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
                params.put("user_id", user_id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }




    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                    return true;

                case R.id.navigation_profile:

                    FragmentManager fragmentProfile = getSupportFragmentManager();
                    FragmentTransaction ftProfile = fragmentProfile.beginTransaction();
                    ftProfile.replace(R.id.frame_main, new FragmentProfile()).addToBackStack("Fragment_Profile").commit();
                    return true;

                case R.id.navigation_cart:
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_main, new FragmentCart()).addToBackStack("Fragment_Cart").commit();
                    return true;


                case R.id.navigation_offers:

                    FragmentManager fragmentoffer = getSupportFragmentManager();
                    FragmentTransaction ftOffer = fragmentoffer.beginTransaction();

                    Fragment fragmentOfferRestaurant = new FragmentOffer();

                    Bundle bundle=new Bundle();
                    bundle.putString("PinCode",pinCode);
                    fragmentOfferRestaurant.setArguments(bundle);
                    ftOffer.replace(R.id.frame_main, fragmentOfferRestaurant).addToBackStack("Fragment_Offers").commit();
                    return true;
            }

            return false;
        }
    };

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        getDeviceLocation();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }




    private void loadBannerRecyclerView() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AlsoFoodCourtApi.RESTAURANT_OFFER_BANNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("SAdsadsadsadsa", response + "");

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Gson gson = new Gson();
                    com.lgt.also_food_court_userApp.beans.RestaurantOffer.ModelRestaurantOffer modelRestaurantOffer = gson.fromJson(String.valueOf(jsonObject), com.lgt.also_food_court_userApp.beans.RestaurantOffer.ModelRestaurantOffer.class);

                    if (modelRestaurantOffer.getStatus().equals("1")) {
                        for (int i = 0; i < modelRestaurantOffer.getData().size(); i++) {
                            Log.e("Dsadsadsadsa", modelRestaurantOffer.getData().size() + "");

                            listBanner = modelRestaurantOffer.getData();

                            adapterBannerMain = new AdapterBannerMain(listBanner, MainActivity.this);
                            rv_banner_main.hasFixedSize();
                            rv_banner_main.setNestedScrollingEnabled(false);
                            rv_banner_main.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                            rv_banner_main.setAdapter(adapterBannerMain);
                            adapterBannerMain.notifyDataSetChanged();

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("dsdsadsadhsad", e + "");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("dsadadsdrtewtretret", error + "");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
      /*  if (mFragmentManager.getBackStackEntryCount() > 0) {
            for (int i = 0; i <= mFragmentManager.getBackStackEntryCount(); i++) {
                mFragmentManager.popBackStack();
                navigation.getMenu().getItem(0).setChecked(true);
            }
        } else {
            finish();
            navigation.getMenu().getItem(0).setChecked(true);
        }
    }*/
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        if (fragmentManager.getBackStackEntryCount() > 0) {
            for (int i = 0; i <= fragmentManager.getBackStackEntryCount(); i++) {
                fragmentManager.popBackStack();
                navigation.getMenu().getItem(0).setChecked(true);
            }
        } else {
            super.onBackPressed();
            navigation.getMenu().getItem(0).setChecked(true);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_order_history) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_main, new FragmentOrderHistory()).addToBackStack("Fragment_OrderHistory").commit();

        } else if (id == R.id.nav_offers) {

            FragmentManager fragmentoffer = getSupportFragmentManager();
            FragmentTransaction ftOffer = fragmentoffer.beginTransaction();

            Fragment fragmentOfferRestaurant = new FragmentOffer();

            Bundle bundle=new Bundle();
            bundle.putString("PinCode",pinCode);
            fragmentOfferRestaurant.setArguments(bundle);
            ftOffer.replace(R.id.frame_main, fragmentOfferRestaurant).addToBackStack("Fragment_Offers").commit();





        } else if (id == R.id.nav_cart) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_main, new FragmentCart()).addToBackStack("Fragment_Cart").commit();
        } else if (id == R.id.nav_share) {
            readyToShare();

        } /*else if (id == R.id.nav_support) {
           // startActivity(new Intent(MainActivity.this, ActivitySupport.class));

        }*/ else if (id == R.id.nav_logout) {
            editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent in = new Intent(MainActivity.this, RegisterActivity.class);

            startActivity(in);
            finishAffinity();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void readyToShare() {


            String contentToShare="Download "+getString(R.string.app_name)+" app at "+AlsoFoodCourtApi.play_store_url
                    +getPackageName();

            if (Patterns.WEB_URL.matcher(AlsoFoodCourtApi.play_store_url+getPackageName()).matches()){
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, contentToShare);

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share"));
            }else {
                //Commn.myToast(context,"Coming soon...");
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {


        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
          //  Log.e("JUHYGTFR",latitude+","+longitude+"");
            Geocoder geocoder = new Geocoder(MainActivity.this);
            try {
                addressList = geocoder.getFromLocation(latitude, longitude, 1);
                if (addressList!=null&&addressList.size()>0){
                    AreaName=addressList.get(0).getSubLocality();
                    fullAddress=addressList.get(0).getAddressLine(0);

                    if (AreaName!=null){
                        tv_currentLocation.setText(AreaName);
                        Log.e("jiorthg",AreaName+"");
                    }
                    if (fullAddress!=null){
                        tv_currentLocationAddress.setText(fullAddress);
                        Log.e("eryy",fullAddress.toString()+"");
                    }



                    pinCode = addressList.get(0).getPostalCode();
                    Log.e("dfjkhgj",pinCode+"");
                    Log.e("JHYUIK",AreaName+"");
                    Log.e("NBVCX",fullAddress+"");
                }



                if (!TextUtils.isEmpty(pinCode)) {
                    loadApiDataHomeApi();

                } else {
                    getDeviceLocation();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
