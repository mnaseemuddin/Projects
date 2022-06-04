package com.lgt.Ace11.FragmentBottomMenu;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lgt.Ace11.APICallingPackage.Class.APIRequestManager;
import com.lgt.Ace11.APICallingPackage.Interface.ResponseManager;
import com.lgt.Ace11.AddCashActivity;
import com.lgt.Ace11.Bean.UserDetails;
import com.lgt.Ace11.Config;
import com.lgt.Ace11.EditProfileActivity;
import com.lgt.Ace11.ForgotPasswordPackage.NewPasswordActivity;
import com.lgt.Ace11.HomeActivity;
import com.lgt.Ace11.MainActivity;
import com.lgt.Ace11.MyAccount.MyAccountActivity;
import com.lgt.Ace11.MyAccount.WithdrawAmountActivity;
import com.lgt.Ace11.PaymentOptionActivity;
import com.lgt.Ace11.R;
import com.lgt.Ace11.SessionManager;
import com.bumptech.glide.Glide;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.lgt.Ace11.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.Ace11.Config.MYACCOUNT;
import static com.lgt.Ace11.Config.UpdateUserProfileImage;
import static com.lgt.Ace11.Constants.MYACCOUNTTYPE;
import static com.lgt.Ace11.Constants.UpdateProfileImage;


public class ProfileFragment extends Fragment implements ResponseManager {
    TextView tv_ProfileChangePassword,tv_ProfileLogout,tv_ProfileYourMail,tv_ProfileAccount
            ,tv_ProfileAddBalance,tv_ProfileView,tv_ProfileDeposited,tv_ProfileWinning,
            tv_ProfileBonus,tv_ProfileUserName,tv_ProfileWithdrawl,tv_ROIWithdraw,tv_referral_code;
    private LinearLayout LL_Copy_App_Id;
    SessionManager sessionManager;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;
    CircleImageView circleImageView;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    HomeActivity activity;
    Context context;
    String Deposited,Winnings,Bonus;

    TextView tv_JoinedContest,tv_JoinedMatches,tv_JoinedSeries,tv_Wins;

    ImageView series,ranking;

    int PICK_IMAGE_GALLERY  = 100;
    int PICK_IMAGE_CAMERA = 101;
    CircleImageView im_ImagePreview;
    Bitmap bitmap;
    String UserEmail;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String Imageurl,IntentType = "", IntentEmail = "";
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        context = activity = (HomeActivity)getActivity();
        sessionManager = new SessionManager();
        initViews(v);
        getUserDetailsData();
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        responseManager = this;
        apiRequestManager = new APIRequestManager(getActivity());


        tv_ProfileChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NewPasswordActivity.class);
                i.putExtra("UserId", HomeActivity.sessionManager.getUser(getContext()).getUser_id());
                i.putExtra("IntentActivity", "ChangePassword");
                startActivity(i);
            }
        });

        tv_ROIWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowToast(context,"Coming soon...");
               // startActivity(new Intent(getActivity(), ROIWithdrawActivity.class));
            }
        });

        tv_ProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();

            }
        });
        tv_ProfileYourMail.setText(HomeActivity.sessionManager.getUser(getContext()).getEmail()+"");
         UserEmail = HomeActivity.sessionManager.getUser(getContext()).getName();

        Imageurl=HomeActivity.sessionManager.getUser(getContext()).getImage();

        Log.e("asasasas",""+Imageurl);
        if (!Imageurl.equalsIgnoreCase("default.jpg")){
            Glide.with(context).load(Config.ProfileIMAGEBASEURL+Imageurl).into(im_ImagePreview);
            Log.e("asasasas2222",""+Imageurl);
        }else if (Imageurl.equalsIgnoreCase("default.jpg")){
            Glide.with(context).load(Config.ProfileIMAGEBASEURL+"default.jpg").into(im_ImagePreview);
            Log.e("asasasas3333",""+Imageurl);
        }else {
            Glide.with(context).load(Imageurl).into(im_ImagePreview);
            Log.e("asasasas4444",""+Imageurl);
        }



        tv_ProfileAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(i);
            }
        });

        tv_ProfileAddBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.wtf("AddCashType", IntentEmail + " | " + IntentType);
                if (IntentType.equalsIgnoreCase("Social")) {
                   // if ( IntentEmail != null && "".equalsIgnoreCase(IntentEmail)) {
                        Intent i = new Intent(activity, PaymentOptionActivity.class);
                        startActivity(i);

                }
                if (IntentType.equalsIgnoreCase("Normal")) {
                    if ( IntentEmail != null && !"".equalsIgnoreCase(IntentEmail)) {
                        Intent i = new Intent(activity, PaymentOptionActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText(activity, "Please Add Email Address Or Phone.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity, EditProfileActivity.class);
                        startActivity(i);
                    }

            }

            }
        });    /*else {
                        Toast.makeText(activity, "Please Add Email Address.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity, EditProfileActivity.class);
                        startActivity(i);
                    }
                } else if (IntentType.equalsIgnoreCase("Normal")) {
                    if ( IntentEmail != null && !"".equalsIgnoreCase(IntentEmail)) {
                        Intent i = new Intent(activity, PaymentOptionActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(activity, "Please Add Email Address.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(activity, EditProfileActivity.class);
                        startActivity(i);
                    }*/


                /*Intent i = new Intent(getActivity(), AddCashActivity.class);
                startActivity(i);*/
                // showAlertDialog();


        tv_ProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(i);
            }
        });


        im_ImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions();
                }
                else {
                    ChooseImageDialog();
                }
            }
        });

        tv_ProfileWithdrawl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double Amount = Double.parseDouble(activity.getString(R.string.MinimumWithdrawAmountLimit));
                if (Double.parseDouble(Winnings)>=Amount) {
                    Intent i = new Intent(getActivity(), WithdrawAmountActivity.class);
                    i.putExtra("AvailableBalance", Winnings);
                    startActivity(i);
                }
                else {
                    ShowToast(context,"Not Enough Amount for Withdraw Request.");
                }
            }
        });

        callMyAccountDetails(true);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        callMyAccountDetails(true);
                                    }
                                }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callMyAccountDetails(false);

            }
        });

        return v;
    }

    private void getUserDetailsData() {
        if (sessionManager.getUser(context) != null) {
            IntentType = sessionManager.getUser(context).getType();
            IntentEmail = sessionManager.getUser(context).getEmail();
            Log.wtf("AddCashType", IntentEmail + " | " + IntentType);
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);
        LinearLayout llcoin = dialogView.findViewById(R.id.ll_bit_coin);
        ImageView cancel_btn = dialogView.findViewById(R.id.iv_cancel_btn);
        LinearLayout lltrackpay = dialogView.findViewById(R.id.ll_track_and_pay_cash);
        builder1.setView(dialogView);
        builder1.setCancelable(false);
        final AlertDialog alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        lltrackpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(activity, PaymentOptionActivity.class);
                startActivity(in);
                alert11.dismiss();
            }
        });
        llcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AddCashActivity.class);
                startActivity(i);
                alert11.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert11.dismiss();
            }
        });
        alert11.show();
    }

    public void initViews(View v){
        tv_ROIWithdraw = v.findViewById(R.id.tv_ROIWithdraw);
        tv_ProfileChangePassword = v.findViewById(R.id.tv_ProfileChangePassword);
        tv_ProfileLogout= v.findViewById(R.id.tv_ProfileLogout);
        tv_ProfileYourMail= v.findViewById(R.id.tv_ProfileYourMail);
        tv_ProfileAccount= v.findViewById(R.id.tv_ProfileAccount);
        tv_ProfileAddBalance= v.findViewById(R.id.tv_ProfileAddBalance);
        tv_ProfileView= v.findViewById(R.id.tv_ProfileView);

        tv_ProfileBonus= v.findViewById(R.id.tv_ProfileBonus);
        tv_ProfileDeposited= v.findViewById(R.id.tv_ProfileDeposited);
        tv_ProfileWinning= v.findViewById(R.id.tv_ProfileWinning);
        tv_ProfileUserName= v.findViewById(R.id.tv_ProfileUserName);
        swipeRefreshLayout= v.findViewById(R.id.swipeRefreshLayout);

        tv_JoinedSeries= v.findViewById(R.id.tv_JoinedSeries);
        tv_Wins= v.findViewById(R.id.tv_Wins);
        tv_referral_code= v.findViewById(R.id.tv_referral_code);
        LL_Copy_App_Id= v.findViewById(R.id.LL_Copy_App_Id);
        if (sessionManager.getUser(activity).getReferral_code()!=null){
            tv_referral_code.setText("Your App ID : "+sessionManager.getUser(activity).getReferral_code());

        }
        LL_Copy_App_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getUser(activity).getReferral_code()!=null){
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", sessionManager.getUser(activity).getReferral_code());
                    clipboard.setPrimaryClip(clip);
                    ShowToast(context,"App ID Copied");
                }

            }
        });

        tv_ProfileWithdrawl= v.findViewById(R.id.tv_ProfileWithdrawl);


        im_ImagePreview = v.findViewById(R.id.im_profilepic);
       // ranking=v.findViewById(R.id.ranking);
       // series=v.findViewById(R.id.series);
        //series.setColorFilter(Color.parseColor("#4301F7"), PorterDuff.Mode.SRC_IN);
       // ranking.setColorFilter(Color.parseColor("#4301F7"),PorterDuff.Mode.SRC_IN);


    }
    public void ChooseImageDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
        builderSingle.setTitle("Choose Image");
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(activity,
                        android.R.layout.simple_list_item_1);
        arrayAdapter.add("Camera");
        arrayAdapter.add("Gallery");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                dialog.dismiss();
                if (strName.equals("Gallery")){
                    bitmap = null;
                    im_ImagePreview.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
                }
                else{
                    bitmap = null;
                    im_ImagePreview.setVisibility(View.GONE);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
                }
            }
        });
        builderSingle.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE_GALLERY &&
                resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.
                        getBitmap(activity.getContentResolver(), filePath);
                im_ImagePreview.setVisibility(View.VISIBLE);
                im_ImagePreview.setImageBitmap(bitmap);
                Log.e("ldkfng",bitmap+"");
                callUploadDoc(true);
                Log.e("Image Path", "onActivityResult: "+filePath );
            } catch (Exception e) {
                bitmap = null;
                e.printStackTrace();
            }
        }

        if (resultCode==RESULT_OK) {

            if (requestCode == 101) {

                bitmap = (Bitmap) data.getExtras().get("data");
                im_ImagePreview.setVisibility(View.VISIBLE);
                im_ImagePreview.setImageBitmap(bitmap);
                Log.d("saaaaaaasf",bitmap+"");
                callUploadDoc(true);
                Log.e("Image Path", "onActivityResult: " );
            }


        }




       /* if (requestCode == PICK_IMAGE_CAMERA&&resultCode == RESULT_OK ) {
            try {


               *//* bitmap = (Bitmap) data.getExtras().get("data");
                im_ImagePreview.setVisibility(View.VISIBLE);
                im_ImagePreview.setImageBitmap(bitmap);
                Log.d("saaaaaaasf",bitmap+"");
                callUploadDoc(true);
                Log.e("Image Path", "onActivityResult: " );*//*
            } catch (Exception e) {
                bitmap = null;
                e.printStackTrace();

            }
        }*/

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission( getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions( (getActivity()),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        else {
            ChooseImageDialog();
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ChooseImageDialog();
            }
            else {

            }
            return;
        }
    }

    private void callMyAccountDetails(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(MYACCOUNT,
                    createRequestJsonWin(), context, activity, MYACCOUNTTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJsonWin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", HomeActivity.sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void callUploadDoc(boolean isShowLoader) {
        try {
            apiRequestManager.callAPI(UpdateUserProfileImage,
                    createRequestJson(), context, activity, UpdateProfileImage,
                    isShowLoader, responseManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            jsonObject.put("profile_image", getStringImage(bitmap));
            Log.e("dlkfgnjv",getStringImage(bitmap)+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        Log.e("shgahsga",result.toString());

        swipeRefreshLayout.setRefreshing(false);
         if(type.equals(UpdateProfileImage))
        {
            try {
               //String picmsg = result.getString("status");
                String imageurl=result.getString("data");
                Log.e("shgahsga",imageurl);
               // ShowToast(mContext,picmsg.toString());
                Log.e("shhhhss2222",result.getString("data")+"");
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.commit();
                UserDetails userDetails = new UserDetails();
                userDetails.setUser_id(HomeActivity.sessionManager.getUser(getContext()).getUser_id());
                userDetails.setName(HomeActivity.sessionManager.getUser(getContext()).getName());
                userDetails.setMobile(HomeActivity.sessionManager.getUser(getContext()).getMobile());
                userDetails.setEmail(HomeActivity.sessionManager.getUser(getContext()).getEmail());
                userDetails.setType(HomeActivity.sessionManager.getUser(getContext()).getType());
                userDetails.setReferral_code(HomeActivity.sessionManager.getUser(getContext()).getReferral_code());
                userDetails.setImage(imageurl);
                Glide.with(context).load(Config.ProfileIMAGEBASEURL+imageurl).into(im_ImagePreview);
                userDetails.setVerify(HomeActivity.sessionManager.getUser(getContext()).getVerify());
                sessionManager.setUser(context, userDetails);
                Log.e("picmgs",imageurl);
                Log.e("result",result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        else {
            try {
                Deposited = result.getString("credit_amount");
                Winnings = result.getString("winning_amount");
                Bonus = result.getString("bonous_amount");
                tv_ProfileDeposited.setText("" + Deposited);
                tv_ProfileWinning.setText("" + Winnings);
                tv_ProfileBonus.setText("" + Bonus);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void getResult2(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void Logout(){
        loginPrefsEditor.clear();
        loginPrefsEditor.commit();
        loginPrefsEditor.apply();
        //LoginManager.getInstance().logOut();
       /* Auth.GoogleSignInApi.revokeAccess(HomeActivity.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });*/

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        activity.finishAffinity();

    }

    @Override
    public void onResume() {
        super.onResume();

        getUserDetailsData();

        UserEmail = HomeActivity.sessionManager.getUser(getContext()).getName();
        Log.e("userkimail",UserEmail+"");

        Imageurl=HomeActivity.sessionManager.getUser(getContext()).getImage();

        Glide.with(context).load(Config.ProfileIMAGEBASEURL+Imageurl).into(im_ImagePreview);

       /* if (UserEmail.equals("null")){
            tv_ProfileUserName.setText("UserEmail");
        }else if (UserEmail.equals("")){
            tv_ProfileUserName.setText("UserEmail");
        }
        else {
            tv_ProfileUserName.setText(UserEmail);
        }*/
    }
}
