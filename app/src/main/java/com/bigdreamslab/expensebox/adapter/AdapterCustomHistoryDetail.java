package com.bigdreamslab.expensebox.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bigdreamslab.expensebox.R;

import java.util.ArrayList;

/**
 * Created by ATUL on 12/14/2014.
 */
public class AdapterCustomHistoryDetail extends ArrayAdapter<String> {
    private final Activity context;
    private  ArrayList<String> date = null;
    private  ArrayList<String> desc=null;
    private final ArrayList<String> expense;
    private ArrayList<String> rowId=null;

    Typeface tf;
    public AdapterCustomHistoryDetail(Activity context,ArrayList<String> date,ArrayList<String> desc, ArrayList<String> expense,ArrayList<String> rowId) {
        super(context, R.layout.custom_listview_add_new, date);
        this.context = context;
        this.date = date;
        this.desc = desc;
        this.expense = expense;
        this.rowId = rowId;
        tf= Typeface.createFromAsset(context.getAssets(), "font/Medium.otf");
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_list_history_detail, null, true);

        TextView txtDate = (TextView) rowView.findViewById(R.id.lvDate);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.lvDesc);
        TextView txtExpense = (TextView) rowView.findViewById(R.id.lvExpenseDetail);
        TextView txtID = (TextView) rowView.findViewById(R.id.lvID);

        txtDate.setText(date.get(position));
        txtDate.setTypeface(tf);

        txtDesc.setText(desc.get(position));
        txtDesc.setTypeface(tf);

        txtExpense.setText(expense.get(position));
        txtExpense.setTypeface(tf);

        txtID.setText(rowId.get(position));
        return rowView;
    }
}
