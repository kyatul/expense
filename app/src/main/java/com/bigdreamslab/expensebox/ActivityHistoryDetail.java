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

import com.bigdreamslab.expensebox.adapter.AdapterCustomHistoryDetail;

import java.util.ArrayList;

public class ActivityHistoryDetail extends Activity {

    ListView lvHistoryDetail;
    DatabaseExpense dbExpense;
    TextView tvHead;
    ArrayList<String> dateList = null;
    ArrayList<String> descList = null;
    ArrayList<String> expenseList = null;
    ArrayList<String> idList = null;
    Typeface tf;
    String category,timeSlab;

    SharedPreferences defaultPrefs,customPrefs;
    String currency;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_category_history_detail);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        customPrefs = getSharedPreferences("ExpenseBox", Context.MODE_PRIVATE);
        currency = defaultPrefs.getString("prefCurrency","$");

        tf= Typeface.createFromAsset(getAssets(), "font/Medium.otf");

        dbExpense = new DatabaseExpense(this);

        lvHistoryDetail = (ListView) findViewById(R.id.lvHistoryDetail);
        tvHead = (TextView) findViewById(R.id.tvCategoryHistoryDetail);
        tvHead.setTypeface(tf);

        extras = getIntent().getExtras();
        if (extras != null) {
            category = extras.getString("category");  //  defines the category for which detail is required
            timeSlab = extras.getString("timeSlab");  // defines the time for which detail is required
        }
        tvHead.setText(category);

    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeListView();
    }

    public void initializeListView(){

        dateList = new ArrayList<String>();
        descList = new ArrayList<String>();
        expenseList = new ArrayList<String>();
        idList = new ArrayList<String>();

        dbExpense.open();
        Cursor cr=null;

        if(timeSlab.equals("month")){
            int month = customPrefs.getInt(ActivityHistoryHome.HISTORY_MONTH_TEMP_BUFFER_int,0);
            int year = customPrefs.getInt(ActivityHistoryHome.HISTORY_YEAR_TEMP_BUFFER_int,0);

            cr = dbExpense.getExpenseDetailByCategory(category,month,year);
        }
        else if(timeSlab.equals("day")){ //recent transaction i.e today and yesterday
            int recentStatus = Integer.parseInt(defaultPrefs.getString("prefRecent","0"));
            int day=0;
            if(recentStatus ==0){
                day= ExpenseDate.getDay();
            }
            else if(recentStatus ==1){
                day= ExpenseDate.getYesterdayDate();
            }
            cr = dbExpense.getExpenseDetailByCategoryToday(category,day,ExpenseDate.getMonth(),ExpenseDate.getYear());
        }
        if(timeSlab.equals("last10Transaction")){
            cr = dbExpense.getExpenseDetailByCategoryLast10(category);
        }



        if(cr.getCount()>0){

            cr.moveToFirst();
            while(!cr.isAfterLast()){
                String date=cr.getString(2)+" "+ExpenseDate.getMonthName(Integer.parseInt(cr.getString(3)))+" "+cr.getString(4);
                String desc=cr.getString(7)+"\n"+cr.getString(5);
                String expense = currency+" "+String.format("%.2f",cr.getFloat(1));
                int rowID=cr.getInt(6);

                dateList.add(date);
                descList.add(desc);
                expenseList.add(expense);
                idList.add(""+rowID);

                cr.moveToNext();
            }
        }
        else{
            dateList.add("");
            descList.add("");
            expenseList.add("");
            idList.add("");
        }
        dbExpense.close();

        AdapterCustomHistoryDetail adapter = new AdapterCustomHistoryDetail(this, dateList ,descList, expenseList,idList);
        lvHistoryDetail.setAdapter(adapter);

        lvHistoryDetail.setOnItemClickListener(new AdapterView.OnItemClickListener(){ //for deletion purpose


            public void onItemClick(AdapterView<?> arg0, View view,int pos, long id) {

                TextView t =(TextView) view.findViewById(R.id.lvID);
                String rowID = t.getText().toString();

                Intent i = new Intent(ActivityHistoryDetail.this,DialogAmountEditPLAN2.class);
                i.putExtra("rowID",rowID);
                startActivity(i);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    initializeListView();
                }
                break;
            }
        }
    }


}
