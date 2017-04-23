package com.charuka.customcalendarview_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashSet<Date> dates=new HashSet<>();
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,2017);
        c.set(Calendar.MONTH,3);
        c.set(Calendar.DAY_OF_MONTH,2);
        dates.add(c.getTime());

        CalendarComp calendarComp=(CalendarComp)findViewById(R.id.calComp);
        calendarComp.updateCalendar();

        calendarComp.updateCalendar(dates);
    }
}
