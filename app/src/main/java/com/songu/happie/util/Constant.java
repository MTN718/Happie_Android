package com.songu.happie.util;


import com.songu.happie.service.RetrofitService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rohit on 7/19/2016.
 */

public class Constant {

    // http://ruby.argalon.net/getcategories

    //http://www.argalon.net/comefixme/index.php/App_controller/List_Categories
    // public static final String BASE_URL = "http://159.203.119.6/~argalon/comefixme/";


    //public static final String BASE_URL = "http://www.argalon.net/comefixme/";

    //    public static final String BASE_URL = "https://www.comefixme.com/";
    public static final String BASE_URL = "http://golvaje.top";

    public static String jSessionId;

    public static RetrofitService retrofit = null;
    public static final String SHAREDPREFUSER = "USERINFO";
    public static final String ISUSERLOGIN = "LOGINSTATUS";

    public static OkHttpClient getinfo() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(logging).build();
    }


    public static final RetrofitService retrofitService = new Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(getinfo()).build().create(RetrofitService.class);


}

