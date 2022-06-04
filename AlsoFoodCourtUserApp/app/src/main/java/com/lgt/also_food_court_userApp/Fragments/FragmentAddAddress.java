package com.lgt.also_food_court_userApp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.lgt.also_food_court_userApp.Activities.ActivityOrderReview;
import com.lgt.also_food_court_userApp.R;
import com.lgt.also_food_court_userApp.extra.AlsoFoodCourtApi;
import com.lgt.also_food_court_userApp.extra.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class FragmentAddAddress extends Fragment {

    private TextView tvToolbar, tv_district, tv_state, tv_country, tvAddNewAddress;
    private Spinner spinner_address, spinner_pincode;
    private ImageView iv_backToolbar;

    private EditText etPinCodeAddress, etNameAddress, etMobileAddress, etHousenoAddress, etFullAddress, etNearbyPlace;
    private String mName, mAddress, mMobile, mHouseNo, mNearby, mPinCode, mDistrict, mState, mAddressType, mAddID, mSpinnerPinCode, mUser, mUserID, realPinApi, city, district, state, country;

    final List<String> list = new ArrayList<>();
    List<String> statedata = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    private static final String TAG = "FragmentAddAddress";
    private boolean from_order_review=false;

    public FragmentAddAddress() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_address, container, false);

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        iv_backToolbar=view.findViewById(R.id.iv_backToolbar);
        tvToolbar = view.findViewById(R.id.tvToolbar);
        tvAddNewAddress = view.findViewById(R.id.tvAddNewAddress);
        etNameAddress = view.findViewById(R.id.etNameAddress);
        etMobileAddress = view.findViewById(R.id.etMobileAddress);
        etHousenoAddress = view.findViewById(R.id.etHousenoAddress);
        etFullAddress = view.findViewById(R.id.etFullAddress);
        etNearbyPlace = view.findViewById(R.id.etNearbyPlace);

        spinner_address = view.findViewById(R.id.spinner_address);
        etPinCodeAddress = view.findViewById(R.id.etPinCodeAddress);
        spinner_pincode = view.findViewById(R.id.spinner_pincode);
        tv_district = view.findViewById(R.id.tv_district);
        tv_state = view.findViewById(R.id.tv_state);
        tv_country = view.findViewById(R.id.tv_country);


        tvToolbar.setText("Add a new address");



        iv_backToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        if (sharedPreferences.contains("user_id")) {

            mUserID = sharedPreferences.getString("user_id", "");

        }

        tvAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateInput();

            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("KEY_ADD_ID")) {

                mAddID = bundle.getString("KEY_ADD_ID");
                etNameAddress.setText(bundle.getString("KEY_ADD_NAME"));
                etMobileAddress.setText(bundle.getString("KEY_ADD_MOBILE_NUMBER"));
                etHousenoAddress.setText(bundle.getString("KEY_HOUSE_NO"));
                etNearbyPlace.setText(bundle.getString("KEY_NEAR_BY"));
                etPinCodeAddress.setText(bundle.getString("KEY_PIN_CODE"));

            }
            if (bundle.containsKey(Common.from_order_review)){
                from_order_review=true;
            }
        }

        if(etPinCodeAddress.length()==6){
            list.clear();
            statedata.clear();
            apiCallForPincode();
        }

        etPinCodeAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 6) {
                    list.clear();
                    statedata.clear();
                    apiCallForPincode();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addDataSpinner();

        return view;
    }

    private void updateAddress() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.EDIT_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponseresponniin: "+response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equals("1")){
                        Toast.makeText(getActivity(), "Address updated", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();

                params.put("user_addressbook_id",mAddID);
                params.put("user_id", mUserID);
                params.put("title", mAddressType);
                params.put("name", mName);
                params.put("houseNo", mHouseNo);
                params.put("mobileNo", mMobile);
                params.put("email", "");
                params.put("nearBy", mNearby);
                params.put("district", tv_district.getText().toString().trim());
                params.put("city", city);
                params.put("location", "");
                params.put("pin_code", mPinCode);
                params.put("state", tv_state.getText().toString().trim());
                params.put("country", "India");
                params.put("address", mAddress);

                Log.e(TAG, "getParamsforeditaddress: "+params);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void validateInput() {

        mName = etNameAddress.getText().toString().trim();
        mMobile = etMobileAddress.getText().toString().trim();
        mHouseNo = etHousenoAddress.getText().toString().trim();
        mAddress = etFullAddress.getText().toString().trim();
        mNearby = etNearbyPlace.getText().toString().trim();
        mPinCode = etPinCodeAddress.getText().toString().trim();

        if (TextUtils.isEmpty(mName)) {
            etNameAddress.setError("Name is required");
            etNameAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mMobile)) {
            etMobileAddress.setError("Mobile is required");
            etMobileAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mHouseNo)) {
            etHousenoAddress.setError("House no is required");
            etHousenoAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mAddress)) {
            etFullAddress.setError("Full address is required");
            etFullAddress.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(mNearby)) {
            etNearbyPlace.setError("Landmark is required");
            etNearbyPlace.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mPinCode)) {
            etPinCodeAddress.setError("Landmark is required");
            etPinCodeAddress.requestFocus();
            return;
        }

        if(mAddressType.equalsIgnoreCase("Please select address type")){
            Toast.makeText(getActivity(), "Please select address type", Toast.LENGTH_SHORT).show();
            return;
        }

        if(city.equalsIgnoreCase("Please select your area")){
            Toast.makeText(getActivity(), "Please select address type", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mAddID!=null){
            if(!mAddID.equals("")){
                Log.e(TAG, "validateInputmaddid"+mAddID);
                updateAddress();
            }
        }


        else {
            Log.d(TAG, "validateInputcalled: ");
            apiSaveAddress();
        }

    }

    private void apiSaveAddress() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AlsoFoodCourtApi.ADD_NEW_ADDRESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "onResponse: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {

                        if (from_order_review){
                            forwordToOrderReview();
                        }else {
                            FragmentManager fragmentManager = getFragmentManager();
                            assert fragmentManager != null;
                            fragmentManager.popBackStack("FragmentAddAddress", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                        Toast.makeText(getActivity(), "Address added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Address not added", Toast.LENGTH_SHORT).show();

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
                params.put("title", mAddressType);
                params.put("name", mName);
                params.put("houseNo", mHouseNo);
                params.put("mobileNo", mMobile);
                params.put("email", "");
                params.put("nearBy", mNearby);
                params.put("district", tv_district.getText().toString().trim());
                params.put("city", city);
                params.put("location", "");
                params.put("pin_code", mPinCode);
                params.put("state", tv_state.getText().toString().trim());
                params.put("country", "India");
                params.put("address", mAddress);

                Log.e(TAG, "getParams: " + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void forwordToOrderReview() {
        Intent intent=new Intent(getActivity(), ActivityOrderReview.class);
        intent.putExtra(Common.address,mAddress);
        startActivity(intent);

    }


    private void apiCallForPincode() {

        String pincodeapi = "http://postalpincode.in/api/pincode/";

        realPinApi = pincodeapi.concat(etPinCodeAddress.getText().toString().trim());
        Log.e("reaalalPin", realPinApi);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, realPinApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("Status");
                    if (status.equals("Success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("PostOffice");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String name = data.getString("Name");
                            String dist = data.getString("District");
                            String state = data.getString("State");
                            String country = data.getString("Country");

                            list.add(name);
                            Log.d("LISTTTT", list + "");

                            statedata.add(dist);
                            statedata.add(state);
                            statedata.add(country);

                            fillPincodeSpinner();

                        }

                    } else {

                        Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(stringRequest);
        list.add(0, "Please select your area");


    }

    private void fillPincodeSpinner() {

        ArrayAdapter<String> pincodespinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, list);
        pincodespinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pincode.setAdapter(pincodespinnerAdapter);

        spinner_pincode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                city = adapterView.getItemAtPosition(i).toString();
                tv_district.setText(statedata.get(i));
                tv_state.setText(statedata.get(i + 1));
                tv_country.setText(statedata.get(i + 2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                city = "a";
            }
        });


    }


    private void addDataSpinner() {

        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Please select address type");
        spinnerItems.add("Home");
        spinnerItems.add("Office");
        spinnerItems.add("Shop");
        spinnerItems.add("Others");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_address.setAdapter(spinnerAdapter);

        spinner_address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mAddressType = adapterView.getItemAtPosition(i).toString();

                //   Toast.makeText(Add_address.this, "You clciekd" + address_type, Toast.LENGTH_SHORT).show();
                Log.d("Selected = ", mAddressType + "");

                if (adapterView.getItemAtPosition(i).toString().equals("Please select")) {

                   /* String selected = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(Add_address.this, "You clciekd"+selected, Toast.LENGTH_SHORT).show();*/
                    Log.d("Selected = ", mAddressType + "");

                } else {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
