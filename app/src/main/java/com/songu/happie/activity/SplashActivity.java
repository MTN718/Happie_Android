package com.songu.happie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.gson.Gson;
import com.songu.happie.R;
import com.songu.happie.doc.Globals;
import com.songu.happie.model.UserModel;
import com.songu.happie.service.IServiceResult;
import com.songu.happie.util.Constant;
import com.songu.happie.util.Utility;

/**
 * Created by Administrator on 12/15/2015.
 */
public class SplashActivity extends Activity implements View.OnClickListener, IServiceResult {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Intent m;
            switch (msg.what) {
                case 1:
                    Globals.mAccount=new Gson().fromJson(Utility.getStringPreferences(SplashActivity.this, Constant.SHAREDPREFUSER), UserModel.class);
                    m = new Intent(SplashActivity.this, HomeActivity.class);
                    m.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    SplashActivity.this.startActivity(m);
                    break;
                case 0:
                    m = new Intent(SplashActivity.this, LoginActivity.class);
                    m.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    SplashActivity.this.startActivity(m);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean b = Utility.getBooleanPreferences(this, Constant.ISUSERLOGIN);
//        SharedPreferences sp = this.getSharedPreferences(Constant.ISUSERLOGIN, Context.MODE_PRIVATE);
//        int isLogin = sp.getInt("login",0);
//        String countryNo = sp.getString("countryNo","");
//
//        if (!countryNo.equals(""))
//        {
//            String countryName = sp.getString("countryName","");
//            String countryImage = sp.getString("countryImage","");
//
//            Globals.currentCountry = new CountryModel();
//            Globals.currentCountry.mNo = countryNo;
//            Globals.currentCountry.mImage = countryImage;
//            Globals.currentCountry.mName = countryName;
//
//            handler.sendEmptyMessageDelayed(1,3000);
//            return;
//        }
        if (b)
            initView(1);
        else
            initView(0);

    }

    public void initView(int i) {
        handler.sendEmptyMessageDelayed(i, 3000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onResponse(int code) {
        switch (code) {
            case 200:
//                Utils.savePreference(this);
//                Intent intent = new Intent(this,HomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                this.startActivity(intent);
                break;
        }
    }
}
