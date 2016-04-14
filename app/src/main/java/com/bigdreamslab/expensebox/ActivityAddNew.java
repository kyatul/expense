package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import android.support.v4.app.FragmentActivity;

import com.bigdreamslab.expensebox.adapter.AdapterCustomList;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class ActivityAddNew extends FragmentActivity implements View.OnClickListener,OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    TextView tvFinalCategory,tvSave,tvCancel,tvPick;
    ImageView ivCategoryCancel,ivNewCategoryAccept,ivSave,ivCancel,ivCalendar,ivTime;
    EditText etAmount,etCategory,etDescription,etDay,etTime;
    ListView lvCategory;
    LinearLayout llOptions;

    RelativeLayout rlTime;

    Typeface tf,tfLight;
    DatabaseCategory dbCategory;
    DatabaseExpense dbExpense;

    String categoryFromDialog,time;

    ArrayList<String> categoryName;
    ArrayList<Integer> categoryIcon;

    SharedPreferences defaultPrefs;
    StringBuilder currency;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    TimePickerDialog tpd;
    DatePickerDialog dpd;
    int day,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_new);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        currency = new StringBuilder(defaultPrefs.getString("prefCurrency","$"));

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),false);
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        day = ExpenseDate.getDay();
        month = ExpenseDate.getMonth();
        year = ExpenseDate.getYear();
        time=ExpenseDate.getAMPMFormat(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));

        dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
        if (dpd != null) {
            dpd.setOnDateSetListener(this);
        }

        tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
        if (tpd != null) {
            tpd.setOnTimeSetListener(this);
        }


        dbCategory = new DatabaseCategory(this);
        dbExpense = new DatabaseExpense(this);

        tf= Typeface.createFromAsset(getAssets(), "font/Medium.otf");
        tfLight = Typeface.createFromAsset(getAssets(),"font/Light.otf");

        llOptions = (LinearLayout) findViewById(R.id.llOptions);
        rlTime = (RelativeLayout) findViewById(R.id.rlTime);
        etTime = (EditText) findViewById(R.id.etTime);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvPick = (TextView) findViewById(R.id.tvPick);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvFinalCategory = (TextView) findViewById(R.id.tvFinalCategory);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etDay = (EditText) findViewById(R.id.etDay);
        etCategory = (EditText) findViewById(R.id.etCategory);
        lvCategory = (ListView) findViewById(R.id.lvCategory);

        ivTime = (ImageView) findViewById(R.id.ivTime);
        ivCategoryCancel = (ImageView) findViewById(R.id.ivCategoryCancel);
        ivNewCategoryAccept = (ImageView) findViewById(R.id.ivNewCategoryAccept);
        ivSave = (ImageView) findViewById(R.id.ivSave);
        ivCancel = (ImageView) findViewById(R.id.ivCancel);
        ivCalendar = (ImageView) findViewById(R.id.ivCalendar);

        tvSave.setTypeface(tf);
        tvCancel.setTypeface(tf);
        tvFinalCategory.setTypeface(tf);
        etAmount.setTypeface(tf);
        etCategory.setTypeface(tf);
        etDescription.setTypeface(tf);
        etDay.setTypeface(tf);
        etTime.setTypeface(tf);
        tvPick.setTypeface(tf);

        ivCategoryCancel.setOnClickListener(this);
        ivNewCategoryAccept.setOnClickListener(this);
        ivCalendar.setOnClickListener(this);
        etTime.setOnClickListener(this);
        ivTime.setOnClickListener(this);

        etTime.setText(time);

        etCategory.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    ivNewCategoryAccept.setVisibility(View.VISIBLE);
                    lvCategory.setVisibility(View.GONE);
                    tvPick.setVisibility(View.GONE);
                }
                else{
                    ivNewCategoryAccept.setVisibility(View.GONE);
                    lvCategory.setVisibility(View.VISIBLE);
                    tvPick.setVisibility(View.VISIBLE);
                    }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


        ivSave.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        etAmount.setOnClickListener(this);
        etDay.setOnClickListener(this);

        etDay.setText(ExpenseDate.getFormatedDate());

        dbCategory.open();
        categoryName = dbCategory.getCategoryNameSortByName();
        categoryIcon = dbCategory.getCategoryIcon(categoryName);

        AdapterCustomList adapter = new AdapterCustomList(this, categoryName , categoryIcon);
        dbCategory.close();
        lvCategory.setAdapter(adapter);

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                tvFinalCategory.setText(categoryName.get(position));

                tvPick.setVisibility(View.GONE);
                lvCategory.setVisibility(View.GONE);
                etCategory.setVisibility(View.GONE);
                tvFinalCategory.setVisibility(View.VISIBLE);
                ivCategoryCancel.setVisibility(View.VISIBLE);
                rlTime.setVisibility(View.VISIBLE);
                llOptions.setVisibility(View.VISIBLE);

            }
        });

        Intent in = new Intent(ActivityAddNew.this, ActivityNumPad.class);
        startActivityForResult(in,1); // 1 is the requestCode which will me matched later in onActivityResult
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivCategoryCancel:

                lvCategory.setVisibility(View.VISIBLE);
                tvPick.setVisibility(View.VISIBLE);
                etCategory.setVisibility(View.VISIBLE);

                tvFinalCategory.setVisibility(View.GONE);
                ivCategoryCancel.setVisibility(View.GONE);
                rlTime.setVisibility(View.GONE);
                llOptions.setVisibility(View.GONE);
                break;

            case R.id.ivNewCategoryAccept:

                String cat=etCategory.getText().toString();
                boolean duplicateCategory = false;
                for(String s: categoryName){
                    if(s.equals(cat)){
                        duplicateCategory = true;
                        Toast.makeText(this,"Category with this name already exist",Toast.LENGTH_LONG).show();
                        break;
                    }
                }

                if(!duplicateCategory){
                    Intent i = new Intent(ActivityAddNew.this, DialogCategory.class);
                    startActivityForResult(i,2); // 1 is the requestCode which will me matched later in onActivityResult
                }
                break;

            case R.id.ivSave:
            case R.id.tvSave:

                String amount;
                float amountFloat;
                if(etAmount.getText().toString().length()==0){
                    Toast.makeText(this,"whoops! you forgot to enter amount",Toast.LENGTH_SHORT).show();
                    break;
                }
                else{
                    //amount= etAmount.getText().toString();
                    String temp=etAmount.getText().toString();
                    temp=temp.substring(2);
                    amountFloat = Float.parseFloat(temp);//since first two character contain currency symbol
                }

                String description= etDescription.getText().toString();
                String category;

                if((etCategory.getText().toString().length()>0)){
                    category = etCategory.getText().toString();
                    dbCategory.open();
                    dbCategory.newEntry(etCategory.getText().toString(),categoryFromDialog);
                    dbCategory.close();
                }
                else {
                    category = tvFinalCategory.getText().toString();
                }

                newEntry(amountFloat,category,description,time,day,month,year);

                Toast.makeText(this,"Successfully Saved!!",Toast.LENGTH_SHORT).show();

                finish();
                break;

            case R.id.ivCancel:
            case R.id.tvCancel:
                        finish();
                break;
            case R.id.etAmount:
                Intent in = new Intent(ActivityAddNew.this, ActivityNumPad.class);
                startActivityForResult(in,1); // 1 is the requestCode which will me matched later in onActivityResult
                break;

            case R.id.ivCalendar:
            case R.id.etDay:
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                break;

            case R.id.ivTime:
            case R.id.etTime:
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
                    String amount = data.getStringExtra("amount");
                    etAmount.setText(amount);
                }
                break;

            case (2) :
                if (resultCode == Activity.RESULT_OK) {
                    categoryFromDialog = data.getStringExtra("category");

                    tvFinalCategory.setText(etCategory.getText().toString());

                    etCategory.setVisibility(View.GONE);
                    ivNewCategoryAccept.setVisibility(View.GONE);
                    tvFinalCategory.setVisibility(View.VISIBLE);
                    ivCategoryCancel.setVisibility(View.VISIBLE);
                    rlTime.setVisibility(View.VISIBLE);
                    llOptions.setVisibility(View.VISIBLE);

                }
        }
    }

    @Override   //method of the datepicker library
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        this.day = day;
        this.month = month+1;
        this.year = year;
        etDay.setText(ExpenseDate.getMonthName(month+1)+" "+day+", "+year);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        time = ExpenseDate.getAMPMFormat(hourOfDay,minute);
        etTime.setText(time);
    }

    public void newEntry(Float amountFloat,String category,String description,String time,int day,int month,int year){
        Float categoryBudget,categoryExpended;


        dbExpense.open();
        dbExpense.newEntry(amountFloat,category,description,time,day,month,year);
        dbExpense.close();


    }
}
