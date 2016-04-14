package com.bigdreamslab.expensebox;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigdreamslab.expensebox.fragments.LaunchScreenFragment1;
import com.bigdreamslab.expensebox.fragments.LaunchScreenFragment2;
import com.bigdreamslab.expensebox.fragments.LaunchScreenFragment3;

public class ActivityLaunch extends FragmentActivity implements View.OnClickListener{

    private static final int NUM_PAGES = 1;
    SharedPreferences prefs;
    TextView btContinue;
    ViewPager mPager;
    PagerAdapter mPagerAdapter;
    SharedPreferences defaultPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch);
        prefs = getSharedPreferences("Expenses", Context.MODE_PRIVATE);
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isFirstLaunched = prefs.getBoolean(PreferencesConstants.FIRST_TIME_LAUNCHED_boolean,true);
        if(!isFirstLaunched){
            startActivity(new Intent(this,ActivityHome.class));
            finish();
        }

        btContinue = (TextView) findViewById(R.id.btContinue);
        btContinue.setOnClickListener(this);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this,DialogCurrency.class);
        startActivityForResult(i, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String currency = data.getStringExtra("currency");
                    defaultPrefs.edit().putString("prefCurrency",currency).commit();
                    defaultPrefs.edit().putString("prefRecent","0").commit();
                    startActivity(new Intent(this,ActivityHome.class));
                    finish();
                }
                break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        LaunchScreenFragment1 lsf1 = new LaunchScreenFragment1();
        LaunchScreenFragment2 lsf2 = new LaunchScreenFragment2();
        LaunchScreenFragment3 lsf3 = new LaunchScreenFragment3();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
             return lsf1;
            }
            if(position==1){
              return lsf2;
            }
            if(position==2){
              return lsf3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}