package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;

public class DialogAmountEditPLAN1 extends FragmentActivity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    EditText etAmtEditTotal,etAmtEditDesc,etAmtEditDay,etAmtEditTime;
    TextView tvAmtEditDelete,tvAmtEditDone;
    ImageView ivAmtEditDay,ivAmtEditTime;
    Spinner spAmtEditCat;

    String rowID,currency,time,category,desc;
    int day,month,year,hour,minute;

    DatabaseExpense dbExpense;
    DatabaseCategory dbCategory;

    SharedPreferences defaultPrefs;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    TimePickerDialog tpd;
    DatePickerDialog dpd;

    ArrayList<String> categoryNames;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_amount_edit_plan1);

        dbExpense = new DatabaseExpense(this);
        dbCategory = new DatabaseCategory(this);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        dbCategory.open();
        categoryNames = dbCategory.getCategoryNameSortByName();
        dbCategory.close();

        currency = defaultPrefs.getString("prefCurrency","$");

        etAmtEditTotal = (EditText) findViewById(R.id.etAmtEditTotal);
        etAmtEditDesc = (EditText) findViewById(R.id.etAmtEditDesc);
        etAmtEditDay = (EditText) findViewById(R.id.etAmtEditDay);
        etAmtEditTime = (EditText) findViewById(R.id.etAmtEditTime);

        tvAmtEditDelete = (TextView) findViewById(R.id.tvAmtEditDelete);
        tvAmtEditDone = (TextView) findViewById(R.id.tvAmtEditDone);

        ivAmtEditDay = (ImageView) findViewById(R.id.ivAmtEditCalendar);
        ivAmtEditTime = (ImageView) findViewById(R.id.ivAmtEditTime);

        spAmtEditCat = (Spinner) findViewById(R.id.spAmtEditCat);

        ivAmtEditDay.setOnClickListener(this);
        ivAmtEditTime.setOnClickListener(this);

        tvAmtEditDone.setOnClickListener(this);
        tvAmtEditDelete.setOnClickListener(this);

        etAmtEditTime.setOnClickListener(this);
        etAmtEditDay.setOnClickListener(this);
        etAmtEditTotal.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAmtEditCat.setAdapter(adapter);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           rowID = extras.getString("rowID");
        }

        dbExpense.open();
        Cursor cr = dbExpense.getExpenseDetailByRowID(rowID);
        cr.moveToFirst();

        category = cr.getString(0);
        String expense = currency+" "+String.format("%.2f",cr.getFloat(1));
        desc = cr.getString(5);
        day = Integer.parseInt(cr.getString(2));
        month = Integer.parseInt(cr.getString(3));
        year = Integer.parseInt(cr.getString(4));

        time = cr.getString(7);
        parseTime(time);


        spAmtEditCat.setSelection(categoryNames.indexOf(category));
        etAmtEditTotal.setText(expense);
        etAmtEditTime.setText(time);
        etAmtEditDay.setText(day+" "+ExpenseDate.getMonthName(month)+" "+year);


        if(desc.length()>0){
            etAmtEditDesc.setText(desc);
            etAmtEditDesc.setSelection(etAmtEditDesc.getText().length());  //to move the cursor at last
        }

        dbExpense.close();


        datePickerDialog = DatePickerDialog.newInstance(this, year, (month-1), day,false);
        timePickerDialog = TimePickerDialog.newInstance(this, hour ,minute, false, false);

        dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
        if (dpd != null) {
            dpd.setOnDateSetListener(this);
        }

        tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
        if (tpd != null) {
            tpd.setOnTimeSetListener(this);
        }

    }

    public void parseTime(String time){
        String split[] = time.split(":");
        String hourStr = split[0];
        String minuteStr = split[1];
        String suffix = minuteStr.substring(3);


        hour = Integer.parseInt(hourStr);
        if(suffix.equals("AM")){
            if(hour == 12){
                hour = 0;
            }
        }
        else if(suffix.equals("PM")){
            if(hour == 12){
                //do nothing
            }
            else{
                hour = hour+12;
            }
        }

        if(minuteStr.charAt(0)=='0'){
            minuteStr = minuteStr.substring(1,2);
        }
        else{
            minuteStr = minuteStr.substring(0,2);
        }

        minute = Integer.parseInt(minuteStr);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAmtEditDone:
                desc = etAmtEditDesc.getText().toString();
                category = spAmtEditCat.getSelectedItem().toString();

                float amountFloat;
                String temp=etAmtEditTotal.getText().toString();
                temp=temp.substring(2);
                amountFloat = Float.parseFloat(temp);//since first two character contain currency symbol


                dbExpense.open();
                dbExpense.updateEntry(amountFloat,category,desc,time,day,month,year,rowID);
                dbExpense.close();

                finish();

                break;

            case R.id.tvAmtEditDelete:

                Intent i = new Intent(this, DialogDelete.class);
                i.putExtra("rowID",rowID);
                i.putExtra("requestFrom","detailPage");
                startActivityForResult(i,1);

                break;
            case R.id.etAmtEditTotal:
                Intent in = new Intent(this, ActivityNumPad.class);
                startActivityForResult(in,2); // 2 is the requestCode which will me matched later in onActivityResult
                break;

            case R.id.ivAmtEditCalendar:
            case R.id.etAmtEditDay:
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);

                break;

            case R.id.ivAmtEditTime:
            case R.id.etAmtEditTime:
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);

                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) :
                if (resultCode == Activity.RESULT_OK) {
                    finish();
                }
                break;
            case (2) :
                if (resultCode == Activity.RESULT_OK) {
                    String amount = data.getStringExtra("amount");
                    etAmtEditTotal.setText(amount);
                }
                break;


        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        this.day = day;
        this.month = month+1;
        this.year = year;
        etAmtEditDay.setText(ExpenseDate.getMonthName(month+1)+" "+day+", "+year);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        time = ExpenseDate.getAMPMFormat(hourOfDay,minute);
        etAmtEditTime.setText(time);
    }
}
