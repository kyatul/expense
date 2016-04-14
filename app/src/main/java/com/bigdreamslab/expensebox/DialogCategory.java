package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogCategory extends Activity implements View.OnClickListener{

    ImageView ctEntertainment,ctFood,ctHealth,ctOthers,ctShopping,ctTravels,ctRandom1,ctRandom2,ctRandom3;
    TextView tvDialogCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_category);

        ctEntertainment = (ImageView) findViewById(R.id.ctEntertainment);
        ctFood = (ImageView) findViewById(R.id.ctFood);
        ctHealth = (ImageView) findViewById(R.id.ctHealth);
        ctOthers = (ImageView) findViewById(R.id.ctOthers);
        ctShopping = (ImageView) findViewById(R.id.ctShopping);
        ctTravels = (ImageView) findViewById(R.id.ctTravel);
        ctRandom1 = (ImageView) findViewById(R.id.ctRandom1);
        ctRandom2 = (ImageView) findViewById(R.id.ctRandom2);
        ctRandom3 = (ImageView) findViewById(R.id.ctRandom3);
        tvDialogCancel = (TextView) findViewById(R.id.tvDialogCancel);


        tvDialogCancel.setOnClickListener(this);
        ctRandom1.setOnClickListener(this);
        ctRandom2.setOnClickListener(this);
        ctRandom3.setOnClickListener(this);
        ctEntertainment.setOnClickListener(this);
        ctFood.setOnClickListener(this);
        ctHealth.setOnClickListener(this);
        ctOthers.setOnClickListener(this);
        ctShopping.setOnClickListener(this);
        ctTravels.setOnClickListener(this);

        CustomImageTouchListener cit = new CustomImageTouchListener(this);
        ctRandom1.setOnTouchListener(cit);
        ctRandom2.setOnTouchListener(cit);
        ctRandom3.setOnTouchListener(cit);
        ctEntertainment.setOnTouchListener(cit);
        ctFood.setOnTouchListener(cit);
        ctHealth.setOnTouchListener(cit);
        ctOthers.setOnTouchListener(cit);
        ctShopping.setOnTouchListener(cit);
        ctTravels.setOnTouchListener(cit);

        CustomTextTouchListener ctt = new CustomTextTouchListener(this,0xffec6e60);
        tvDialogCancel.setOnTouchListener(ctt);
    }

    @Override
    public void onClick(View v) {
        String category;
        Intent resultIntent;
        switch (v.getId()){
            case R.id.tvDialogCancel:
                finish();
                break;
            case R.id.ctRandom1:
                category = "random7";

                resultIntent = new Intent();
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

            case R.id.ctRandom2:
                category = "random8";

                resultIntent = new Intent();
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

            case R.id.ctRandom3:
                category = "random9";

                resultIntent = new Intent();
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

            case R.id.ctEntertainment:
                category = "random1";

                resultIntent = new Intent();
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

            case R.id.ctTravel:
                category = "random6";

                resultIntent = new Intent();
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

            case R.id.ctFood:
                category = "random2";

                resultIntent = new Intent();
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

            case R.id.ctHealth:
                category = "random3";

                resultIntent = new Intent();
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

            case R.id.ctOthers:
                category = "random4";

                resultIntent = new Intent();
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

            case R.id.ctShopping:
                category = "random5";

                resultIntent = new Intent();
                resultIntent.putExtra("category", category);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;

        }
    }
}
