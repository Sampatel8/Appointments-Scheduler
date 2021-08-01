package com.example.scheduleappotiment.utility;

public class MyConstant {

    public static final String EVENT_SHOW_TIME="H:mm a";
    public static final String EVENT_SHOW_DATE="dd-MMM-yyyy";


    //Appointment status
    public static final int UPCOMING_APPOINTMENT=1;
    public static final int REJECTED_APPOINTMENT=2;
    public static final int PENDING_APPOINTMENT=3;
    public static final int CANCEL_APPOINTMENT=4;
    public static final int ASK_APPOINTMENT=5;
    public static final int TAKE_APPOINTMENT=6;
    public static final int ATTEND_APPOINTMENT = 7;

    public static final String STATUS_APPROVED="Approved";
    public static final String STATUS_REJECTED="Rejected";
    public static final String STATUS_RESCHEDULE="Re-Scheduled";
    public static final String STATUS_PENDING="Pending";

    //token
    public static String mToken;

    //sharedPreferences constant
    public static final String CONTACT_ID="Contact_id";
    public static final String IS_PC="is_pc";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME="last_name";
    public static final String DEPARTMENT="department";
    public static final String MY_URL="https://3vision2-dev-ed.my.salesforce.com/services/apexrest/";
}
