package com.songu.happie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.songu.happie.R;
import com.songu.happie.adapter.AdapterHome;
import com.songu.happie.doc.Globals;
import com.songu.happie.model.HappieModel;
import com.songu.happie.service.IServiceResult;
import com.songu.happie.service.ServiceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 7/14/2017.
 */

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener,IServiceResult {


    public DrawerLayout mDrawerLayout;
    public RelativeLayout mDrawerPane;
    public RelativeLayout slide_menu;
    public SwipeRefreshLayout swipeRefreshLayout;
    public AdapterHome adapterHome;
    public List<String> lstCategory = new ArrayList<>();
    public List<String> lstType = new ArrayList<>();
    public Spinner spJoke;
    public Spinner spCategory;
    public LinearLayout layoutInvite;
    public LinearLayout layoutUpload;

    public LinearLayout layoutMyUpload;
    public LinearLayout layoutProfile;
    public LinearLayout layoutRate;
    public LinearLayout layoutLogin;
    public LinearLayout layoutTerms;
    public LinearLayout layoutPolicy;
    public LinearLayout layoutHelp;
    public LinearLayout layoutShare;

    public RecyclerView lstHappies;
    public TextView txtUsername;
    public TextView txtLogin;


    public List<HappieModel> lstModels = new ArrayList<>();

    private LinearLayoutManager mLinearLayoutManager;

    private int currentCategory = 0;
    private int currentType = 0;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_activity);

        slide_menu = (RelativeLayout) findViewById(R.id.slide_menu_icon);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        spJoke = (Spinner) findViewById(R.id.joke_category);
        spCategory = (Spinner) findViewById(R.id.category);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        layoutInvite = (LinearLayout) findViewById(R.id.invitationLinearLayoutHomeScreen);
        layoutUpload = (LinearLayout) findViewById(R.id.addJokeLinearLayoutHomeScreen);
        layoutMyUpload = (LinearLayout) findViewById(R.id.youruploads);
        layoutProfile = (LinearLayout) findViewById(R.id.edit_profile);
        layoutRate = (LinearLayout) findViewById(R.id.profile_rate);
        layoutLogin = (LinearLayout) findViewById(R.id.logout);
        layoutTerms = (LinearLayout) findViewById(R.id.services);
        layoutPolicy = (LinearLayout) findViewById(R.id.privacy);
        layoutShare = (LinearLayout) findViewById(R.id.slide_profile_share);
        layoutHelp = (LinearLayout) findViewById(R.id.need_help);
        txtUsername = (TextView) findViewById(R.id.userName);
        txtLogin = (TextView) findViewById(R.id.loginStatusTextView);

        lstHappies = (RecyclerView) findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        lstHappies.setLayoutManager(mLinearLayoutManager);
        slide_menu.setOnClickListener(this);
        layoutMyUpload.setOnClickListener(this);
        layoutProfile.setOnClickListener(this);
        layoutRate.setOnClickListener(this);
        layoutInvite.setOnClickListener(this);
        layoutUpload.setOnClickListener(this);
        layoutLogin.setOnClickListener(this);
        layoutTerms.setOnClickListener(this);
        layoutPolicy.setOnClickListener(this);
        layoutShare.setOnClickListener(this);
        layoutHelp.setOnClickListener(this);
        initFilter();
        lstHappies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                swipeRefreshLayout.setRefreshing(false);
                if (newState == 0)
                {

                }
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = position;
                loadJokesByFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spJoke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentType = position;
                loadJokesByFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadJokesByFilter();
        if (Globals.mAccount == null)
        {
            txtUsername.setText("Guest");
            txtLogin.setText("Log In");
        }
        else
        {
            txtUsername.setText("Guest");
            txtLogin.setText("Log Out");
        }
    }

    public void loadJokesByFilter()
    {
        swipeRefreshLayout.setRefreshing(true);
        ServiceManager.serviceLoadJokes(this,currentCategory,currentType);
    }
    public void initFilter()
    {
        lstCategory.clear();
        lstCategory.add("Popular");
        lstCategory.add("Latest");

        lstType.clear();
        lstType.add("All");
        lstType.add("Text");
        lstType.add("Image");
        lstType.add("Video");

        ArrayAdapter localArrayAdapter1 = new ArrayAdapter(this, R.layout.view_spinner_item_category, this.lstCategory);
        localArrayAdapter1.setDropDownViewResource(R.layout.spinner_item);
        this.spCategory.setAdapter(localArrayAdapter1);

        localArrayAdapter1 = new ArrayAdapter(this, R.layout.view_spinner_item_category, this.lstType);
        localArrayAdapter1.setDropDownViewResource(R.layout.spinner_item);
        this.spJoke.setAdapter(localArrayAdapter1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.slide_menu_icon:
                mDrawerLayout.openDrawer(mDrawerPane);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lstModels.add(new HappieModel());
                        adapterHome.notifyItemInserted(lstModels.size());
                    }
                });
                break;
            case R.id.invitationLinearLayoutHomeScreen:
                break;
            case R.id.addJokeLinearLayoutHomeScreen:
                Intent m = new Intent(this,AddJokeActivity.class);
                this.startActivity(m);
                break;
            case R.id.youruploads:
                if (Globals.mAccount == null) {
                    m = new Intent(this, LoginActivity.class);
                    this.startActivityForResult(m,100);
                }
                else
                {
                    m = new Intent(this,MyUploadActivity.class);
                    this.startActivity(m);
                }
                break;
            case R.id.edit_profile:
                if (Globals.mAccount == null) {
                    m = new Intent(this, LoginActivity.class);
                    this.startActivityForResult(m,100);
                }
                else
                {
                    m = new Intent(this,UserProfileActivity.class);
                    this.startActivity(m);
                }
                break;
            case R.id.profile_rate:
                break;
            case R.id.logout:
                if (Globals.mAccount == null) {
                    m = new Intent(this, LoginActivity.class);
                    this.startActivityForResult(m,100);
                }
                else
                {

                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 100) {

        }
    }
    @Override
    public void onResponse(int code) {
        swipeRefreshLayout.setRefreshing(false);
        switch (code)
        {
            case 200:
                adapterHome = new AdapterHome(Globals.lstJokes);
                lstHappies.setAdapter(adapterHome);
                break;
            case 400:
                break;
        }
    }
}
