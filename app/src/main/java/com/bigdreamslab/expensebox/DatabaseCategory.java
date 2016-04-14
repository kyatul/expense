package com.bigdreamslab.expensebox;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseCategory {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CATEGORY_NAME = "name";
    public static final String KEY_CATEGORY_ICON = "icon";
    public static final String KEY_CATEGORY_BUDGET = "budget";
    public static final String KEY_RANDOM_1 = "random1";
    public static final String KEY_RANDOM_2 = "random2";

    public static final String DATABASE_NAME = "category_database";
    public static final String DATABASE_TABLE = "category_lists";
    public static final int DATABASE_VERSION = 1;

    DbHelper ourHelper;
    Context ourContext;
    SQLiteDatabase ourDatabase;



    class DbHelper extends SQLiteOpenHelper {

        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "
                    + DATABASE_TABLE
                    + " ("
                    + KEY_ROWID+ " INTEGER PRIMARY KEY AUTOINCREMENT , "
                    + KEY_CATEGORY_NAME+ " TEXT NOT NULL, "
                    + KEY_CATEGORY_ICON + " TEXT NOT NULL, "
                    + KEY_CATEGORY_BUDGET + " INTEGER NOT NULL, "
                    + KEY_RANDOM_1 + " TEXT , "
                    + KEY_RANDOM_2 + " TEXT  ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

    }// END OF DBHELPER CLASS

    public DatabaseCategory(Context c) {
        ourContext = c;
    }

    public DatabaseCategory open() {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {

        ourHelper.close();
    }

    public SQLiteDatabase getDatabase() {
        return ourDatabase;
    }

    public void intializeDatabase() {

        ContentValues cv = new ContentValues();

        cv.put(KEY_CATEGORY_NAME, "Food and Drink");
        cv.put(KEY_CATEGORY_ICON, "food");
        cv.put(KEY_CATEGORY_BUDGET, "0.00");
        ourDatabase.insert(DATABASE_TABLE, null, cv);

        cv.put(KEY_CATEGORY_NAME, "Entertainment");
        cv.put(KEY_CATEGORY_ICON, "entertainment");
        cv.put(KEY_CATEGORY_BUDGET, "0.00");
        ourDatabase.insert(DATABASE_TABLE, null, cv);

        cv.put(KEY_CATEGORY_NAME, "Health");
        cv.put(KEY_CATEGORY_ICON, "health");
        cv.put(KEY_CATEGORY_BUDGET, "0.00");
        ourDatabase.insert(DATABASE_TABLE, null, cv);

        cv.put(KEY_CATEGORY_NAME, "Travel");
        cv.put(KEY_CATEGORY_ICON, "travel");
        cv.put(KEY_CATEGORY_BUDGET, "0.00");
        ourDatabase.insert(DATABASE_TABLE, null, cv);

        cv.put(KEY_CATEGORY_NAME, "Shopping");
        cv.put(KEY_CATEGORY_ICON, "shopping");
        cv.put(KEY_CATEGORY_BUDGET, "0.00");
        ourDatabase.insert(DATABASE_TABLE, null, cv);

        cv.put(KEY_CATEGORY_NAME, "Others");
        cv.put(KEY_CATEGORY_ICON, "others");
        cv.put(KEY_CATEGORY_BUDGET, "0.00");
        ourDatabase.insert(DATABASE_TABLE, null, cv);


    }

    public void newEntry(String name,String icon){
        ContentValues cv = new ContentValues();

        cv.put(KEY_CATEGORY_NAME, name);
        cv.put(KEY_CATEGORY_ICON, icon);
        cv.put(KEY_CATEGORY_BUDGET, "0.00");
        ourDatabase.insert(DATABASE_TABLE, null, cv);

    }

    public ArrayList<String> getCategoryName(){
        ArrayList<String> name = new ArrayList<String>();
        String[] col = new String[] { KEY_ROWID, KEY_CATEGORY_NAME};
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, null, null, null,null, null);
        cr.moveToFirst();
        while(!cr.isAfterLast()){
            name.add(cr.getString(cr.getColumnIndex(KEY_CATEGORY_NAME)));
            cr.moveToNext();
        }

        return name;
    }


    public ArrayList<String> getCategoryNameSortByName(){
        ArrayList<String> name = new ArrayList<String>();
        String[] col = new String[] { KEY_ROWID, KEY_CATEGORY_NAME};
        String sortOrder = KEY_CATEGORY_NAME + " ASC";
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, null, null, null,null,sortOrder, null);
        cr.moveToFirst();
        while(!cr.isAfterLast()){
            name.add(cr.getString(cr.getColumnIndex(KEY_CATEGORY_NAME)));
            cr.moveToNext();
        }

        return name;
    }

    public ArrayList<String> getCategoryBudgetSortByName(){
        ArrayList<String> budget = new ArrayList<String>();
        String amount,category;
        float tempFloat;

        String[] col = new String[] { KEY_ROWID, KEY_CATEGORY_NAME,KEY_CATEGORY_BUDGET};
        String sortOrder = KEY_CATEGORY_NAME + " ASC";
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, null, null, null,null,sortOrder, null);
        cr.moveToFirst();
        while(!cr.isAfterLast()){
            amount = cr.getString(cr.getColumnIndex(KEY_CATEGORY_BUDGET));

            //to format the amount with two decimal places
            tempFloat=Float.parseFloat(amount);
            amount = String.format("%.2f",tempFloat);
            budget.add(amount);
            cr.moveToNext();
        }

        return budget;
    }


    public ArrayList<Integer> getCategoryIcon(){
        ArrayList<Integer> icon = new ArrayList<Integer>();

        ArrayList<String> drawableName = new ArrayList<String>();
        String[] col = new String[] { KEY_ROWID, KEY_CATEGORY_ICON};
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, null, null, null,null, null);
        cr.moveToFirst();
        while(!cr.isAfterLast()){
            drawableName.add(cr.getString(cr.getColumnIndex(KEY_CATEGORY_ICON)));
            cr.moveToNext();
        }

        Resources r = ourContext.getResources();
        for(int i=0;i<drawableName.size();i++) {
            icon.add(r.getIdentifier(drawableName.get(i), "drawable", ourContext.getPackageName()));
        }
        return icon;
    }

    public ArrayList<Integer> getCategoryIcon(ArrayList<String> nameList){
        ArrayList<Integer> iconList = new ArrayList<Integer>();
        Resources r = ourContext.getResources();
        String[] col = new String[] { KEY_ROWID,KEY_CATEGORY_NAME, KEY_CATEGORY_ICON};

        for(int i=0;i<nameList.size();i++){
            String selection=KEY_CATEGORY_NAME + "='"+nameList.get(i)+"'" ;
            Cursor cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, null,null, null);
            cr.moveToFirst();

            String iconName=cr.getString(cr.getColumnIndex(KEY_CATEGORY_ICON));
            iconList.add(r.getIdentifier(iconName, "drawable", ourContext.getPackageName()));

        }

        return iconList;
    }

    public void updateBudget(String category,Float amount){
        ContentValues cv =new ContentValues();
        cv.put(KEY_CATEGORY_BUDGET,amount);

        String selection = KEY_CATEGORY_NAME + " ='"+category+"'";
        ourDatabase.update(DATABASE_TABLE,cv,selection,null);
    }

    public void updateName(String catOldName,String catNewName){
        ContentValues cv =new ContentValues();
        cv.put(KEY_CATEGORY_NAME,catNewName);

        String selection = KEY_CATEGORY_NAME + " ='"+catOldName+"'";
        ourDatabase.update(DATABASE_TABLE,cv,selection,null);
    }

    public void updateIcon(String category,String icon){
        ContentValues cv =new ContentValues();
        cv.put(KEY_CATEGORY_ICON,icon);

        String selection = KEY_CATEGORY_NAME + " ='"+category+"'";
        ourDatabase.update(DATABASE_TABLE,cv,selection,null);
    }


    public void deleteCategory(String category) {
           String selection=KEY_CATEGORY_NAME + "='"+category+"' ";
           ourDatabase.delete(DATABASE_TABLE, selection, null);
    }
}
