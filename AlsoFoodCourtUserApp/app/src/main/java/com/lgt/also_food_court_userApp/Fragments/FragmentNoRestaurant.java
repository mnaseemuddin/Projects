package com.lgt.also_food_court_userApp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lgt.also_food_court_userApp.R;


public class FragmentNoRestaurant extends Fragment {

    private ImageView iv_backToolbar;



    public FragmentNoRestaurant() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_restaurant, container, false);

        iv_backToolbar = view.findViewById(R.id.iv_backToolbar);

        iv_backToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                fragmentManager.popBackStack("FragmentNoRestaurant",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.popBackStack("Fragment_Banner",FragmentManager.POP_BACK_STACK_INCLUSIVE);


            }
        });
        return view;
    }

}
