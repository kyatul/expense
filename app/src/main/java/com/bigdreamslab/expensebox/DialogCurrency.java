package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigdreamslab.expensebox.adapter.AdapterCustomCurrency;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DialogCurrency extends Activity implements View.OnClickListener{

    TextView tvCancelCurrency,tvSelectCurrency;
    ListView lvCurrency;

    String [] symbolList,descList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_currency);

        tvCancelCurrency = (TextView) findViewById(R.id.tvCancelCurrency);
        tvSelectCurrency = (TextView) findViewById(R.id.tvSelectCurrency);

        tvCancelCurrency.setOnClickListener(this);
        CustomTextTouchListener ctt = new CustomTextTouchListener(this,0xffec6e60);
        tvCancelCurrency.setOnTouchListener(ctt);

        lvCurrency = (ListView) findViewById(R.id.lvCurrency);

        symbolList = getResources().getStringArray(R.array.currencylistCodes);
        descList = getResources().getStringArray(R.array.currencylist);

        AdapterCustomCurrency acc = new AdapterCustomCurrency(this,symbolList,descList);
        lvCurrency.setAdapter(acc);


        lvCurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view,int pos, long id) {


                Intent resultIntent = new Intent();
                resultIntent.putExtra("currency", symbolList[pos]);
                setResult(Activity.RESULT_OK, resultIntent);
                DialogCurrency.this.finish();

            }
        });


    }

    @Override
    public void onClick(View v) {
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
