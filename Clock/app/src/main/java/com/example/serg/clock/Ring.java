package com.example.serg.clock;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

public class Ring extends AppCompatActivity {
    int time_per;
    String APP_PREFERENCES_ALL_APPS = "allApps";
    TextView textView;
    ArrayList<String> allApps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        Button perenos = findViewById(R.id.Perenos);
        Button switch_of = findViewById(R.id.Switch_off);
        final TextView textView = findViewById(R.id.textView);

        SharedPreferences mSettings;
        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = mSettings.edit();
        time_per = mSettings.getInt(MainActivity.APP_PREFERENCES_TIME_PERENOS,5);

        if(mSettings.contains(APP_PREFERENCES_ALL_APPS)){//доработай идею что инфа о заблокированных приложенияй храниться в памяти телефона
            allApps = null;
            for (String a : mSettings.getString(APP_PREFERENCES_ALL_APPS,"").split(" ")){
                allApps.add(a);
            }

        }else{
            allApps = getAllApps();
            //allApps.remove(getForegroundApp());//надо бы удалить сам будильник из приложений
        }

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
          final Ringtone ringtone = RingtoneManager.getRingtone(this, notification);
          ringtone.play();

          switch_of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //android.os.Process.killProcess(android.os.Process.myUid());
                ringtone.stop();
                textView.setText("Destroy");
                onStop();
                onDestroy();

            }
        });
        perenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String App = allApps.remove(/*new Random().nextInt(allApps.size() - 1)*/ 0);
                textView.setText(App + " " + getForegroundApp());

                String aa = "";
                for (String i:allApps) {
                    aa+=i + " ";
                }
                editor.putString(APP_PREFERENCES_ALL_APPS,aa);
                new Block(App).start();
                //showHomeScreen();
                final Intent my_intent = new Intent(getApplicationContext(), Ring.AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(Ring.this,0,my_intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + time_per * 60 * 1000,pendingIntent);
                onStop();
                //ringtone.stop();
 /*
                try {
                    Thread.sleep(time_per*60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                //setContentView(R.layout.activity_ring);
               // ringtone.play();
                //onStart();
                onDestroy();//довь в анроид манифест     <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />s
            }
        });
    }
    protected void onStop(){
        super.onStop();
    }
    protected void onStart(){
        super.onStart();
    }
    protected void onDestroy(){
        super.onDestroy();
    }

    public class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        Ringtone ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
//        ringtone.play();
            //int  time_perenos = getIntent().getIntExtra("Time_perenos",time_perenos);
            Log.d("gg","yeah beach");
        }

    }
    public ArrayList<String> getAllApps(){
        PackageManager packageManager = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null); mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<String> appList1 = new ArrayList<String>();
        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo a = p.applicationInfo;
            // skip system apps if they shall not be included
            if ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            } appList1.add(p.packageName);
        }
        return appList1;
    }
    public void showHomeScreen(){
        Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
        startHomescreen.addCategory(Intent.CATEGORY_HOME);
        startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startHomescreen);
    }

    public String getForegroundApp1() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) { mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) { currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses(); currentApp = tasks.get(0).processName;
        }

        return currentApp;
    }
    public String getForegroundApp(){
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop=ar.topActivity.getPackageName();
        return activityOnTop;
    }

    public class Block extends Thread {
        String App;
        Block(String s){
            App = s;
        }
        public void run() {
            int i = 0;
            while(i <= 10){//!!!!!!!!!!!!!!!!!1
                if(App == getForegroundApp()){
                    textView = findViewById(R.id.textView);
                    textView.setText("Block");
                    showHomeScreen();

                }
                //Thread.sleep(1000);
                i++;
            }
            SharedPreferences mSettings;
            mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = mSettings.edit();

            String aa = "";
            for (String r:allApps) {
                aa+=i + " ";//!!!!!!!!!!!!!!!!!!!!!!!!!!!!1последний пробел это плохо
            }
            aa+=App;
            editor.putString(APP_PREFERENCES_ALL_APPS,aa);

        }
    }



}
