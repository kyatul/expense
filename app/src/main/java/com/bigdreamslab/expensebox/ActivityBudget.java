package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigdreamslab.expensebox.adapter.AdapterCustomBudget;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityBudget extends Activity {

    ListView lvBudget;
    TextView tvHead;

    DatabaseCategory dbCategory;
    DatabaseExpense dbExpense;

    ArrayList<String> nameList = null;
    ArrayList<Integer> iconList = null;
    ArrayList<String> expenseList = null;
    ArrayList<String> budgetList = null;

    SharedPreferences defaultPrefs;
    Typeface tf;
    String currency,category;
    int month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_budget);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        currency = defaultPrefs.getString("prefCurrency","$");

        month = ExpenseDate.getMonth();
        year = ExpenseDate.getYear();

        dbCategory = new DatabaseCategory(this);
        dbExpense = new DatabaseExpense(this);

        tf= Typeface.createFromAsset(getAssets(), "font/Medium.otf");
        tvHead = (TextView) findViewById(R.id.tvBudget);
        lvBudget = (ListView) findViewById(R.id.lvBudget);
        tvHead.setTypeface(tf);

        initializeListView();

    }

    public void initializeListView(){


        nameList = new ArrayList<String>();
        iconList = new ArrayList<Integer>();
        expenseList = new ArrayList<String>();
        budgetList = new ArrayList<String>();

        dbCategory.open();
        nameList = dbCategory.getCategoryNameSortByName();
        iconList = dbCategory.getCategoryIcon(nameList);
        budgetList = dbCategory.getCategoryBudgetSortByName();
        dbCategory.close();

        dbExpense.open();
        HashMap<String,String> tempMap=dbExpense.getExpenseSumByCategoryAlphabetically(month,year);
        expenseList = generateExpenseFromMap(tempMap);
        dbExpense.close();

        AdapterCustomBudget adapter = new AdapterCustomBudget(this, nameList ,iconList, expenseList,budgetList);
        lvBudget.setAdapter(adapter);

        lvBudget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                category = nameList.get(position);
                Intent i = new Intent(ActivityBudget.this, ActivityNumPad.class);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String temp=data.getStringExtra("amount");
                    temp=temp.substring(2);
                    float amount = Float.parseFloat(temp);//since first two character contain currency symbol
                    if(amount>0){
                        dbCategory.open();
                        dbCategory.updateBudget(category,amount);
                        dbCategory.close();

                        initializeListView();
                    }
                }
                break;
            }
        }
    }

    public ArrayList<String> generateExpenseFromMap(HashMap<String,String> expenseMap){
        ArrayList<String> expense = new ArrayList<String>();

        for(String category:nameList){
            if(expenseMap.containsKey(category)){
                expense.add(expenseMap.get(category));
            }
            else{
                expense.add("0.00");
            }
        }
        return expense;
    }
}
