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
import android.widget.Toast;

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
import com.songu.happie.model.UserModel;
import com.songu.happie.util.Constant;
import com.songu.happie.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.facebook.FacebookSdk;

/**
 * Created by Administrator on 7/14/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    public RelativeLayout layoutGoogle;

    //Google Plus Variables
    public static final int RC_SIGN_IN = 100;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    private boolean signedInUser;
    private String googleId, googleEmail, googleName;
    private Context mContext;
    private ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";
    private Gson gson;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        mContext = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        controller = (Controller) getApplicationContext();

        String userinfo = Utility.getStringPreferences(mContext, "userinfo");
        if (userinfo != ""){
            UserModel userModel1 = gson.fromJson(userinfo, UserModel.class);
            controller.setUserModel(userModel1);
            Intent intent=new Intent(mContext,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        googleSetup();
//        layoutGoogle = (RelativeLayout) this.findViewById(R.id.relMainGoogle);
        binding.relMainGoogle.setOnClickListener(this);

        gson = new Gson();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.relMainGoogle:
                getGmailData();
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data.getExtras().toString() + "]");

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
            Constant.retrofitService.login(new UserModel()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String string = response.body().string();
                        Log.i(TAG, "onResponse() called with: call = [" + call + "], response = [" + string + "]");

                        final JSONObject jsonObject=new JSONObject(string);
                        if(jsonObject.getBoolean("status")){
                            UserModel userModel = gson.fromJson(jsonObject.getString("data"), UserModel.class);
                            userModel.setName("lovekesh");

                            Constant.retrofitService.login(userModel).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    try {
                                        String string1 = response.body().string();

                                        JSONObject jsonObject1=new JSONObject(string1);
                                        if (jsonObject1.getBoolean("status")){
                                            String data = jsonObject1.getString("data");
                                            Utility.setStringPreferences(mContext,"userinfo", data);
                                            UserModel userModel1 = gson.fromJson(data, UserModel.class);
                                            controller.setUserModel(userModel1);
                                            Intent intent=new Intent(mContext,HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Utility.showAlert(mContext,jsonObject1.getString("message"));
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });


                        }else {
                            Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                }
            });


            Log.e(TAG, "GoogleSignInAccount:" + acct.getDisplayName() + "," + acct.getEmail());
//            setInfo(acct.getDisplayName(), acct.getId(), acct.getEmail(), acct.getPhotoUrl() + "", "google");


//            Log.e(TAG, "handleSignInResult() called with: " + "result = [" + acct.getId() + "]");
//            Log.e(TAG, "handleSignInResult() called with: " + "result = [" + acct.getDisplayName() + "]");
//            Log.e(TAG, "handleSignInResult() called with: " + "result = [" + acct.getEmail() + "]");
            //  Log.e(TAG, "handleSignInResult() called with: " + "result = [" + acct.getFamilyName() + "]");


            //  private void setInfo(final String name, final String id, final String email, final String url, String from)
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
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
}
