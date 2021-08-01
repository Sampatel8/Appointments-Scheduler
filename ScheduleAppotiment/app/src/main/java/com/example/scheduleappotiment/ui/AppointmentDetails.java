package com.example.scheduleappotiment.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.scheduleappotiment.R;

public class AppointmentDetails extends AppCompatActivity {

    public static boolean isRescheduleSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);


    }
}