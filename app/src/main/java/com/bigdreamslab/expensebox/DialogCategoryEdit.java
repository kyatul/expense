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

public class DialogCategoryEdit extends Activity implements View.OnClickListener{

    TextView tvCatEditSave,tvCatEditDelete;
    ImageView ivCatEdit;
    EditText etCatEdit;

    Typeface tf,tfLight;

    DatabaseCategory dbCategory;
    DatabaseExpense dbExpense;
    String category,iconChanged;
    int iconId;
    boolean isIconChanged = false;

    Resources r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_category_edit);

        dbCategory = new DatabaseCategory(this);
        dbExpense = new DatabaseExpense(this);
        r = getResources();

        tf= Typeface.createFromAsset(getAssets(), "font/Medium.otf");
        tfLight = Typeface.createFromAsset(getAssets(),"font/Light.otf");

        tvCatEditSave = (TextView) findViewById(R.id.tvCatEditSave);
        tvCatEditDelete = (TextView) findViewById(R.id.tvCatEditDelete);
        ivCatEdit = (ImageView) findViewById(R.id.ivCatEdit);
        etCatEdit = (EditText) findViewById(R.id.etCatEdit);

        ivCatEdit.setOnClickListener(this);
        tvCatEditSave.setOnClickListener(this);
        tvCatEditDelete.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category = extras.getString("category");  //  defines the category for which detail is required
            iconId = Integer.parseInt(extras.getString("iconId"));  // defines the time for which detail is required
        }

        etCatEdit.setText(category);
        etCatEdit.setSelection(etCatEdit.getText().length());  //to move the cursor at last
        ivCatEdit.setImageResource(iconId);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ivCatEdit:

                Intent i = new Intent(this, DialogCategory.class);
                startActivityForResult(i, 2); // 2 is the requestCode which will me matched later in onActivityResult

                break;
            case R.id.tvCatEditSave:

                dbCategory.open();

                if(isIconChanged){  //first icon shoud be updated otherwise in next call name of category will
                    // be changed and then we can't use old category name to update icon
                    dbCategory.updateIcon(category,iconChanged);
                }
                dbCategory.updateName(category,etCatEdit.getText().toString());
                dbCategory.close();

                dbExpense.open();
                dbExpense.updateName(category,etCatEdit.getText().toString());
                dbExpense.close();

                finish();

                break;
            case R.id.tvCatEditDelete:
                Intent i1 = new Intent(this, DialogDelete.class);
                i1.putExtra("category", category);
                i1.putExtra("requestFrom", "categoryDelete");
                startActivityForResult(i1, 1);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    finish();
                }
                break;
            case (2):
                if (resultCode == Activity.RESULT_OK) {
                    isIconChanged = true;
                    iconChanged = data.getStringExtra("category");
                    ivCatEdit.setImageResource(r.getIdentifier(iconChanged, "drawable", getPackageName()));
                }
                break;
        }
    }
}
