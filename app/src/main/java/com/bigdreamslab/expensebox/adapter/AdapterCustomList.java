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
public class AdapterCustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> name;
    private final ArrayList<Integer> imageId;

    Typeface tf;
    public AdapterCustomList(Activity context,ArrayList<String>web, ArrayList<Integer> imageId) {
        super(context, R.layout.custom_listview_add_new, web);
        this.context = context;
        this.name = web;
        this.imageId = imageId;
        tf= Typeface.createFromAsset(context.getAssets(), "font/Medium.otf");
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_listview_add_new, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.lvText);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.lvIcon);

        txtTitle.setText(name.get(position));
        txtTitle.setTextColor(0xffb4ecff);
        txtTitle.setTypeface(tf);
        imageView.setImageResource(imageId.get(position));
        return rowView;
    }
}
