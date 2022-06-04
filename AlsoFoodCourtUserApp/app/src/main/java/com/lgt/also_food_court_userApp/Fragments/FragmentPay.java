package com.lgt.also_food_court_userApp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.lgt.also_food_court_userApp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPay extends Fragment {

    Button btnPay;
    ImageView iv_back_pay;


    public FragmentPay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_pay, container, false);
        iv_back_pay = view.findViewById(R.id.iv_back_pay);
        btnPay = view.findViewById(R.id.btnPay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameOrderReview,new FragmentPaymentConfirmation()).addToBackStack("Fragment_Order_Confirmation").commit();
*/
            }
        });

        iv_back_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack("Fragment_Pay",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        return view;
    }

}
