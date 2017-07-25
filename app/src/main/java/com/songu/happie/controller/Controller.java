package com.songu.happie.controller;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import com.songu.happie.model.UserModel;


/**
 * Created by Argalon-PC on 8/10/2016.
 */

public class Controller extends MultiDexApplication {


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    // public UserInfoModel userInfirmationModel;

    public UserModel userModel;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(Controller.this);

    }

    @Override
    public void onCreate() {
        super.onCreate();

       /* FontsOverride.setDefaultFont(this, "SERIF", "fonts/ClanPro-Book.otf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/ClanPro-Medium.otf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/ClanPro-WideNews.otf");*/
        userModel=new UserModel();

    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
