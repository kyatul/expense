package com.bigdreamslab.expensebox;


import java.util.Calendar;

public class ExpenseDate {


    public static String getAMPMFormat(int hour,int minute){
        String time="";
        String suffix="";
        String minuteStr="";

        if(hour<12){
            suffix = "AM";
        }
        else{
            suffix = "PM";
            hour = hour-12;
        }

        if(hour == 0){  //since 12AM is returned 0 and 12PM is returned as 12 but got deducted in above else clause and its value is also 0 now
            hour = 12;
        }

        if(minute<10){
            minuteStr = "0"+minute;
        }
        else{
            minuteStr = ""+minute;
        }
        return hour+":"+minuteStr+" "+suffix;
    }

    public static String getFormatedDate(){
        Calendar c;
        c=Calendar.getInstance();

        String date = getMonthName(c.get(Calendar.MONTH)+1)+" "+c.get(Calendar.DATE)+", "+c.get(Calendar.YEAR);
        return date;
    }

    public static String getMonthTitle(){
        Calendar c;
        c=Calendar.getInstance();
        return getMonthName(c.get(Calendar.MONTH)+1);
    }


    public static int getDay(){
        Calendar c;
        c=Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    public static int getMonth(){
        Calendar c;
        c=Calendar.getInstance();
        return (c.get(Calendar.MONTH)+1);
    }

    public static int getYear(){
        Calendar c;
        c=Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static String getMonthName(int n){
        String st=null;
        switch(n){
            case 1:
                st="January";
                break;
            case 2:
                st="February";
                break;
            case 3:
                st="March";
                break;
            case 4:
                st="April";
                break;
            case 5:
                st="May";
                break;
            case 6:
                st="June";
                break;
            case 7:
                st="July";
                break;
            case 8:
                st="August";
                break;
            case 9:
                st="September";
                break;
            case 10:
                st="October";
                break;
            case 11:
                st="November";
                break;
            case 12:
                st="December";
                break;

        }

        return st;
    }


    public static String getMonthNameInverse(String month){
        String st="";

        if(month.equals("January")){
            st="1";
        }
        else if(month.equals("February")){
            st="2";
        }
        else if(month.equals("March")){
            st="3";
        }
        else if(month.equals("April")){
            st="4";
        }
        else if(month.equals("May")){
            st="5";
        }
        else if(month.equals("June")){
            st="6";
        }
        else if(month.equals("July")){
            st="7";
        }
        else if(month.equals("August")){
            st="8";
        }
        else if(month.equals("September")){
            st="9";
        }
        else if(month.equals("October")){
            st="10";
        }
        else if(month.equals("November")){
            st="11";
        }
        else if(month.equals("December")){
            st="12";
        }
        return st;
    }

    public static  int getYesterdayDate(){
        Calendar c;
        c=Calendar.getInstance();
        c.add(Calendar.DATE, -1);

        return c.get(Calendar.DATE);
    }
}
