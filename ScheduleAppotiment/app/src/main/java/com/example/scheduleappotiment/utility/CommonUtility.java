package com.example.scheduleappotiment.utility;

import android.os.SystemClock;

import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtility {

    public static String dateInShowEvent(long milliSec){
        try{
            SimpleDateFormat df= new SimpleDateFormat(MyConstant.EVENT_SHOW_DATE);
            return df.format(new Date(milliSec));
        }catch (Exception e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().log("dateInShowEvent time:="+milliSec);
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return "";
    }

    public static String timeInShowEvent(long milliSec){
        try{
            SimpleDateFormat format=new SimpleDateFormat(MyConstant.EVENT_SHOW_TIME);
            return format.format(new Date(milliSec));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static long getTimeInMilli(int date,int month,int year,int hour,int minute){
        try{
            Calendar c=Calendar.getInstance();
            c.set(year, month, date,hour,minute,0);
            return c.getTimeInMillis();
        }catch (Exception e){
            e.printStackTrace();

            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return 00L;
    }
}
