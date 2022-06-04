package com.app.cryptok.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.app.cryptok.databinding.ActivityLoginBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.app.cryptok.Api.DBConstants;
import com.app.cryptok.R;

import com.app.cryptok.model.ProfilePOJO;
import com.app.cryptok.utils.Commn;
import com.app.cryptok.utils.ConstantsKey;
import com.app.cryptok.utils.SessionManager;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private LoginActivity activity;
    private String user_name;
    private boolean isfilled = false;
    private SessionManager sessionManager;
    //forgot password
    private FirebaseAuth mAuth;
    private static final String EMAIL = "email";
    AccessToken accessTokan;
    //database
    //userinfo
    private String LOGIN_TYPE = "";
    private String USER_IMAGE = "";
    private String USER_NAME = "";
    private String USER_MOBILE = "";
    private String USER_EMAIL = "";
    private String USER_ID = "";
    //choose country
    private CountryPicker countryPicker;
    private String countryCode = "+91", country_name = "India";

    private boolean isUserExists = false;
    //
    ProfilePOJO profilePOJO;
    FirebaseFirestore firebaseFirestore;
    private ActivityLoginBinding binding;

    //facebook
    CallbackManager callbackManager;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = activity = this;
        mAuth = FirebaseAuth.getInstance();
        iniFirebase();
        iniViews();
        printHashKey(this);

    }

    private void iniFirebase() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    // private final static String TAG=LoginActivity.class.getName();
    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Commn.showErrorLog(TAG + "printHashKey() Hash Key: " + hashKey + "");
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
            e.printStackTrace();
        }
    }

    private void iniViews() {

        sessionManager = new SessionManager(this);

        /*binding.tvGoToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(context,SignUpActivity.class));

            }
        });*/
        callbackManager = CallbackManager.Factory.create();
        binding.llFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.loginButton.performClick();
                loginWithFacebook();

            }
        });


        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //   requestHint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // openForgotDialog();
            }
        });

        binding.googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        binding.llGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // google_sign_in.performClick();
                signIn();
            }
        });

        //signInWithGoogle();
        handleClick();
    }

    private void loginWithFacebook() {

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = binding.loginButton;
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Commn.showDebugLog("facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Commn.showDebugLog("facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Commn.showDebugLog("facebook:onError" + error);
                // ...
            }
        });


    }

    private void handleFacebookAccessToken(AccessToken token) {
        Commn.showDebugLog("handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateFacebookUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Commn.showDebugLog("signInWithCredential:failure" + task.getException());


                    }

                    // ...
                });
    }

    private void updateFacebookUI(FirebaseUser user) {

        USER_NAME = user.getDisplayName() + "";
        USER_EMAIL = "";
        USER_ID = user.getUid() + "";
        //String image=
        LOGIN_TYPE = Commn.FACEBOOK_TYPE;
        USER_IMAGE = "https://graph.facebook.com/" + user.getProviderId() + "/picture?width=9999";

        Commn.showDebugLog("getFacebookData:" + new Gson().toJson(user).toString() + ",image:" + USER_IMAGE);

        checkUser();
    }

    private void handleClick() {

        binding.llChooseCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countryPicker = CountryPicker.newInstance("Select Country");  // dialog title
                countryPicker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String full_country_name, String short_country_name, String countrycode, int icon) {

                        binding.tvCountryCode.setText(countrycode);
                        country_name = full_country_name;
                        binding.ivCountryImage.setImageResource(icon);
                        countryPicker.dismiss();
                        Commn.showDebugLog("short_country_name:" + short_country_name + ",country_full_name:" + country_name + ",country_code:" + countrycode + "");
                    }
                });

                countryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");


            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateLogin();
                // startActivity(new Intent(LoginActivity.this,OtpActivity.class));
            }
        });
    }


    private void validateLogin() {
        user_name = binding.etMobile.getText().toString().trim();
        countryCode = binding.tvCountryCode.getText().toString();
        //  password=et_password.getText().toString().trim();
        isfilled = true;
        if (TextUtils.isEmpty(user_name)) {
            binding.etMobile.requestFocus();
            binding.etMobile.setError("Enter user name");
            isfilled = false;
        }
        if (TextUtils.isEmpty(countryCode) && TextUtils.isEmpty(country_name)) {
            binding.tvCountryCode.requestFocus();
            Commn.myToast(context, "please choose country type...");
            isfilled = false;
        }
        if (isfilled) {

            signInWithMobile();

        }

    }

    String verificationid;

    private void signInWithMobile() {

        //iniMobileLogin();
        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(countryCode + user_name)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        if (task.getResult().getString(DBConstants.auth_status) != null) {
                            String status = task.getResult().getString(DBConstants.auth_status);
                            if ("false".equalsIgnoreCase(status)) {
                                USER_ID = countryCode + user_name;
                                showBlockMsg();

                            } else {
                                iniMobileLogin();
                            }
                        } else {
                            iniMobileLogin();
                        }
                    } else {
                        iniMobileLogin();
                    }

                });

    }

    /************   Google Login   *******************/
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 100;

    private void iniMobileLogin() {
        binding.btLoginProgress.setVisibility(View.VISIBLE);
        binding.btnLogin.setVisibility(View.GONE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                countryCode + binding.etMobile.getText().toString().trim(), 30L /*timeout*/, TimeUnit.SECONDS,
                this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationid = verificationId;
                        // Save the verification id somewhere
                        // ...


                        binding.btLoginProgress.setVisibility(View.GONE);
                        binding.btnLogin.setVisibility(View.VISIBLE);
                        // The corresponding whitelisted code above should be used to complete sign-in.
                        Commn.showErrorLog("VerificationId : " + verificationId + "\nResendToken : " + forceResendingToken.toString());
                        Intent intent = new Intent(context, OtpActivity.class);
                        intent.putExtra(ConstantsKey.VERIFICATION_KEY, verificationId);
                        intent.putExtra(Commn.USER_MOBILE, countryCode + binding.etMobile.getText().toString().trim());
                        intent.putExtra(DBConstants.country_name, country_name);
                        intent.putExtra(DBConstants.country_code, countryCode);
                        startActivity(intent);
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                        // ...
                        Commn.showErrorLog(phoneAuthCredential.toString());
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // ...
                        Commn.showErrorLog(e.getMessage());
                        Commn.showShortToast(context, "Otp Verifaction Failed");
                    }

                });
    }

    private void showBlockMsg() {
        firebaseFirestore.collection(DBConstants.block_info)
                .document(USER_ID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        String msg = task.getResult().getString(DBConstants.block_message);
                        Commn.myToast(context, msg);
                    } else {
                        firebaseFirestore.collection(DBConstants.GlobalBlockMessage)
                                .document(DBConstants.global_block_doc)
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.getResult().exists()) {
                                        String msg = task1.getResult().getString(DBConstants.block_message);
                                        Commn.myToast(context, msg);
                                    }
                                });

                    }
                });
    }

    /*private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        try {
            mGoogleSignInClient.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (data != null && callbackManager != null)
                callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Commn.showDebugLog("firebaseAuthWithGoogle:" + account.getIdToken());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Commn.showErrorLog("signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            handleSignInResult(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Commn.showErrorLog("signInWithCredential:failure" + task.getException());


                        }

                        // ...
                    }
                });
    }

    public static final String TAG = LoginActivity.class.getName();

    private void handleSignInResult(FirebaseUser completedTask) {
        try {
            Commn.showErrorLog(new Gson().toJson(completedTask));

            if (completedTask != null) {
                USER_NAME = completedTask.getDisplayName();
                USER_EMAIL = completedTask.getEmail();
                Uri personPhoto = completedTask.getPhotoUrl();

                if (personPhoto == null) {
                    USER_IMAGE = DBConstants.placeholder_user;
                } else {
                    USER_IMAGE = personPhoto.toString();
                }

                USER_ID = completedTask.getUid();
                //String image=
                LOGIN_TYPE = Commn.GOOGLE_TYPE;
                checkUser();


            } else {
                Toast.makeText(context, "Unable to fetch notificationModel", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Log.e(TAG, "signInResult:failed code=");
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
            // updateUI(null);
        }
    }

    private void checkUser() {

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(USER_ID)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.getResult().exists()) {

                        if (task.getResult().getString(DBConstants.auth_status) != null) {
                            String status = task.getResult().getString(DBConstants.auth_status);
                            if ("false".equalsIgnoreCase(status)) {
                                showBlockMsg();
                            } else {
                                iniSocialLogin(task);
                            }
                        } else {
                            iniSocialLogin(task);
                        }

                    } else {
                        isUserExists = false;
                        Commn.showDebugLog("check_user:" + isUserExists + "");
                        showCountryDialog();
                    }
                });

    }

    private void iniSocialLogin(Task<DocumentSnapshot> task) {
        if (!activity.isFinishing()) {
            Commn.showDialog(activity);
        }
        isUserExists = true;
        Commn.showDebugLog("check_user:" + isUserExists + "");

        profilePOJO = task.getResult().toObject(ProfilePOJO.class);

        if (profilePOJO != null) {
            loginUser();
        }
    }

    private void showCountryDialog() {
        countryPicker = CountryPicker.newInstance("Select Country");  // dialog title
        countryPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String full_country_name, String short_country_name, String countrycode, int icon) {
                countryCode = countrycode;
                country_name = full_country_name;
                countryPicker.dismiss();
                addNewUser();

                Commn.showDebugLog("short_country_name:" + short_country_name + ",country_full_name:" + country_name + ",country_code:" + countrycode + "");
            }
        });

        countryPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
    }

    private void addNewUser() {

        firebaseFirestore.collection(DBConstants.points_table)
                .document(DBConstants.points_table_key)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        if (task.getResult().exists()) {
                            int bonus = task.getResult().getLong(DBConstants.signup_bonus).intValue();
                            Commn.showErrorLog("table_signup_bonus:" + String.valueOf(bonus) + "");
                            registerUser(bonus);

                        } else {
                            Commn.showErrorLog("table_signup_bonus not available in table:");
                            registerUser(1000);
                        }
                    } else {
                        Commn.showErrorLog("table_signup_bonus not available in table:");
                        registerUser(1000);
                    }

                });

    }

    private void registerUser(int bonus) {
        profilePOJO = new ProfilePOJO();
        profilePOJO.setEmail(USER_EMAIL + "");
        profilePOJO.setName(USER_NAME + "");
        profilePOJO.setUserId(USER_ID + "");
        profilePOJO.setGender("");
        profilePOJO.setBio("");
        profilePOJO.setDob("");
        profilePOJO.setHometown("");
        profilePOJO.setPoint(bonus);
        profilePOJO.setTotal_recieved_beans(0);
        profilePOJO.setTotal_sent_diamonds(0);
        profilePOJO.setCurrent_level("1");
        profilePOJO.setAuth_status("true");
        profilePOJO.setSuper_live_id("cryptok" + arrangeString(USER_ID) + "");
        profilePOJO.setMobile(USER_MOBILE + "");
        profilePOJO.setImage(USER_IMAGE + "");
        profilePOJO.setLoginType(LOGIN_TYPE + "");
        profilePOJO.setCountry_name(country_name + "");
        profilePOJO.setCountry_code(countryCode + "");
        sessionManager.setString(ConstantsKey.PROFILE_KEY, new Gson().toJson(profilePOJO));
        updateFirebase();
    }


    private String arrangeString(String SALTCHARS) {
        String str;
        if (SALTCHARS.contains("+")) {
            str = SALTCHARS.replace("+", "");
            Commn.showDebugLog("+ contained");

        } else {
            str = SALTCHARS;
            Commn.showDebugLog("+ not contained");

        }
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * str.length());
            salt.append(str.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr.trim();
    }

    private void loginUser() {

        sessionManager.setString(ConstantsKey.PROFILE_KEY, new Gson().toJson(profilePOJO));
        sessionManager.saveMyLevel(profilePOJO.getCurrent_level() + "");
        updateFirebase();

    }

    private void updateFirebase() {

        firebaseFirestore.collection(DBConstants.UserInfo)
                .document(profilePOJO.getUserId())
                .set(profilePOJO)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Commn.showDebugLog("check_user:" + isUserExists + "");
                        if (isUserExists) {
                            saveUser();
                        } else {
                            updateCountry();
                        }

                    }
                });

    }

    private void updateCountry() {

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put(DBConstants.country_name, country_name);
        messageMap.put(DBConstants.country_code, countryCode);
        messageMap.put(DBConstants.country_id, countryCode);

        firebaseFirestore.collection(DBConstants.All_Countries)
                .document(countryCode)
                .set(messageMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Commn.showDebugLog("country_added:");
                            saveUser();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Commn.showDebugLog("country_added failure:" + e.getMessage() + " ");
            }
        });

    }

    private void saveUser() {
        sessionManager.setBoolean(ConstantsKey.IS_LOGIN, true);

        if (!activity.isFinishing()) {
            Commn.hideDialog(activity);
        }
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}