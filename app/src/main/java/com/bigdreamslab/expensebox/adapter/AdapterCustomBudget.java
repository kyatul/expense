package com.bigdreamslab.expensebox.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigdreamslab.expensebox.R;

import java.util.ArrayList;

public class AdapterCustomBudget extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> name;
    private ArrayList<Integer> icon=null;
    private final ArrayList<String> expense;
    private  ArrayList<String> budget=null;
    SharedPreferences defaultPrefs;
    String currency;

    Typeface tf,tfLight;
    public AdapterCustomBudget(Activity context,ArrayList<String> categoryName,ArrayList<Integer> icon, ArrayList<String> expense,ArrayList<String> budget) {
        super(context, R.layout.custom_listview_add_new, categoryName);
        this.context = context;
        this.icon = icon;
        this.name = categoryName;
        this.expense = expense;
        this.budget = budget;

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        currency = defaultPrefs.getString("prefCurrency","$");
        tf= Typeface.createFromAsset(context.getAssets(), "font/Medium.otf");
        tfLight= Typeface.createFromAsset(context.getAssets(), "font/Light.otf");
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_list_budget, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.lvBudgetText);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.lvBudgetIcon);
        TextView txtExpense = (TextView) rowView.findViewById(R.id.tvBudgetAmt);

        txtTitle.setText(name.get(position));
        txtTitle.setTypeface(tf);

        if(Float.parseFloat(budget.get(position))>0){
            txtExpense.setText(currency+" "+expense.get(position)+" / "+currency+" "+budget.get(position));

            if(Float.parseFloat(expense.get(position)) >= Float.parseFloat(budget.get(position))){
                txtExpense.setTextColor(0xffec6e60);
            }
        }
        else{
            txtExpense.setText("SET BUDGET");
        }


        txtExpense.setTypeface(tfLight);

        imageView.setImageResource(icon.get(position));

        return rowView;
    }
}
