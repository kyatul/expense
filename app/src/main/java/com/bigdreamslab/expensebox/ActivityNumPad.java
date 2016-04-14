package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityNumPad extends Activity implements View.OnClickListener{


    TextView tvNumTotal,tvNum0,tvNum1,tvNum2,tvNum3,tvNum4,tvNum5,tvNum6,tvNum7,tvNum8,tvNum9,tvNumCancel,tvNumOK,tvNumDot;
    ImageView ivNumBack;
    int numBeforeDecimal=0,numAfterDecimal=0;
    boolean isDecimalPressed=false;
    SharedPreferences defaultPrefs;
    String currency;
    Typeface tf;
    long modulo = 100000000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_numpad);

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        currency = defaultPrefs.getString("prefCurrency","$");
        tf= Typeface.createFromAsset(getAssets(), "font/Android-Light.ttf");

        tvNumTotal = (TextView) findViewById(R.id.tvNumTotal);
        tvNum0 = (TextView) findViewById(R.id.tvNum0);
        tvNum1 = (TextView) findViewById(R.id.tvNum1);
        tvNum2 = (TextView) findViewById(R.id.tvNum2);
        tvNum3 = (TextView) findViewById(R.id.tvNum3);
        tvNum4 = (TextView) findViewById(R.id.tvNum4);
        tvNum5 = (TextView) findViewById(R.id.tvNum5);
        tvNum6 = (TextView) findViewById(R.id.tvNum6);
        tvNum7 = (TextView) findViewById(R.id.tvNum7);
        tvNum8 = (TextView) findViewById(R.id.tvNum8);
        tvNum9 = (TextView) findViewById(R.id.tvNum9);
        tvNumDot = (TextView) findViewById(R.id.tvNumDot);
        tvNumOK = (TextView) findViewById(R.id.tvNumOK);
        tvNumCancel = (TextView) findViewById(R.id.tvNumCancel);

        ivNumBack = (ImageView) findViewById(R.id.ivNumBack);

        tvNum1.setOnClickListener(this);
        tvNum2.setOnClickListener(this);
        tvNum3.setOnClickListener(this);
        tvNum4.setOnClickListener(this);
        tvNum5.setOnClickListener(this);
        tvNum6.setOnClickListener(this);
        tvNum7.setOnClickListener(this);
        tvNum8.setOnClickListener(this);
        tvNum9.setOnClickListener(this);
        tvNum0.setOnClickListener(this);
        tvNumOK.setOnClickListener(this);

        tvNum0.setTypeface(tf);
        tvNum1.setTypeface(tf);
        tvNum2.setTypeface(tf);
        tvNum3.setTypeface(tf);
        tvNum4.setTypeface(tf);
        tvNum5.setTypeface(tf);
        tvNum6.setTypeface(tf);
        tvNum7.setTypeface(tf);
        tvNum8.setTypeface(tf);
        tvNum9.setTypeface(tf);
        tvNumOK.setTypeface(tf);
        tvNumCancel.setTypeface(tf);
        tvNumTotal.setTypeface(tf);
        tvNumDot.setTypeface(tf);

        ivNumBack.setOnClickListener(this);
        tvNumCancel.setOnClickListener(this);
        tvNumDot.setOnClickListener(this);

        CustomTextTouchListener ctt = new CustomTextTouchListener(this,0xff0099cc);
        tvNumOK.setOnTouchListener(ctt);
        tvNumCancel.setOnTouchListener(ctt);

        CustomTextTouchListener ctt1 = new CustomTextTouchListener(this,0xfffafafa);
        tvNum0.setOnTouchListener(ctt1);
        tvNum1.setOnTouchListener(ctt1);
        tvNum2.setOnTouchListener(ctt1);
        tvNum3.setOnTouchListener(ctt1);
        tvNum4.setOnTouchListener(ctt1);
        tvNum5.setOnTouchListener(ctt1);
        tvNum6.setOnTouchListener(ctt1);
        tvNum7.setOnTouchListener(ctt1);
        tvNum8.setOnTouchListener(ctt1);
        tvNum9.setOnTouchListener(ctt1);
        tvNumDot.setOnTouchListener(ctt1);

        CustomImageTouchListener cit = new CustomImageTouchListener(this);
        ivNumBack.setOnTouchListener(cit);

        tvNumTotal.setText(currency+" 0.00");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNum1:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10+1;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        numAfterDecimal = 1;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".10");
                    }
                    else if(numAfterDecimal/10==0){ //pressed once after decimal
                        numAfterDecimal=numAfterDecimal*10+1;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal);
                    }
                    else{
                        //ignore as already two num taken after decimal
                    }
                }
                break;
            case R.id.tvNum2:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10+2;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        numAfterDecimal = 2;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".20");
                    }
                    else if(numAfterDecimal/10==0){ //pressed once after decimal
                        numAfterDecimal=numAfterDecimal*10+2;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal);
                    }
                    else{
                        //ignore as already two num taken after decimal
                    }
                }
                break;
            case R.id.tvNum3:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10+3;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        numAfterDecimal = 3;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".30");
                    }
                    else if(numAfterDecimal/10==0 ){ //pressed once after decimal
                        numAfterDecimal=numAfterDecimal*10+3;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal);
                    }
                    else{
                        //ignore as already two num taken after decimal
                    }
                }
                break;
            case R.id.tvNum4:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10+4;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        numAfterDecimal = 4;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".40");
                    }
                    else if(numAfterDecimal/10==0){ //pressed once after decimal
                        numAfterDecimal=numAfterDecimal*10+4;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal);
                    }
                    else{
                        //ignore as already two num taken after decimal
                    }
                }
                break;
            case R.id.tvNum5:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10+5;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        numAfterDecimal = 5;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".50");
                    }
                    else if(numAfterDecimal/10==0){ //pressed once after decimal
                        numAfterDecimal=numAfterDecimal*10+5;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal);
                    }
                    else{
                        //ignore as already two num taken after decimal
                    }
                }
                break;
            case R.id.tvNum6:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10+6;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        numAfterDecimal = 6;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".60");
                    }
                    else if(numAfterDecimal/10==0){ //pressed once after decimal
                        numAfterDecimal=numAfterDecimal*10+6;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal);
                    }
                    else{
                        //ignore as already two num taken after decimal
                    }
                }
                break;
            case R.id.tvNum7:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10+7;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        numAfterDecimal = 7;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".70");
                    }
                    else if(numAfterDecimal/10==0){ //pressed once after decimal
                        numAfterDecimal=numAfterDecimal*10+7;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal);
                    }
                    else{
                        //ignore as already two num taken after decimal
                    }
                }
                break;
            case R.id.tvNum8:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10+8;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        numAfterDecimal = 8;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".80");
                    }
                    else if(numAfterDecimal/10==0){ //pressed once after decimal
                        numAfterDecimal=numAfterDecimal*10+8;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal);
                    }
                    else{
                        //ignore as already two num taken after decimal
                    }
                }
                break;
            case R.id.tvNum9:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10+9;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        numAfterDecimal = 9;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".90");
                    }
                    else if(numAfterDecimal/10==0 ){ //pressed once after decimal
                        numAfterDecimal=numAfterDecimal*10+9;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal);
                    }
                    else{
                        //ignore as already two num taken after decimal
                    }
                }
                break;
            case R.id.tvNum0:
                if(!isDecimalPressed){
                    if(numBeforeDecimal/modulo == 0){
                        numBeforeDecimal=numBeforeDecimal*10;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }

                }
                else{
                    //do nothing
                }
                break;
            case R.id.tvNumDot:
                isDecimalPressed = true;
                break;
            case R.id.tvNumOK:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("amount", tvNumTotal.getText().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
            case R.id.tvNumCancel:
                finish();
                break;
            case R.id.ivNumBack:
                if(!isDecimalPressed){
                    numBeforeDecimal=numBeforeDecimal/10;
                    tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                }
                else{
                    if(numAfterDecimal == 0){ // pressed none of times before
                        isDecimalPressed = false;

                        numBeforeDecimal/=10;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }
                    else if(numAfterDecimal/10==0){ //pressed once after decimal
                        numAfterDecimal=0;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+".00");
                    }
                    else{
                        numAfterDecimal/=10;
                        tvNumTotal.setText(currency+" "+numBeforeDecimal+"."+numAfterDecimal+"0");
                    }
                }
                break;

        }
    }
}
