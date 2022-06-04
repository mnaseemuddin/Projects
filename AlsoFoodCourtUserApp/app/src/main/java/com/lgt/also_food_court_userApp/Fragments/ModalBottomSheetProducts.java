package com.lgt.also_food_court_userApp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.also_food_court_userApp.Adapters.AdapterBottomSheet;
import com.lgt.also_food_court_userApp.Models.ModelBottomSheet;
import com.lgt.also_food_court_userApp.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranjan on 11/8/2019.
 */
public class ModalBottomSheetProducts extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;

    private RecyclerView rvBottomSheet;
    private AdapterBottomSheet adapterBottomSheet;
    private List<ModelBottomSheet> listBottomSheet;

    private ImageView ivCloseBottomSheet;

    private LinearLayout llPieceOption,llItems;

    private static String mType = "";

    private RadioGroup radioGroupSize;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_products, container, false);


        rvBottomSheet = v.findViewById(R.id.rvBottomSheet);
        ivCloseBottomSheet = v.findViewById(R.id.ivCloseBottomSheet);
        llPieceOption = v.findViewById(R.id.llPieceOption);
        llItems = v.findViewById(R.id.llItems);
        radioGroupSize = v.findViewById(R.id.radioGroupSize);

        ivCloseBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Bundle bundle = getArguments();
        if(bundle!=null){
            if(bundle.containsKey("KEY_PRICE_TYPE")){
                mType = bundle.getString("KEY_PRICE_TYPE");

                if(mType.equalsIgnoreCase("Size")){
                    llPieceOption.setVisibility(View.VISIBLE);
                    llItems.setVisibility(View.GONE);

                }

                else {
                    llPieceOption.setVisibility(View.GONE);
                    llItems.setVisibility(View.VISIBLE);
                }

                Log.e("sadsartdrtrdt",mType+"");

            }
        }

        radioGroupSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(i==R.id.radioFull){
                    Toast.makeText(getActivity(), "Full clicked", Toast.LENGTH_SHORT).show();
                }
                if(i==R.id.radioHalf){
                    Toast.makeText(getActivity(), "Half clicked", Toast.LENGTH_SHORT).show();
                }
                if(i==R.id.radioQuarter){
                    Toast.makeText(getActivity(), "Quarter clicked", Toast.LENGTH_SHORT).show();
                }

            }
        });

        loadBottomRecyclerView();

       /* Button button1 = v.findViewById(R.id.button1);
        Button button2 = v.findViewById(R.id.button2);*/
        /*button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Button 1 clicked");
                dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Button 2 clicked");
                dismiss();
            }
        });*/

        return v;
    }

    private void loadBottomRecyclerView() {

        listBottomSheet = new ArrayList<>();

        listBottomSheet.add(new ModelBottomSheet("", "Burger", "80.00", "60.00", "20.00", "1", "Customizable"));
        listBottomSheet.add(new ModelBottomSheet("", "Burger", "80.00", "60.00", "20.00", "2", "Customizable"));
        listBottomSheet.add(new ModelBottomSheet("", "Burger", "80.00", "60.00", "20.00", "1", "Customizable"));


        adapterBottomSheet = new AdapterBottomSheet(listBottomSheet, getActivity());
        rvBottomSheet.hasFixedSize();
        rvBottomSheet.setNestedScrollingEnabled(false);
        rvBottomSheet.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvBottomSheet.setAdapter(adapterBottomSheet);

    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
