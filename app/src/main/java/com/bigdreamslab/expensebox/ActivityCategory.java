package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigdreamslab.expensebox.adapter.AdapterCustomList;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityCategory extends Activity implements View.OnClickListener{

    ListView lvCategory;
    TextView tvHead,tvAdd;

    DatabaseCategory dbCategory;

    ArrayList<String> nameList = null;
    ArrayList<Integer> iconList = null;

    SharedPreferences defaultPrefs;
    Typeface tf,tfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_category);

        dbCategory = new DatabaseCategory(this);

        tf= Typeface.createFromAsset(getAssets(), "font/Medium.otf");
        tfLight= Typeface.createFromAsset(getAssets(), "font/Light.otf");

        tvHead = (TextView) findViewById(R.id.tvCategoryHead);
        tvAdd = (TextView) findViewById(R.id.tvAddCategory);

        tvHead.setTypeface(tf);

        lvCategory = (ListView) findViewById(R.id.lvCategoryEdit);
        tvAdd.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeListView();
    }

    public void initializeListView(){


        nameList = new ArrayList<String>();
        iconList = new ArrayList<Integer>();

        dbCategory.open();
        nameList = dbCategory.getCategoryNameSortByName();
        iconList = dbCategory.getCategoryIcon(nameList);
        dbCategory.close();

        AdapterCustomList adapter = new AdapterCustomList(this, nameList ,iconList);
        lvCategory.setAdapter(adapter);

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                String category = nameList.get(position);
                String iconId = ""+iconList.get(position);
                Intent i = new Intent(ActivityCategory.this,DialogCategoryEdit.class);
                i.putExtra("category",category);
                i.putExtra("iconId",iconId);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvAddCategory:
                startActivity(new Intent(this,DialogCategoryAdd.class));
                break;
        }
    }
}
