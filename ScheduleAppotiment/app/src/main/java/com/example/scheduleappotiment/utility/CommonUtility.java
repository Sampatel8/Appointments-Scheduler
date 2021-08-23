package com.example.scheduleappotiment.utility;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.scheduleappotiment.model.apimodel.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.google.firebase.crashlytics.internal.Logger.TAG;

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

    @SuppressLint("SimpleDateFormat")
    public static String getDateFromSlotObj(String date){
        if (!CommonUtility.isEmpty(date)){
            try{
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                Date day=format.parse(date);
                format=new SimpleDateFormat(MyConstant.EVENT_SHOW_DATE);
                return format.format(day);
            }catch (Exception e){
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
                Log.d(TAG, "getDateFromSlotObj: "+e.getMessage());
            }
        }
        return date;
    }

    public static String getTimeFromSlotObj(String time){
        if (!CommonUtility.isEmpty(time)){
            time= time.replace(".000Z","");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sfd=new SimpleDateFormat("HH:mm:ss");
            sfd.setTimeZone(TimeZone.getDefault());
            try {
                Date date=sfd.parse(time);
                return timeInShowEvent(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "getTimeFromSlotObj: "+e.getMessage());
            }
        }
        return null;
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

    public static String getBearerToken(){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=password&client_id=3MVG9cHH2bfKACZY3qTfeMnKcfPOqHVgbOnMLjARLVdGwC4IGLvB3RDc7sUay5KzoFZ547DdWJA7ollnJSgUR&client_secret=4E09E6E0B26783BCF53AD0DC93B6F2C05ED9B159AC7D20A948191DC20B770B94&password=appointment%401235zf8kTbOISywc0dSpmmrsfb8d&username=siddharth9365%40gmail.com.appointment");
        Request request = new Request.Builder()
                .url("https://login.salesforce.com/services/oauth2/token")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            TokenResponse response1 = new ObjectMapper().readValue(response.body().string(), TokenResponse.class);
            MyConstant.mToken = response1.getAccessToken();
            return MyConstant.mToken;
        } catch (IOException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            MyConstant.mToken=null;
            return null;
        }catch (Exception e){
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
            MyConstant.mToken=null;
            return null;
        }
    }

    public static String getTimeIn12HourMinute(Date date){
        if (date!=null){
            try{
                String format="hh:mm aa";
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                return simpleDateFormat.format(date);
            }catch (Exception e){
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
                return null;
            }
        }
        return null;
    }

    public static String getTimeIn24HourMinute(Date date){
        if (date!=null) {
            try{
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
                return simpleDateFormat.format(date);
            }catch (Exception e){
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
                return null;
            }
        }
        return null;
    }

    public static boolean isEmpty(String text){
        return text==null || text.trim().isEmpty() || TextUtils.isEmpty(text.trim());
    }

    public static Date getDateFrom24HourTime(String time){
        if (!CommonUtility.isEmpty(time)){
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat=new SimpleDateFormat("HH:mm:ss");
            try {
                return dateFormat.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getTimeForAvailabilityIn24Hour(Date date){
        if (date!=null) {
            try{
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
                return simpleDateFormat.format(date);
            }catch (Exception e){
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
                return null;
            }
        }
        return null;
    }

    public static String getMakeAppointmentDate(long appDate) {
        try{
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getDefault());
            calendar.setTimeInMillis(appDate);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,20);
            return sdf.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String changeDateFormat(String srcFormat,String destFormat,String date){
        try{
            @SuppressLint("SimpleDateFormat") SimpleDateFormat srcSDF=new SimpleDateFormat(srcFormat);
            srcSDF.setTimeZone(TimeZone.getDefault());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat destSDF=new SimpleDateFormat(destFormat);
            destSDF.setTimeZone(TimeZone.getDefault());
            Date d=srcSDF.parse(date);
            return destSDF.format(d);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateFromTime(Date time){
        try{
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdp=new SimpleDateFormat("dd-MM-yyyy");
            sdp.setTimeZone(TimeZone.getDefault());
            return sdp.format(time);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static long getTimeInMilli(String date,String source){
        try{
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat(source);
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.parse(date).getTime();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
