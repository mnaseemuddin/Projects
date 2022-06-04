package com.lgt.fxtradingleague.FragmentBottomMenu;


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

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lgt.fxtradingleague.APICallingPackage.Class.Validations;
import com.lgt.fxtradingleague.AddCashActivity;
import com.lgt.fxtradingleague.Config;
import com.lgt.fxtradingleague.EditProfileActivity;
import com.lgt.fxtradingleague.Extra.VolleyMultipartRequest;
import com.lgt.fxtradingleague.ForgotPasswordPackage.NewPasswordActivity;
import com.lgt.fxtradingleague.HomeActivity;
import com.lgt.fxtradingleague.MainActivity;
import com.lgt.fxtradingleague.MyAccount.MyAccountActivity;
import com.lgt.fxtradingleague.MyAccount.UploadKYCActivity;
import com.lgt.fxtradingleague.R;
import com.lgt.fxtradingleague.ROIWithdrawActivity;
import com.lgt.fxtradingleague.RazorPayments.AddPaymentActivity;
import com.lgt.fxtradingleague.SessionManager;
import com.lgt.fxtradingleague.TrakNPayPackage.PaymetPackageActivity;
import com.lgt.fxtradingleague.WithdrawListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.lgt.fxtradingleague.APICallingPackage.Class.Validations.ShowToast;
import static com.lgt.fxtradingleague.Extra.ExtraData.PROFILE_API;
import static com.lgt.fxtradingleague.Extra.ExtraData.UPDATE_PROFILE_IMAGE_API;


public class ProfileFragment extends Fragment {
    TextView tv_Withdraw, tv_ProfileChangePassword, tv_ProfileLogout, tv_ProfileYourMail, tv_ProfileAccount, tv_ProfileAddBalance, tv_ProfileView, tv_ProfileDeposited, tv_ProfileWinning,
            tv_ProfileBonus, tv_ProfileUserName, tv_ProfileWithdrawl, tv_ROIWithdraw, tv_referral_code, tv_WinningAmount,
            tv_UploadPan, tv_UploadAadhaar, tv_WithdrawHistory;
    private LinearLayout LL_Copy_App_Id;
    SessionManager sessionManager;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;
    CircleImageView circleImageView;
    HomeActivity activity;
    Context context;
    String Deposited, Winnings, Bonus, user_id, PanStatus = "0", AadhaarStatus = "0";

    TextView tv_JoinedContest, tv_JoinedMatches, tv_JoinedSeries, tv_Wins;

    ImageView series, ranking, im_AadhaarVerified, im_PanVerified;

    int PICK_IMAGE_GALLERY = 100;
    int PICK_IMAGE_CAMERA = 101;
    CircleImageView im_ImagePreview;
    Bitmap bitmap;
    String UserEmail;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String Imageurl;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        context = activity = (HomeActivity) getActivity();
        sessionManager = new SessionManager();
        initViews(v);
        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        tv_ProfileChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NewPasswordActivity.class);
                i.putExtra("UserId", HomeActivity.sessionManager.getUser(getContext()).getUser_id());
                i.putExtra("IntentActivity", "ChangePassword");
                startActivity(i);
            }
        });

        if (saveLogin) {
            user_id = sessionManager.getUser(getActivity()).getUser_id();
        }
        Log.d("yes_login", "" + user_id);
        tv_ROIWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowToast(context, "Coming soon...");
                // startActivity(new Intent(getActivity(), ROIWithdrawActivity.class));
            }
        });

        tv_ProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
        tv_ProfileYourMail.setText(HomeActivity.sessionManager.getUser(getContext()).getEmail() + "");
        UserEmail = HomeActivity.sessionManager.getUser(getContext()).getName();

        Imageurl = HomeActivity.sessionManager.getUser(getContext()).getImage();

        Log.e("asasasas", "" + Imageurl);
        if (!Imageurl.equalsIgnoreCase("default.jpg")) {
            Glide.with(context).load(Config.ProfileIMAGEBASEURL + Imageurl).into(im_ImagePreview);
            Log.e("asasasas2222", "" + Imageurl);
        } else if (Imageurl.equalsIgnoreCase("default.jpg")) {
            Glide.with(context).load(Config.ProfileIMAGEBASEURL + "default.jpg").into(im_ImagePreview);
            Log.e("asasasas3333", "" + Imageurl);
        } else {
            Glide.with(context).load(Imageurl).into(im_ImagePreview);
            Log.e("asasasas4444", "" + Imageurl);
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
                startActivity(new Intent(getContext(), AddPaymentActivity.class));
            }
        });
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
                } else {
                    ChooseImageDialog();
                }
            }
        });

//        tv_ProfileWithdrawl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                double Amount = Double.parseDouble(activity.getString(R.string.MinimumWithdrawAmountLimit));
//                if (Double.parseDouble(Winnings) >= Amount) {
//                    Intent i = new Intent(getActivity(), WithdrawAmountActivity.class);
//                    i.putExtra("AvailableBalance", Winnings);
//                    startActivity(i);
//                } else {
//                    ShowToast(context, "Not Enough Amount for Withdraw Request.");
//                }
//            }
//        });

        callMyAccountDetails(true);
        return v;
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
                Toast.makeText(context, "Payment", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, PaymetPackageActivity.class);
                startActivity(i);
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

    public void initViews(View v) {
        // im_AadhaarVerified,im_PanVerified
        tv_ROIWithdraw = v.findViewById(R.id.tv_ROIWithdraw);
        tv_Withdraw = v.findViewById(R.id.tv_Withdraw);
        tv_WithdrawHistory = v.findViewById(R.id.tv_WithdrawHistory);
        im_PanVerified = v.findViewById(R.id.im_PanVerified);
        im_AadhaarVerified = v.findViewById(R.id.im_AadhaarVerified);
        tv_UploadPan = v.findViewById(R.id.tv_UploadPan);
        tv_UploadAadhaar = v.findViewById(R.id.tv_UploadAadhaar);
        tv_WinningAmount = v.findViewById(R.id.tv_WinningAmount);
        tv_ProfileChangePassword = v.findViewById(R.id.tv_ProfileChangePassword);
        tv_ProfileLogout = v.findViewById(R.id.tv_ProfileLogout);
        tv_ProfileYourMail = v.findViewById(R.id.tv_ProfileYourMail);
        tv_ProfileAccount = v.findViewById(R.id.tv_ProfileAccount);
        tv_ProfileAddBalance = v.findViewById(R.id.tv_ProfileAddBalance);
        tv_ProfileView = v.findViewById(R.id.tv_ProfileView);

        tv_ProfileBonus = v.findViewById(R.id.tv_ProfileBonus);
        tv_ProfileDeposited = v.findViewById(R.id.tv_ProfileDeposited);
        tv_ProfileWinning = v.findViewById(R.id.tv_ProfileWinning);
        tv_ProfileUserName = v.findViewById(R.id.tv_ProfileUserName);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
      //  tv_JoinedSeries = v.findViewById(R.id.tv_JoinedSeries);
      //  tv_Wins = v.findViewById(R.id.tv_Wins);
        tv_referral_code = v.findViewById(R.id.tv_referral_code);
        LL_Copy_App_Id = v.findViewById(R.id.LL_Copy_App_Id);
        if (sessionManager.getUser(activity).getReferral_code() != null) {
            tv_referral_code.setText("Your App ID : " + sessionManager.getUser(activity).getReferral_code());

        }
        LL_Copy_App_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getUser(activity).getReferral_code() != null) {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", sessionManager.getUser(activity).getReferral_code());
                    clipboard.setPrimaryClip(clip);
                    ShowToast(context, "App ID Copied");
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        tv_WithdrawHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WithdrawListActivity.class);
                startActivity(i);
            }
        });

        tv_UploadPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, UploadKYCActivity.class);
                i.putExtra("DocType", "pan");
                startActivity(i);
            }
        });

        tv_UploadAadhaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, UploadKYCActivity.class);
                i.putExtra("DocType", "aadhar");
                startActivity(i);
            }
        });

        tv_ProfileWithdrawl = v.findViewById(R.id.tv_ProfileWithdrawl);


        im_ImagePreview = v.findViewById(R.id.im_profilepic);
        // ranking=v.findViewById(R.id.ranking);
        // series=v.findViewById(R.id.series);
        //series.setColorFilter(Color.parseColor("#4301F7"), PorterDuff.Mode.SRC_IN);
        // ranking.setColorFilter(Color.parseColor("#4301F7"),PorterDuff.Mode.SRC_IN);

        tv_Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValidateWithDrawal();
            }
        });
    }

    private void getValidateWithDrawal() {
        double Amount = Double.parseDouble(activity.getString(R.string.MinimumWithdrawAmountLimit));
        if (PanStatus.equals("2") && AadhaarStatus.equals("2")) {
            if (Double.parseDouble(tv_WinningAmount.getText().toString().trim()) >= 100) {
                // ShowToast(context, "Amount for Withdraw Request.");
                Intent intent = new Intent(getContext(), ROIWithdrawActivity.class);
                intent.putExtra("WinningWallet", tv_WinningAmount.getText().toString().trim());
                startActivity(intent);
            } else {
                ShowToast(context, "Not Enough Amount for Withdraw Request.");
            }
        } else {
            ShowToast(context, " Update your KYC document for withdraw amount.");
        }
    }

    public void ChooseImageDialog() {
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
                if (strName.equals("Gallery")) {
                    bitmap = null;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
                } else {
                    bitmap = null;
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
                }
            }
        });
        builderSingle.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.
                        getBitmap(activity.getContentResolver(), filePath);
                Glide.with(getActivity()).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(im_ImagePreview);
                Log.e("ldkfng", bitmap + "");
                sendImage();
                Log.e("Image Path", "onActivityResult: " + filePath);
            } catch (Exception e) {
                bitmap = null;
                e.printStackTrace();
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                bitmap = (Bitmap) data.getExtras().get("data");
                Glide.with(getActivity()).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(im_ImagePreview);
                Log.d("saaaaaaasf", bitmap + "");
                sendImage();
                Log.e("Image Path", "onActivityResult: ");
            }
        }
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                Glide.with(getActivity()).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(im_ImagePreview);
                Log.d("saaaaaaasf", bitmap + "");
                sendImage();
                Log.e("Image Path", "onActivityResult: ");
            } catch (Exception e) {
                bitmap = null;
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
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
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((getActivity()),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), 100);
            return false;
        } else {
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
            } else {

            }
            return;
        }
    }

    private void callMyAccountDetails(boolean isShowLoader) {
        Validations.showProgress(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyAccountDetails", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONObject innerData = jsonObject.getJSONObject("data");
                        String name = innerData.getString("name");
                        String mobile = innerData.getString("mobile");
                        String email = innerData.getString("email");
                        String winning_amt = innerData.getString("winning_amt");
                        String total_wallet_amount = innerData.getString("total_wallet_amount");
                        String deposite_amt = innerData.getString("deposite_amt");
                        String bonus_amt = innerData.getString("bonus_amt");
                        String aadhar_status = innerData.getString("aadhar_status");
                        String pan_status = innerData.getString("pan_status");
                        PanStatus = pan_status;
                        AadhaarStatus = aadhar_status;
                        String birthday = innerData.getString("birthday");
                        String referral_code = innerData.getString("referral_code");
                        String user_image = innerData.getString("user_image");
                        tv_ProfileDeposited.setText(deposite_amt);
                        tv_ProfileWinning.setText(winning_amt);
                        tv_ProfileBonus.setText(total_wallet_amount);
                        tv_referral_code.setText(referral_code);
                        tv_ProfileUserName.setText(name);
                        tv_WinningAmount.setText(winning_amt);
                        checkPanOrAadharStatus(aadhar_status, pan_status);
                        if (getActivity() != null) {
                            Glide.with(getActivity()).load(user_image).apply(new RequestOptions()
                                    .override(192, 192)).into(im_ImagePreview);
                        }
                        Validations.hideProgress();
                    } else {
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        Validations.hideProgress();
                    }

                } catch (JSONException e) {
                    Validations.hideProgress();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.hideProgress();
                Log.d("MyAccountDetails", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", user_id);
                Log.d("MyAccountDetails", "" + param);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void checkPanOrAadharStatus(String aadhar_status, String pan_status) {
        if (pan_status.equals("0")) {
            im_PanVerified.setVisibility(View.INVISIBLE);
            tv_UploadPan.setEnabled(true);
        } else if (pan_status.equals("1")) {
            im_PanVerified.setVisibility(View.VISIBLE);
            im_PanVerified.setImageResource(R.drawable.pending_icon);
            tv_UploadPan.setText("Pending");
            tv_UploadPan.setEnabled(false);
        } else if (pan_status.equals("2")) {
            im_PanVerified.setVisibility(View.VISIBLE);
            im_PanVerified.setImageResource(R.drawable.verify_icon);
            tv_UploadPan.setText("Verified");
            tv_UploadPan.setEnabled(false);
        } else {
            im_PanVerified.setVisibility(View.INVISIBLE);
            tv_UploadPan.setText("Upload");
            tv_UploadPan.setEnabled(true);
        }

        if (aadhar_status.equals("0")) {
            im_AadhaarVerified.setVisibility(View.INVISIBLE);
            tv_UploadAadhaar.setEnabled(true);
        } else if (aadhar_status.equals("1")) {
            im_AadhaarVerified.setVisibility(View.VISIBLE);
            im_AadhaarVerified.setImageResource(R.drawable.pending_icon);
            tv_UploadAadhaar.setText("Pending");
            tv_UploadAadhaar.setEnabled(false);

        } else if (aadhar_status.equals("2")) {
            im_AadhaarVerified.setVisibility(View.VISIBLE);
            im_AadhaarVerified.setImageResource(R.drawable.verify_icon);
            tv_UploadAadhaar.setText("Verified");
            tv_UploadAadhaar.setEnabled(false);
        } else {
            im_AadhaarVerified.setVisibility(View.INVISIBLE);
            tv_UploadAadhaar.setText("Upload");
            tv_UploadAadhaar.setEnabled(true);
        }
//        if (PanStatus.equals("2")&&AadhaarStatus.equals("2")) {
//
//        }else{
//
//        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void sendImage() {
        Validations.showProgress(getActivity());
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, UPDATE_PROFILE_IMAGE_API,
                new com.android.volley.Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("UPLOADDDD", response + "");

                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            // pbProfile.setVisibility(View.GONE);
                            Log.e("UPLOADDDD", status + " | " + message);
                            if (status.equals("1")) {
                                Validations.hideProgress();
                                callMyAccountDetails(true);
                                Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Validations.hideProgress();
                                Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Validations.hideProgress();
                            e.printStackTrace();
                        }
                        Log.e("MULTIPART", response + "");

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Validations.hideProgress();
                Log.e("MULTIPART", error.getMessage() + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sessionManager.getUser(context).getUser_id());
                Log.e("PPPPPPP", params + "");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                Log.e("PARAMS", params + "");
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }




    /* @Override
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
    }*/

    public void Logout() {
        loginPrefsEditor.clear();
        loginPrefsEditor.commit();
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
      //  callMyAccountDetails(false);
        /*UserEmail = HomeActivity.sessionManager.getUser(getContext()).getName();

        Imageurl = HomeActivity.sessionManager.getUser(getContext()).getImage();
        Glide.with(context).load(Config.ProfileIMAGEBASEURL + Imageurl).into(im_ImagePreview);

        if (UserEmail.equals("null")) {
            tv_ProfileUserName.setText("Username");
        } else if (UserEmail.equals("")) {
            tv_ProfileUserName.setText("Username");
        } else {
            tv_ProfileUserName.setText(UserEmail);
        }*/
    }
}
