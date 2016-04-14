package com.bigdreamslab.expensebox;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DialogCategoryAdd extends Activity implements View.OnClickListener{

    TextView tvCatAdd;
    ImageView ivCatAdd;
    EditText etCatAdd;

    Typeface tf,tfLight;
    String iconValue;

    DatabaseCategory dbCategory;
    boolean isIconSelected = false;
    Resources r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_category_add);

        dbCategory = new DatabaseCategory(this);
        r = getResources();

        tf= Typeface.createFromAsset(getAssets(), "font/Medium.otf");
        tfLight = Typeface.createFromAsset(getAssets(),"font/Light.otf");

        tvCatAdd = (TextView) findViewById(R.id.tvCatAdd);
        ivCatAdd = (ImageView) findViewById(R.id.ivCatAdd);
        etCatAdd = (EditText) findViewById(R.id.etCatAdd);

        etCatAdd.setTypeface(tfLight);
        tvCatAdd.setTypeface(tf);

        ivCatAdd.setOnClickListener(this);
        tvCatAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ivCatAdd:

                Intent i = new Intent(this, DialogCategory.class);
                startActivityForResult(i, 1); // 1 is the requestCode which will me matched later in onActivityResult

                break;
            case R.id.tvCatAdd:
                if(isIconSelected){
                    String catName = etCatAdd.getText().toString();
                    if(catName.length()>0){
                        dbCategory.open();
                        dbCategory.newEntry(catName,iconValue);
                        dbCategory.close();

                        finish();
                    }
                    else{
                        Toast.makeText(this,"Enter name for category",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(this,"Select an image for category",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    isIconSelected = true;
                    iconValue = data.getStringExtra("category");
                    ivCatAdd.setImageResource(r.getIdentifier(iconValue, "drawable", getPackageName()));
                }
                break;

            }
        }
}
