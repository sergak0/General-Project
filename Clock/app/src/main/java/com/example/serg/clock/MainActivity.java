package com.example.serg.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button start,stop,settings;
    TimePicker timePicker;
    AlarmManager alarmManager;
    TextView textView;
    public int time_perenos = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        textView = findViewById(R.id.int_time);
        timePicker = findViewById(R.id.timePicker);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        settings = findViewById(R.id.settings);
        //PendingIntent pendingIntent;

        time_perenos = getIntent().getIntExtra("Time_perenos",time_perenos);
        final Intent my_intent = new Intent(getApplicationContext(), Ring.AlarmReceiver.class);
        my_intent.putExtra("Time_perenos",time_perenos);
        final Calendar calendar = Calendar.getInstance();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                timePicker = findViewById(R.id.timePicker);
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                long h , m;
                long dt = (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;

                if(dt > 0){ h = dt/3600;dt%=3600;m = dt / 60 ; }
                else{ dt+=24*3600;h = dt/3600;dt%=3600;m = dt / 60 ; }
                if(m==60){ m=0;h++; }

                textView = findViewById(R.id.int_time);
                int min = timePicker.getCurrentMinute();
                String s_min=String.valueOf(min);
                if(min<10) s_min = "0" + String.valueOf(min);
                Toast.makeText(getApplicationContext(),"Будильник зазвонит в " + calendar.get(Calendar.HOUR_OF_DAY) +
                        ":" + s_min + " (через "  + h + " ч "+ m+" мин)" ,Toast.LENGTH_LONG).show();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,my_intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                //pendingIntent.cancel();
                String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
                Log.d("ddd",String.valueOf(calendar.getTimeInMillis()));
                Log.d("ff",currentDateTimeString);
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                /*
                Timer timer = new Timer();
                class UpdateTimeTask extends TimerTask {
                    int i = 0;
                    public void run() {
                        i++;
                        Log.d("jj",String.valueOf(i));
                    }
                }
                timer.schedule(new UpdateTimeTask(), 0, 1000); //тикаем каждую секунду без задержки
                 //задача для таймера
                */
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView = findViewById(R.id.int_time);
                textView.setText("будильник Выключен");
                alarmManager = null;
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Setting.class);
                startActivity(intent);
            }
        });
    }



}
