package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigdreamslab.expensebox.adapter.AdapterCustomHistoryHome;

import java.util.ArrayList;

public class ActivityRecent extends Activity{

    ListView lvRecent;
    DatabaseExpense dbExpense;
    DatabaseCategory dbCategory;
    TextView tvTotalExpenseNull,tvRecentDetail,tvRecent;

    ArrayList<String> nameList = null;
    ArrayList<Integer> iconList = null;
    ArrayList<String> expenseList = null;

    Typeface tf,tfLight;
    int day,month,year;

    SharedPreferences defaultPrefs,customPrefs;
    String currency;
    int recentStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recent);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        customPrefs = getSharedPreferences("ExpenseBox", Context.MODE_PRIVATE);

        currency = defaultPrefs.getString("prefCurrency","$");
        recentStatus = Integer.parseInt(defaultPrefs.getString("prefRecent","0"));

        tf= Typeface.createFromAsset(getAssets(), "font/Medium.otf");
        tfLight= Typeface.createFromAsset(getAssets(), "font/Light.otf");

        dbExpense = new DatabaseExpense(this);
        dbCategory = new DatabaseCategory(this);

        lvRecent = (ListView) findViewById(R.id.lvRecent);
        tvTotalExpenseNull = (TextView) findViewById(R.id.tvTotalExpenseNull2);
        tvRecentDetail = (TextView) findViewById(R.id.tvRecentDetail);
        tvRecent = (TextView) findViewById(R.id.tvRecent);

        tvTotalExpenseNull.setTypeface(tf);
        tvRecentDetail.setTypeface(tfLight);
        tvRecent.setTypeface(tf);

        if(recentStatus ==0){
            tvRecentDetail.setText("(Today)");
            day=ExpenseDate.getDay();
        }
        else if(recentStatus ==1){
            tvRecentDetail.setText("(Yesterday)");
            day=ExpenseDate.getYesterdayDate();
        }
        else if(recentStatus ==2){
            tvRecentDetail.setText("(Last 10 Transactions)");
        }

        month=ExpenseDate.getMonth();
        year=ExpenseDate.getYear();

        customPrefs.edit().putInt(ActivityHistoryHome.HISTORY_MONTH_TEMP_BUFFER_int,month).commit();
        customPrefs.edit().putInt(ActivityHistoryHome.HISTORY_YEAR_TEMP_BUFFER_int,year).commit();



    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeListView(day,month,year);
    }

    public void initializeListView(int day,int month,int year){


        nameList = new ArrayList<String>();
        iconList = new ArrayList<Integer>();
        expenseList = new ArrayList<String>();

        dbExpense.open();

        Cursor cr=null;
        if(recentStatus ==0 || recentStatus ==1){
            cr = dbExpense.getExpenseSumByCategoryToday(day, month, year);
        }
        else if(recentStatus == 2){
            cr = dbExpense.getExpenseSumByCategoryLast10();
        }



        if(cr.getCount()>0){
            tvTotalExpenseNull.setVisibility(View.GONE);

            cr.moveToFirst();
            while(!cr.isAfterLast()){
                String name=cr.getString(1);
                String expense = currency+" "+String.format("%.2f", cr.getFloat(0));

                nameList.add(name);
                expenseList.add(expense);
                cr.moveToNext();
            }

            dbCategory.open();
            iconList = dbCategory.getCategoryIcon(nameList);
            dbCategory.close();
            AdapterCustomHistoryHome adapter = new AdapterCustomHistoryHome(this, nameList ,iconList, expenseList);
            lvRecent.setAdapter(adapter);

            lvRecent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                    String category = nameList.get(position);
                    Intent i = new Intent(ActivityRecent.this,ActivityHistoryDetail.class);
                    i.putExtra("category",category);

                    if(recentStatus==0 || recentStatus ==1){
                        i.putExtra("timeSlab","day");
                    }
                    else if(recentStatus ==2){
                        i.putExtra("timeSlab","last10Transaction");
                    }

                    startActivity(i);
                }
            });

        }
        else{
            AdapterCustomHistoryHome adapter = new AdapterCustomHistoryHome(this, nameList ,iconList, expenseList);
            lvRecent.setAdapter(adapter);
            tvTotalExpenseNull.setVisibility(View.VISIBLE);
        }
        dbExpense.close();


        lvRecent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                String categoryBuffer = nameList.get(position);

                Intent i = new Intent(ActivityRecent.this, DialogDelete.class);
                i.putExtra("category",categoryBuffer);
                i.putExtra("requestFrom","recentTransHomePage");
                startActivityForResult(i,1);
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    initializeListView(day,month,year);
                }
                break;
            }
        }
    }
}
