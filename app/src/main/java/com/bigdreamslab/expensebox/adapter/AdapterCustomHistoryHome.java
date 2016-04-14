package com.bigdreamslab.expensebox.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigdreamslab.expensebox.R;

import java.util.ArrayList;

/**
 * Created by ATUL on 12/14/2014.
 */
public class AdapterCustomHistoryHome extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> name;
    private ArrayList<Integer> icon=null;
    private final ArrayList<String> expense;

    Typeface tf;
    public AdapterCustomHistoryHome(Activity context,ArrayList<String> categoryName,ArrayList<Integer> icon, ArrayList<String> expense) {
        super(context, R.layout.custom_listview_add_new, categoryName);
        this.context = context;
        this.icon = icon;
        this.name = categoryName;
        this.expense = expense;
        tf= Typeface.createFromAsset(context.getAssets(), "font/Medium.otf");
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_list_history_home, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.lvHHText);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.lvHHIcon);
        TextView txtExpense = (TextView) rowView.findViewById(R.id.lvHHExpense);

        txtTitle.setText(name.get(position));
        txtTitle.setTypeface(tf);

        txtExpense.setText(expense.get(position));
        txtExpense.setTypeface(tf);

        imageView.setImageResource(icon.get(position));

        return rowView;
    }
}
