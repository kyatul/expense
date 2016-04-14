package com.bigdreamslab.expensebox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

import java.util.HashMap;

public class DatabaseExpense {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TIME = "time";
    public static final String KEY_DAY = "day";
    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";
    public static final String KEY_RANDOM_1 = "random1";
    public static final String KEY_RANDOM_2 = "random2";

    public static final String DATABASE_NAME = "new_database";
    public static final String DATABASE_TABLE = "expense_lists";
    public static final int DATABASE_VERSION = 1;

    DbHelper ourHelper;
    Context ourContext;
    SQLiteDatabase ourDatabase;
    Time today;

    class DbHelper extends SQLiteOpenHelper {

        DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "
                    + DATABASE_TABLE
                    + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                    + KEY_AMOUNT + " INT NOT NULL, "
                    + KEY_CATEGORY + " TEXT NOT NULL, "
                    + KEY_DESCRIPTION + " TEXT NOT NULL, "
                    + KEY_TIME + " TEXT NOT NULL, "
                    + KEY_DAY + " TEXT NOT NULL, "
                    + KEY_MONTH + " TEXT NOT NULL, "
                    + KEY_YEAR + " TEXT NOT NULL"
                    + KEY_RANDOM_1 + " TEXT , "
                    + KEY_RANDOM_2 + " TEXT  );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

    }// END OF DBHELPER CLASS

    public DatabaseExpense(Context c) {
        ourContext = c;
    }

    public DatabaseExpense open() {

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


    public void newEntry(float expense,String category,String description,String time,int day,int month,int year) {

        ContentValues cv = new ContentValues();

        cv.put(KEY_AMOUNT, expense);
        cv.put(KEY_CATEGORY, category);
        cv.put(KEY_DESCRIPTION,description);
        cv.put(KEY_TIME,time);
        cv.put(KEY_DAY, ""+day);
        cv.put(KEY_MONTH, ""+month);
        cv.put(KEY_YEAR, ""+year);
        ourDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public void updateEntry(float expense,String category,String description,String time,int day,int month,int year,String rowID) {
        String selection = KEY_ROWID + " ="+rowID;

        ContentValues cv = new ContentValues();

        cv.put(KEY_AMOUNT, expense);
        cv.put(KEY_CATEGORY, category);
        cv.put(KEY_DESCRIPTION,description);
        cv.put(KEY_TIME,time);
        cv.put(KEY_DAY, ""+day);
        cv.put(KEY_MONTH, ""+month);
        cv.put(KEY_YEAR, ""+year);

        ourDatabase.update(DATABASE_TABLE,cv,selection,null);
    }



    public float getExpenseSum(int month,int year){
        String[] col = new String[] { "SUM("+KEY_AMOUNT+")"};
        String selection=KEY_MONTH + "='"+month+"' AND " + KEY_YEAR + "='"+year+"'" ;
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, null,null, null);
        cr.moveToFirst();
        return cr.getFloat(cr.getColumnIndex("SUM("+KEY_AMOUNT+")"));
    }

    public Cursor getExpenseSumByCategory(int month,int year){  //sort by sum
        String[] col = new String[] { "SUM("+KEY_AMOUNT+") AS SUM", KEY_CATEGORY};
        String selection=KEY_MONTH + "='"+month+"' AND " + KEY_YEAR + "='"+year+"'" ;
        String sortOrder = "SUM" + " DESC";
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, KEY_CATEGORY ,null,sortOrder, null);
        return cr;
    }


    public HashMap<String,String> getExpenseSumByCategoryAlphabetically(int month,int year){ //sort by category lexicographically

        HashMap<String,String> expenseMap = new HashMap<String,String>();
        String amount,category;
        float tempFloat;

        String[] col = new String[] { "SUM("+KEY_AMOUNT+") AS SUM", KEY_CATEGORY};
        String selection=KEY_MONTH + "='"+month+"' AND " + KEY_YEAR + "='"+year+"'" ;
        String sortOrder = KEY_CATEGORY + " ASC";
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, KEY_CATEGORY ,null,sortOrder, null);


        cr.moveToFirst();
        while(!cr.isAfterLast()){
            category = cr.getString(cr.getColumnIndex(KEY_CATEGORY));
            amount = cr.getString(cr.getColumnIndex("SUM"));

            //to format the amount with two decimal places
            tempFloat=Float.parseFloat(amount);
            amount = String.format("%.2f",tempFloat);

            expenseMap.put(category,amount);

            cr.moveToNext();
        }

        return expenseMap;
    }

    public Cursor getExpenseSumByCategoryToday(int day,int month,int year){
        String[] col = new String[] { "SUM("+KEY_AMOUNT+") AS SUM", KEY_CATEGORY};
        String selection=KEY_MONTH + "='"+month+"' AND " + KEY_YEAR + "='"+year+"'"+" AND " + KEY_DAY + "='"+day+"'" ;
        String sortOrder = "SUM" + " DESC";
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, KEY_CATEGORY ,null,sortOrder, null);
        return cr;
    }

    public Cursor getExpenseSumByCategoryLast10(){
        int max=0;
        //getting the maximum value
        String[] col = new String[] {  "MAX("+KEY_CATEGORY+")"};
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col,null, null, null ,null,null, null);
        cr.moveToFirst();
        max=cr.getInt(0);

        //getting the last 10 Transactions
        String selection;
        col = new String[] { "SUM("+KEY_AMOUNT+") AS SUM", KEY_CATEGORY};
        if(max<=10){
            selection=null;
        }
        else{
            max=max-10;
            selection=KEY_ROWID + ">="+max ;
        }

        String sortOrder = "SUM" + " DESC";
        cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, KEY_CATEGORY ,null,sortOrder, null);
        return cr;
    }


    public Cursor getExpenseDetailByCategory(String category,int month,int year){
        String[] col = new String[] {  KEY_CATEGORY, KEY_AMOUNT , KEY_DAY , KEY_MONTH ,KEY_YEAR,KEY_DESCRIPTION,KEY_ROWID,KEY_TIME};
        String selection=KEY_MONTH + "='"+month+"' AND " + KEY_YEAR + "='"+year+"' AND " + KEY_CATEGORY + "='"+category+"'";
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, null ,null,null, null);
        return cr;
    }

    public Cursor getExpenseDetailByCategoryToday(String category,int day,int month,int year){
        String[] col = new String[] {  KEY_CATEGORY, KEY_AMOUNT , KEY_DAY , KEY_MONTH ,KEY_YEAR,KEY_DESCRIPTION,KEY_ROWID,KEY_TIME};
        String selection=KEY_DAY + "='"+day+"' AND " + KEY_MONTH + "='"+month+"' AND " + KEY_YEAR + "='"+year+"' AND " + KEY_CATEGORY + "='"+category+"'";
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, null ,null,null, null);
        return cr;
    }

    public Cursor getExpenseDetailByCategoryLast10(String category){
        int max=0;
        //getting the maximum value
        String[] col = new String[] {  "MAX("+KEY_CATEGORY+")"};
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col,null, null, null ,null,null, null);
        cr.moveToFirst();
        max=cr.getInt(0);

        String selection;
        if(max<=10){
            selection=KEY_CATEGORY + "='"+category+"'";
        }
        else{
            max=max-10;
            selection=KEY_ROWID + ">="+max+" AND "+ KEY_CATEGORY + "='"+category+"'";
        }
        col = new String[] {  KEY_CATEGORY, KEY_AMOUNT  ,KEY_TIME, KEY_MONTH ,KEY_YEAR,KEY_DESCRIPTION,KEY_ROWID, KEY_DAY};

        cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, null ,null,null, null);
        return cr;
    }

    public Cursor getExpenseDetailByRowID(String rowID){
        String[] col = new String[] {  KEY_CATEGORY, KEY_AMOUNT , KEY_DAY , KEY_MONTH ,KEY_YEAR,KEY_DESCRIPTION,KEY_ROWID,KEY_TIME};
        String selection=KEY_ROWID + "="+rowID;
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col, selection, null, null ,null,null, null);
        return cr;
    }

    public void deleteByID(String rowID) {
        String selection=KEY_ROWID + "="+rowID;
        ourDatabase.delete(DATABASE_TABLE, selection, null);
    }

    public void deleteByMonth(String category,int month,int year) {
        String selection=KEY_MONTH + "='"+month+"' AND " + KEY_YEAR + "='"+year+"' AND " + KEY_CATEGORY + "='"+category+"'";
        ourDatabase.delete(DATABASE_TABLE, selection, null);
    }

    public void deleteByDay(String category,int day,int month,int year) {
        String selection=KEY_DAY + "='"+day+"' AND " +KEY_MONTH + "='"+month+"' AND " + KEY_YEAR + "='"+year+"' AND " + KEY_CATEGORY + "='"+category+"'";
        ourDatabase.delete(DATABASE_TABLE, selection, null);
    }

    public void deleteLast10(String category){
        int max=0;
        //getting the maximum value
        String[] col = new String[] {  "MAX("+KEY_CATEGORY+")"};
        Cursor cr = ourDatabase.query(DATABASE_TABLE, col,null, null, null ,null,null, null);
        cr.moveToFirst();
        max=cr.getInt(0);

        String selection;
        if(max<=10){
            selection=KEY_CATEGORY + "='"+category+"'";
        }
        else{
            max=max-10;
            selection=KEY_ROWID + ">="+max+" AND "+ KEY_CATEGORY + "='"+category+"'";
        }

        ourDatabase.delete(DATABASE_TABLE,selection,null);

    }

    public void updateName(String catOldName,String catNewName){
        ContentValues cv =new ContentValues();
        cv.put(KEY_CATEGORY,catNewName);

        String selection = KEY_CATEGORY + " ='"+catOldName+"'";
        ourDatabase.update(DATABASE_TABLE,cv,selection,null);
    }

    public void deleteCategory(String category) {
        String selection=KEY_CATEGORY + "='"+category+"' ";
        ourDatabase.delete(DATABASE_TABLE, selection, null);
    }
}
