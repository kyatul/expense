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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigdreamslab.expensebox.adapter.AdapterCustomHistoryHome;

import java.util.ArrayList;

public class ActivityHistoryHome extends Activity implements View.OnClickListener{

    ListView lvHomeHistory;
    DatabaseExpense dbExpense;
    DatabaseCategory dbCategory;
    ImageView ivLeft,ivRight;
    TextView tvTotalExpenseNull, tvMonthHistory,tvMonthHistoryYear;
    ArrayList<String> nameList = null;
    ArrayList<Integer> iconList = null;
    ArrayList<String> expenseList = null;
    Typeface tf,tfLight;
    int month,year;

    SharedPreferences defaultPrefs,customPrefs;
    String currency;

    //the below two preference will be used in DeleteHelper class to know the current month and year being called to be deleted
    public static final String HISTORY_MONTH_TEMP_BUFFER_int="historyMonthTempBuffer";
    public static final String HISTORY_YEAR_TEMP_BUFFER_int="historyYearTempBuffer";

    public static final String CATEGORY_TEMP_BUFFER_string="categoryTempBuffer";
    public static final String TIMESLAB_TEMP_BUFFER_string="timeSlabTempBuffer";
    String categoryBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_month_history);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        customPrefs = getSharedPreferences("ExpenseBox", Context.MODE_PRIVATE);

        currency = defaultPrefs.getString("prefCurrency","$");
        tf= Typeface.createFromAsset(getAssets(), "font/Medium.otf");
        tfLight= Typeface.createFromAsset(getAssets(), "font/Light.otf");

        dbExpense = new DatabaseExpense(this);
        dbCategory = new DatabaseCategory(this);


        lvHomeHistory = (ListView) findViewById(R.id.lvHistoryHome);
        tvTotalExpenseNull = (TextView) findViewById(R.id.tvTotalExpenseNull1);
        tvMonthHistory = (TextView) findViewById(R.id.tvMonthHistory);
        tvMonthHistoryYear = (TextView) findViewById(R.id.tvMonthHistoryYear);

        ivLeft = (ImageView) findViewById(R.id.ivLeft);
        ivRight = (ImageView) findViewById(R.id.ivRight);

        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);

        tvTotalExpenseNull.setTypeface(tf);
        tvMonthHistory.setTypeface(tf);
        tvMonthHistoryYear.setTypeface(tfLight);


        month=ExpenseDate.getMonth();
        year=ExpenseDate.getYear();

        tvMonthHistoryYear.setText(", "+year);
        //putting the values in Preference Buffer
        customPrefs.edit().putInt(HISTORY_MONTH_TEMP_BUFFER_int,month).commit();
        customPrefs.edit().putInt(HISTORY_YEAR_TEMP_BUFFER_int,year).commit();

        tvMonthHistory.setText(ExpenseDate.getMonthName(month));
    }


    @Override
    protected void onResume() {
        super.onResume();

        initializeListView(month,year);
    }

    public void initializeListView(int month,int year){

        nameList = new ArrayList<String>();
        iconList = new ArrayList<Integer>();
        expenseList = new ArrayList<String>();

        dbExpense.open();
        Cursor cr = dbExpense.getExpenseSumByCategory(month,year);

        if(cr.getCount()>0){
            tvTotalExpenseNull.setVisibility(View.GONE);

            cr.moveToFirst();
            while(!cr.isAfterLast()){
                String name=cr.getString(1);
                String expense = currency+" "+String.format("%.2f",cr.getFloat(0));

                nameList.add(name);
                expenseList.add(expense);
                cr.moveToNext();
            }

            dbCategory.open();
            iconList = dbCategory.getCategoryIcon(nameList);
            dbCategory.close();
            AdapterCustomHistoryHome adapter = new AdapterCustomHistoryHome(this, nameList ,iconList, expenseList);
            lvHomeHistory.setAdapter(adapter);

            lvHomeHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                    String category = nameList.get(position);
                    Intent i = new Intent(ActivityHistoryHome.this,ActivityHistoryDetail.class);
                    i.putExtra("category",category);
                    i.putExtra("timeSlab","month");
                    startActivity(i);
                }
            });

        }
        else{
            AdapterCustomHistoryHome adapter = new AdapterCustomHistoryHome(this, nameList ,iconList, expenseList);
            lvHomeHistory.setAdapter(adapter);
            tvTotalExpenseNull.setVisibility(View.VISIBLE);
        }
        dbExpense.close();

        lvHomeHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                categoryBuffer = nameList.get(position);

                Intent i = new Intent(ActivityHistoryHome.this, DialogDelete.class);
                i.putExtra("category",categoryBuffer);
                i.putExtra("requestFrom","historyHomePage");
                startActivityForResult(i, 1);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ivLeft:
                if(month==1){
                    month=12;
                    year--;
                }
                else{
                    month--;
                }

                customPrefs.edit().putInt(HISTORY_MONTH_TEMP_BUFFER_int,month).commit();
                customPrefs.edit().putInt(HISTORY_YEAR_TEMP_BUFFER_int,year).commit();

                tvMonthHistory.setText(ExpenseDate.getMonthName(month));
                tvMonthHistoryYear.setText(", "+year);
                initializeListView(month,year);

                break;
            case R.id.ivRight:
                if(month==12){
                    month=1;
                    year++;
                }
                else{
                    month++;
                }

                customPrefs.edit().putInt(HISTORY_MONTH_TEMP_BUFFER_int,month).commit();
                customPrefs.edit().putInt(HISTORY_YEAR_TEMP_BUFFER_int,year).commit();

                tvMonthHistory.setText(ExpenseDate.getMonthName(month));
                tvMonthHistoryYear.setText(", "+year);
                initializeListView(month,year);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    initializeListView(month, year);
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //returning buffer state to original--just for being safe-not mandatory
        customPrefs.edit().putInt(HISTORY_MONTH_TEMP_BUFFER_int,ExpenseDate.getMonth()).commit();
        customPrefs.edit().putInt(HISTORY_YEAR_TEMP_BUFFER_int,ExpenseDate.getYear()).commit();

    }
}
