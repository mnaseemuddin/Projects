package com.app.arthasattva.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.app.arthasattva.Api.DBConstants;
import com.app.arthasattva.R;
import com.app.arthasattva.databinding.FragmentHelpBottomSheetBinding;


public class HelpBottomSheet extends BottomSheetDialogFragment {

    private FragmentHelpBottomSheetBinding binding;

    private FirebaseFirestore firebaseFirestore;

    public HelpBottomSheet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_help_bottom_sheet, container, false);

        return binding.getRoot();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        getInfo();
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void getInfo() {
        new Handler().postDelayed(() -> getData(), 100);
    }

    private void getData() {

        firebaseFirestore.collection(DBConstants.App_Guidelines + "/" + DBConstants.guidlines_table_key + "/" + DBConstants.help)
                .document(DBConstants.help_document)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().exists()) {
                        if (task.getResult().getString(DBConstants.website) != null) {
                            binding.tvWebsite.setText(task.getResult().getString(DBConstants.website));
                        }
                        if (task.getResult().getString(DBConstants.email) != null) {
                            binding.tvEmail.setText(task.getResult().getString(DBConstants.email));
                        }
                    }
                });


    }
}