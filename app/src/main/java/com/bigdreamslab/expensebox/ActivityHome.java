package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ActivityHome extends Activity implements View.OnClickListener, View.OnTouchListener {

    TextView tvMonth,tvTotalExpense,tvTotalExpenseNull,tvExpense1,tvExpense2,tvCategory1,tvCategory2,tvAddNew;
    ImageView ivAdd,ivDrawer;
    SharedPreferences prefs;
    DatabaseCategory dbCategory;
    DatabaseExpense dbExpense;

    Typeface tf,tfLight,tfBold;
    SharedPreferences defaultPrefs;

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    CharSequence mTitle;

    private TypedArray navMenuIcons;
    private String[] navMenuTitles;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    String currency;



    @Override
    protected void onResume() {
        super.onResume();

        currency = defaultPrefs.getString("prefCurrency","$");
        dbExpense.open();
        float expense = dbExpense.getExpenseSum(ExpenseDate.getMonth(),ExpenseDate.getYear());

        if(expense>0){
            tvTotalExpenseNull.setVisibility(View.GONE);
            tvTotalExpense.setVisibility(View.VISIBLE);
            tvTotalExpense.setText(currency+" "+String.format("%.2f",expense));
        }
        else{
            tvTotalExpenseNull.setVisibility(View.VISIBLE);
            tvTotalExpense.setVisibility(View.GONE);
        }

        Cursor cr = dbExpense.getExpenseSumByCategory(ExpenseDate.getMonth(),ExpenseDate.getYear());
        if(cr.getCount()>1){
            tvCategory1.setVisibility(View.VISIBLE);
            tvCategory2.setVisibility(View.VISIBLE);
            tvExpense1.setVisibility(View.VISIBLE);
            tvExpense2.setVisibility(View.VISIBLE);

            cr.moveToFirst();
            tvCategory1.setText(cr.getString(1));
            tvExpense1.setText(currency+" "+String.format("%.2f",cr.getFloat(0)));

            cr.moveToNext();
            tvCategory2.setText(cr.getString(1));
            tvExpense2.setText(currency+" "+String.format("%.2f",cr.getFloat(0)));

        }
        else{
            tvCategory1.setVisibility(View.GONE);
            tvCategory2.setVisibility(View.GONE);
            tvExpense1.setVisibility(View.GONE);
            tvExpense2.setVisibility(View.GONE);
        }

        dbExpense.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        tf= Typeface.createFromAsset(getAssets(), "font/Medium.otf");
        tfLight= Typeface.createFromAsset(getAssets(), "font/Light.otf");
        tfBold= Typeface.createFromAsset(getAssets(), "font/Bold.otf");

        //NavDrawer Codes Starts
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_item);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
                .getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
                .getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
                .getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
                .getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
                .getResourceId(4, -1)));


        navMenuIcons.recycle();

        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //NavDrawer Codes Ends

        dbCategory = new DatabaseCategory(this);
        dbExpense = new DatabaseExpense(this);

        tvMonth = (TextView)findViewById(R.id.tvMonth);
        tvTotalExpense = (TextView) findViewById(R.id.tvTotalExpense);
        tvTotalExpenseNull = (TextView) findViewById(R.id.tvTotalExpenseNull);
        tvExpense1 = (TextView) findViewById(R.id.tvExpense1);
        tvExpense2 = (TextView) findViewById(R.id.tvExpense2);
        tvCategory1 = (TextView) findViewById(R.id.tvCategory1);
        tvCategory2 = (TextView) findViewById(R.id.tvCategory2);
        tvAddNew = (TextView) findViewById(R.id.tvAddNew);
        ivDrawer = (ImageView) findViewById(R.id.ivDrawer);

        tvMonth.setTypeface(tf);
        tvTotalExpenseNull.setTypeface(tfLight);
        tvTotalExpense.setTypeface(tfBold);
        tvCategory1.setTypeface(tfLight);
        tvCategory2.setTypeface(tfLight);
        tvExpense1.setTypeface(tf);
        tvExpense2.setTypeface(tf);
        tvAddNew.setTypeface(tf);

        tvMonth.setText(ExpenseDate.getMonthTitle());

        ivAdd = (ImageView) findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(this);
        //ivAdd.setOnTouchListener(this);
        ivDrawer.setOnClickListener(this);


        prefs = getSharedPreferences("Expenses", Context.MODE_PRIVATE);

        boolean isFirstLaunched = prefs.getBoolean(PreferencesConstants.FIRST_TIME_LAUNCHED_boolean,true);
        if(isFirstLaunched){
            dbCategory.open();
            dbCategory.intializeDatabase();
            dbCategory.close();

            mDrawerLayout.openDrawer(mDrawerList);
            prefs.edit().putBoolean(PreferencesConstants.FIRST_TIME_LAUNCHED_boolean,false).commit();
        }
    }


    private void selectItem(int position) {

        if (position == 0) {
            startActivity(new Intent(this,ActivityHistoryHome.class));
        }
        if (position == 1) {
            startActivity(new Intent(this, ActivityBudget.class));
        }
        if (position == 2) {
            startActivity(new Intent(this, ActivityRecent.class));
        }
        if (position == 3) {
            startActivity(new Intent(this,ActivityPreferences.class));
        }
        if (position == 4) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.bigdreamslab.expensemanager"));
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivAdd:
                startActivity(new Intent(this,ActivityAddNew.class));
                break;

            case R.id.ivDrawer:
                mDrawerLayout.openDrawer(mDrawerList);
                break;

        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ((ImageView) v).setImageResource(R.drawable.touch);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ((ImageView) v).setImageResource(R.drawable.add_new);
                break;
        }
        return false;
    }
}
