package com.lgt.Ace11.MyTabFragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgt.Ace11.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KabaddiFragment extends Fragment {


    public KabaddiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_kabaddi, container, false);
        return  view;
    }

}
