package com.example.serg.clock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button settings;
    Button addNew ;
    ArrayAdapter<String> adapter;
    public static final String APP_PREFERENCES_TIME_PERENOS = "timePerenos";
    public static final String APP_PREFERENCES_FLAG = "flag";
    public static final String APP_PREFERENCES_WAKE_UPERS = "wakeUpers";
    ArrayList<String> wakeUpers = new ArrayList<String>();
    int timePerenos = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        SharedPreferences mSettings;
        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        timePerenos = getIntent().getIntExtra("Time_perenos",timePerenos);
        editor.putInt(APP_PREFERENCES_TIME_PERENOS,timePerenos);

        if(mSettings.contains(APP_PREFERENCES_WAKE_UPERS) && getIntent().getStringExtra("Time") != null &&
                getIntent().getStringExtra("Time") != "") {
            String wk = mSettings.getString(APP_PREFERENCES_WAKE_UPERS, "");
            wk+=" " + getIntent().getStringExtra("Time");
           // editor.remove(APP_PREFERENCES_WAKE_UPERS);
            editor.putString(APP_PREFERENCES_WAKE_UPERS,wk);
        }else if (!mSettings.contains(APP_PREFERENCES_FLAG)){
            editor.putInt(APP_PREFERENCES_FLAG,1);
            editor.putString(APP_PREFERENCES_WAKE_UPERS,"Delete_me");
        }
        editor.apply();
        wakeUpers.clear();
        String s = mSettings.getString(APP_PREFERENCES_WAKE_UPERS,"");
        for (String s1: s.split(" ")) {
            wakeUpers.add(s1);
        }
        listView = findViewById(R.id.listView);
        settings = findViewById(R.id.settings);
        addNew = findViewById(R.id.newWake);


        final Intent add = new Intent(getApplicationContext(), oneWakeUper.class);
        timePerenos = getIntent().getIntExtra("Time_perenos",timePerenos);
        add.putExtra("Time_perenos",timePerenos);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, wakeUpers);
        listView.setAdapter(adapter);
        //if(getIntent().getStringExtra("Time") != null && getIntent().getStringExtra("Time") != "")
        //wakeUpers.add(getIntent().getStringExtra("Time"));


        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Ring.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Setting.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, final int position,
                                    long id) {




                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Do you want to delete this Alarm Clock")
                        .setCancelable(false)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })

                        .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                wakeUpers.remove(position);
                                String wk1 = "";
                                for (String i : wakeUpers) {
                                    wk1+= i + " ";
                                }

                                SharedPreferences mSettings;
                                mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.remove(APP_PREFERENCES_WAKE_UPERS);
                                editor.putString(APP_PREFERENCES_WAKE_UPERS,wk1);
                                editor.apply();
                                listView.setAdapter(adapter);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }



}
