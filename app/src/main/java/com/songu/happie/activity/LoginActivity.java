package com.songu.happie.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.songu.happie.R;
import com.songu.happie.controller.Controller;
import com.songu.happie.databinding.ActivityLoginBinding;
import com.songu.happie.doc.Globals;
import com.songu.happie.helper.UIHelper;
import com.songu.happie.model.UserModel;
import com.songu.happie.network.RetriveHandler;
import com.songu.happie.util.Constant;
import com.songu.happie.util.MessageConstants;
import com.songu.happie.util.Utility;

//import com.facebook.FacebookSdk;

/**
 * Created by Administrator on 7/14/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RetriveHandler {

    public RelativeLayout layoutGoogle;

    //Google Plus Variables
    public static final int RC_SIGN_IN = 100;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private boolean signedInUser;
    private Context mContext;
    private ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";
    private Gson gson;
    private Controller controller;
    private String userJson;
    private UIHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        mContext = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        controller = (Controller) getApplicationContext();
        uiHelper = new UIHelper(this);
        String userinfo = Utility.getStringPreferences(mContext, Constant.SHAREDPREFUSER);
        if (userinfo != "") {
            UserModel userModel1 = gson.fromJson(userinfo, UserModel.class);
            controller.setUserModel(userModel1);
            gotoHomePage();
        }

        googleSetup();
//        layoutGoogle = (RelativeLayout) this.findViewById(R.id.relMainGoogle);
        binding.relMainGoogle.setOnClickListener(this);

        gson = new Gson();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relMainGoogle:
                getGmailData();
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        } else {
//            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());


        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                UserModel userModel = new UserModel();
                userModel.setName(acct.getGivenName());
                userModel.setId(acct.getId());
                userModel.setEmail(acct.getEmail());
                userJson = gson.toJson(userModel);
                Globals.mAccount = userModel;
                storeDatainPrefandgotoHome();
//                if (Utility.isConnectingToInternet(mContext)) {
//                    new RetriveData(this, LoginActivity.this, null, "Loading ...", REQUEST_TYPE.LOGINREQUEST, METHOD_TYPE.POST).execute(Config.LOGIN_URL, userJson);
//                } else {
//                    uiHelper.showToast(MessageConstants.PLEASE_CONNECT_TO_INTERNET);
//                }

            }
        }
    }


    private void getGmailData() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void googleSetup() {
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSuccess(String result, REQUEST_TYPE request_type) {


    }

    @Override
    public void onError(String result, REQUEST_TYPE request_type) {

        Globals.mAccount = null;
        if (result.contains(MessageConstants.ERROR_MSG)) {
            uiHelper.showToast(Utility.getErrorMsg(result));
        } else {
            uiHelper.showToast(MessageConstants.SOMETHING_WENT_WRONG);
        }


    }


    public void storeDatainPrefandgotoHome() {

        Utility.setBooleanPreferences(mContext, Constant.ISUSERLOGIN, true);
        Utility.setStringPreferences(mContext, Constant.SHAREDPREFUSER, userJson);

        gotoHomePage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
        mGoogleApiClient = null;
        binding = null;
        uiHelper = null;
    }

    private void gotoHomePage() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
