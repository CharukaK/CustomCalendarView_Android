package com.charuka.customcalendarview_android;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class CalendarComp extends LinearLayout implements View.OnClickListener{
    private ImageView prevBtn;
    private ImageView forwBtn;
    private TextView txtDate;
    private GridView gridView;
    private Calendar currentDate=Calendar.getInstance();
    private final int DAY_COUNT=42;
    private Date selected=new Date();
    private HashSet<Date> events;


    public CalendarComp(Context context) {
        super(context);

        initControl(context);

    }

    public CalendarComp(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initControl(context);
    }

    public CalendarComp(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CalendarComp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_calendar_component,this);

        prevBtn=(ImageView)findViewById(R.id.btnCalBack);
        forwBtn=(ImageView)findViewById(R.id.btnCalFor);
        txtDate=(TextView)findViewById(R.id.txtDate);
        gridView=(GridView)findViewById(R.id.calendarGrid);
        prevBtn.setOnClickListener(this);
        forwBtn.setOnClickListener(this);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected= (Date) parent.getItemAtPosition(position);
                updateCalendar();
            }
        });


        updateCalendar();
        txtDate.setText(new SimpleDateFormat("MMMM yyyy").format(new Date(currentDate.getTimeInMillis())));
    }

    public void updateCalendar(){
        updateCalendar(events);
    }


    public void updateCalendar(HashSet<Date> events){
        this.events=events;
        List<Date> cells=new ArrayList<>();
        Calendar calendar=(Calendar)currentDate.clone();

        //set the calendar to the begining of the month
        calendar.set(Calendar.DAY_OF_MONTH,1);

        //determine the starting fay of the month
        int monthBeginingCell=calendar.get(Calendar.DAY_OF_WEEK)-1;
        //revert calendar so that when the cells start filling taking dates from it the the
        // beginning of the month will be aligned properly with the proper day of week
        calendar.add(Calendar.DAY_OF_MONTH,-(monthBeginingCell-1));

        //filling the cells
        while(cells.size()<DAY_COUNT){
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }


        //setting the adapter
        gridView.setAdapter(new CAdapter(getContext(),cells,events));



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCalFor:
                currentDate.add(Calendar.MONTH,1);
                txtDate.setText(new SimpleDateFormat("MMMM yyyy").format(new Date(currentDate.getTimeInMillis())));
                updateCalendar();
                break;
            case R.id.btnCalBack:
                currentDate.add(Calendar.MONTH,-1);
                txtDate.setText(new SimpleDateFormat("MMMM yyyy").format(new Date(currentDate.getTimeInMillis())));
                updateCalendar();
                break;

        }
    }

    private class CAdapter extends ArrayAdapter<Date>{

        private HashSet<Date> eventDays;

        private LayoutInflater inflater;

        public CAdapter(@NonNull Context context, List<Date> days, HashSet<Date> events) {
            super(context, R.layout.calendar_day, days);
            this.eventDays=events;
            inflater=LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Date date=getItem(position);
            int day=date.getDate();
            int month=date.getMonth();
            int year=date.getYear();

            Date today=new Date();

            if(convertView==null){
                convertView=inflater.inflate(R.layout.calendar_day,parent,false);
            }




            convertView.setBackgroundResource(0);

            if(month==selected.getMonth()&&year==selected.getYear()&&day==selected.getDate()){
                convertView.setBackgroundResource(R.mipmap.blank_selected);
                ((TextView)convertView).setTextColor(Color.WHITE);
            }


            if(eventDays!=null){
                for (Date eventDate:eventDays){
                    if(eventDate.getDate()==day&&eventDate.getMonth()==month&&eventDate.getYear()==year){

                        if(month==selected.getMonth()&&year==selected.getYear()&&day==selected.getDate()){
                            convertView.setBackgroundResource(R.mipmap.reminder_white);
                        }else {
                            convertView.setBackgroundResource(R.mipmap.reminder);
                        }
                        break;
                    }
                }
            }

            ((TextView)convertView).setTypeface(null, Typeface.NORMAL);
            ((TextView)convertView).setTextColor(Color.BLACK);

            if(month!=today.getMonth()||year!=today.getYear()){
                ((TextView)convertView).setTextColor(Color.GRAY);
            }else if(day==today.getDate()){
                ((TextView)convertView).setTypeface(null, Typeface.BOLD);
                ((TextView)convertView).setTextColor(Color.BLUE);
            }

            ((TextView)convertView).setText(String.valueOf(date.getDate()));
            return convertView;
        }
    }




}
