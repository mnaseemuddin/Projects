package com.lgt.Ace11.FragmentBottomMenu;


import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.Ace11.R;

import java.util.Objects;


public class BottomSheetMatchStatus extends BottomSheetDialogFragment {

   private TextView tvOk;

    public BottomSheetMatchStatus() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_match_status, container, false);
        tvOk = view.findViewById(R.id.tvOk);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if(Objects.requireNonNull(getDialog()).isShowing()){
                  getDialog().dismiss();
              }
            }
        });
        return view;
    }


}
