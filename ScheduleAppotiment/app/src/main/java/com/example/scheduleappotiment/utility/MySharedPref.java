package com.example.scheduleappotiment.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {
    private SharedPreferences mSharedPreferences = null;
    private static MySharedPref mMyPref = null;

    private MySharedPref(Context context) {
        mSharedPreferences = context.getSharedPreferences("AppointmentStore", Context.MODE_PRIVATE);
    }

    public static MySharedPref getInstance(Context context) {
        if (mMyPref == null) {
            mMyPref = new MySharedPref(context);
        }
        return mMyPref;
    }

    public void setContactID(String id) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(MyConstant.CONTACT_ID, id);
        edit.apply();
    }

    public String getContactId() {
        return mSharedPreferences.getString(MyConstant.CONTACT_ID, "null");
    }

    public void setString(String key,String value){
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public String getString(String key){
        return mSharedPreferences.getString(key,"null");
    }

    public void setBoolean(String key,boolean b){
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(key, b);
        edit.apply();
    }

    public boolean getBoolean(String key){
        return mSharedPreferences.getBoolean(key,false);
    }

    public void removeAll(){
        SharedPreferences.Editor edit=mSharedPreferences.edit();
        edit.clear();
        edit.apply();
    }
}
