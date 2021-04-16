package com.lgt.NeWay.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lgt.NeWay.Extra.NeWayApi;
import com.lgt.NeWay.Neway.R;
import com.lgt.NeWay.activity.ActivityChangePassword;
import com.lgt.NeWay.activity.ContactList.AddContactNumber;
import com.lgt.NeWay.activity.Amity.Amitys;
import com.lgt.NeWay.activity.BoardList.BaordList;
import com.lgt.NeWay.activity.ClassList.ClassList;
import com.lgt.NeWay.activity.Gallary.GallaryList;
import com.lgt.NeWay.activity.JobList.Jobs;
import com.lgt.NeWay.activity.Profile;
import com.lgt.NeWay.activity.Splash;
import com.lgt.NeWay.activity.SubjectList.SubjectList;
import com.lgt.NeWay.activity.TeacherList.TeacherList;
import com.lgt.NeWay.Extra.common;
import com.lgt.NeWay.activity.UploadOtherCertificate.UploadOtherCertificate;
import com.lgt.NeWay.activity.UserBatchRequest.UserBatchRequest;
import com.lgt.NeWay.activity.WebViewActivity;


public class MoreFragment extends Fragment {
 TextView tv_cancel,tv_countinue;
   SharedPreferences sharedPreferences;
   SharedPreferences.Editor editor;
   LinearLayout llMyProfile,llBoardlist,llLogOut,llSubjectlist,llClasstlist,llJobsApplied,llTeacherlist,ll_Aminitylist,llgallerylist,llUsercontacts,llAboutUs,llUserBatchRequestlist,llChangePassword,llOtherCertificate,llPrivacyPolicy;
   String UllLogOut,USERID;
    public AlertDialog download_dialog;
    Context context;
    String urlToOpen, urlType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_more, container, false);

        llMyProfile=view.findViewById(R.id.llMyProfile);
        llSubjectlist=view.findViewById(R.id.llSubjectlist);
        llLogOut=view.findViewById(R.id.llLogOut);
        llBoardlist=view.findViewById(R.id.llBoardlist);
        tv_cancel=view.findViewById(R.id.tv_cancel);
        tv_countinue=view.findViewById(R.id.tv_countinue);
        llClasstlist=view.findViewById(R.id.llClasstlist);
        llJobsApplied=view.findViewById(R.id.llJobsApplied);
        llTeacherlist=view.findViewById(R.id.llTeacherlist);
        ll_Aminitylist=view.findViewById(R.id.ll_Aminitylist);
        llgallerylist=view.findViewById(R.id.llgallerylist);
        llUsercontacts=view.findViewById(R.id.llUsercontacts);
        llAboutUs=view.findViewById(R.id.llAboutUs);
        llPrivacyPolicy=view.findViewById(R.id.llPrivacyPolicy);
        llUserBatchRequestlist=view.findViewById(R.id.llUserBatchRequestlist);
        llChangePassword=view.findViewById(R.id.llChangePassword);
        llOtherCertificate=view.findViewById(R.id.llOtherCertificate);

        llMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Profile.class);
                startActivity(intent);
            }
        });

        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                urlToOpen = NeWayApi.ABOUT_US;
                urlType = "About us";

                openWebView(urlToOpen, urlType);
            }
        });

        llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                urlToOpen = NeWayApi.PRIVACY_POLICY;
                urlType = "Privacy Policy";

                openWebView(urlToOpen, urlType);
            }
        });
        llBoardlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent=new Intent(getContext(), BaordList.class);
                startActivity(intent);
            }
        });

        llSubjectlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SubjectList.class);
                startActivity(intent);

            }
        });

        llClasstlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ClassList.class));
            }
        });

        llJobsApplied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Jobs.class));
            }
        });
        llTeacherlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TeacherList.class));
            }
        });
        ll_Aminitylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Amitys.class));
            }
        });

        llgallerylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), GallaryList.class));
            }
        });
        llUsercontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddContactNumber.class));
            }
        });
        llUserBatchRequestlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UserBatchRequest.class));
            }
        });
        llOtherCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UploadOtherCertificate.class));
            }
        });
        llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityChangePassword.class));
            }
        });

       llLogOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               openalert();

               Log.e("erorrrrrrrrrrr","ysssss");
               //

           }
       });


        return view;
    }

    private void openWebView(String urlToOpen, String typeOfURL) {

        Intent webViewIntent = new Intent(getActivity(), WebViewActivity.class);
        webViewIntent.putExtra("KEY_WEB_URL", urlToOpen);
        webViewIntent.putExtra("KEY_URL_TYPE", typeOfURL);
        startActivity(webViewIntent);
    }

    private void openalert() {
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.logoutalert, null);
        download_dialog = new AlertDialog.Builder(getActivity()).create();
        download_dialog.setView(customView);
        download_dialog.setCanceledOnTouchOutside(false);

        tv_cancel = customView.findViewById(R.id.tv_cancel);
        tv_countinue = customView.findViewById(R.id.tv_countinue);

        download_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        download_dialog.show();

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download_dialog.dismiss();

            }
        });
        tv_countinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inshareprefrence();
            }
        });
    }

    private void inshareprefrence() {
        sharedPreferences = getActivity().getSharedPreferences(common.UserData, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(common.UseriD)) {
            USERID = sharedPreferences.getString(common.UseriD, "");
            editor = sharedPreferences.edit();
            if (USERID!=null&& !USERID.isEmpty()){
                editor.clear();
                editor.commit();
                editor.apply();

                startActivity(new Intent(getContext(), Splash.class));

            }
            Log.e("hhhhhhhhhh",USERID);

        }
    }

}