package com.lgt.fxtradingleague.MyTabFragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgt.fxtradingleague.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootballFragment extends Fragment {


    public FootballFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_football, container, false);
        return view;
    }

}
