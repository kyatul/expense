package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class DialogDelete extends Activity implements View.OnClickListener{

    TextView tvDelCancel,tvDelOK,tvAreYouSure;
    Bundle extras;
    String requestFrom;
    String category,expense;
    int day,month,year;

    SharedPreferences customPrefs,defaultPrefs;
    DatabaseExpense dbExpense;
    DatabaseCategory dbCategory;

    String rowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_delete);
        dbExpense = new DatabaseExpense(this);
        dbCategory = new DatabaseCategory(this);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        customPrefs = getSharedPreferences("ExpenseBox", Context.MODE_PRIVATE);

        month = customPrefs.getInt(ActivityHistoryHome.HISTORY_MONTH_TEMP_BUFFER_int,0);
        year = customPrefs.getInt(ActivityHistoryHome.HISTORY_YEAR_TEMP_BUFFER_int,0);

        tvDelCancel = (TextView) findViewById(R.id.tvDelCancel);
        tvDelOK = (TextView) findViewById(R.id.tvDelOK);
        tvAreYouSure = (TextView) findViewById(R.id.tvAreYouSure);

        CustomTextTouchListener ctt = new CustomTextTouchListener(this,0xffec6e60);
        tvDelCancel.setOnTouchListener(ctt);
        tvDelOK.setOnTouchListener(ctt);

        tvDelCancel.setOnClickListener(this);
        tvDelOK.setOnClickListener(this);

        extras = getIntent().getExtras();
        if (extras != null) {
            requestFrom = extras.getString("requestFrom");
            if(requestFrom.equals("detailPage")){
              rowID = extras.getString("rowID");
            }
            else if(requestFrom.equals("categoryDelete")){
                tvAreYouSure.setText("Are you sure? \nAll related transaction data will be lost.");
                category = extras.getString("category");
            }
            else{
              category = extras.getString("category");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvDelCancel:
                finish();
                break;
            case R.id.tvDelOK:
                dbExpense.open();
                dbCategory.open();

                if(requestFrom.equals("detailPage")){
                    dbExpense.deleteByID(rowID);
                }
                else if(requestFrom.equals("historyHomePage")){
                    dbExpense.deleteByMonth(category,month,year);
                }
                else if(requestFrom.equals("categoryDelete")){
                    dbCategory.deleteCategory(category);
                    dbExpense.deleteCategory(category);
                }
                else if(requestFrom.equals("recentTransHomePage")){
                    int recentStatus = Integer.parseInt(defaultPrefs.getString("prefRecent","0"));

                    if(recentStatus ==0){
                        day=ExpenseDate.getDay();
                        dbExpense.deleteByDay(category,day,month,year);
                    }
                    else if(recentStatus ==1){
                        day=ExpenseDate.getYesterdayDate();
                        dbExpense.deleteByDay(category,day,month,year);
                    }
                    else if(recentStatus ==2){
                        dbExpense.deleteLast10(category);
                    }
                }

                dbCategory.close();
                dbExpense.close();


                //to call onActivityResult in previous Activity
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
        }
    }
}
