package com.fleet247.driver.data.repository.taskhandler;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtill {
    static SimpleDateFormat simpleDateFormater=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    static SimpleDateFormat simpleTimeFormater=new SimpleDateFormat("HH:mm:ss",Locale.getDefault());

    public static String getDate(){
        String date=new String();
        Calendar calendar=Calendar.getInstance();
        try {
            date=simpleDateFormater.format(new Date(calendar.getTimeInMillis()));
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return date;
    }

    public static String getTime(){
        String time=new String();
        Calendar calendar=Calendar.getInstance();
        try {
            time=simpleTimeFormater.format(new Date(calendar.getTimeInMillis()));
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return time;
    }

}
