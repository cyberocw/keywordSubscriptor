package com.creativeoh.keywordsubscriptor;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.crashlytics.android.Crashlytics;
import com.creativeoh.keywordsubscriptor.keyword.KeywordFragment;
import com.creativeoh.keywordsubscriptor.util.CommonUtils;
import com.creativeoh.keywordsubscriptor.util.TitleMessageEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    public Context mContext;
    FirebaseAnalytics mFirebaseAnalytics;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        Crashlytics cr = new Crashlytics();
        Fabric.with(this, cr, new Crashlytics());
        mContext = getApplicationContext();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        MobileAds.initialize(this, "ca-app-pub-8072677228798230~9532888367"); // real
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("048A3A6B542D3DD340272D8C1D80AC18")
                .build();
        if(Const.IS_DEBUG){
            adView.setVisibility(View.GONE);
            //+dimension margin 0 주기
        }else{
            adView.setVisibility(View.VISIBLE);
        }
        adView.loadAd(adRequest);

        initMainActivity(getIntent());
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(this.toString(), "onstart");
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void initMainActivity(Intent intent){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        String tag = "keyword";
        Crashlytics.log(Log.DEBUG, Const.DEBUG_TAG, "ocw vintent = " + intent + " get extras = " + intent.getExtras());
        //시작하는 경우는 최초 실행, 메모보기, 알림 연장 의 경우로 아 래 로직을 탐
        Bundle bundle = intent.getExtras();
        fragment = new KeywordFragment();
        fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_container, fragment, tag).commit();

        //afterUpdateVersion();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TitleMessageEvent event) {
        //Toast.makeText(getApplicationContext(), event.message, Toast.LENGTH_SHORT).show();
        //pushActionBarInfo(event.title, event.isShowHelp);
    }
}
