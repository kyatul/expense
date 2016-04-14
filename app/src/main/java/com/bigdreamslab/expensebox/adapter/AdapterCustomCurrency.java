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

public class AdapterCustomCurrency extends ArrayAdapter<String> {
    private final Activity context;
    private  String [] symbol = null;
    private  String [] desc=null;

    Typeface tf;
    public AdapterCustomCurrency(Activity context,String [] symbol,String [] desc) {
        super(context, R.layout.custom_list_currency,symbol);
        this.context = context;
        this.symbol = symbol;
        this.desc = desc;
        tf= Typeface.createFromAsset(context.getAssets(), "font/Light.otf");
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_list_currency, null, true);

        TextView txtSymbol = (TextView) rowView.findViewById(R.id.tvCurSymbol);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.tvCurDesc);

        txtSymbol.setText(symbol[position]);

        txtDesc.setText(desc[position]);
        txtDesc.setTypeface(tf);

        return rowView;
    }
}
