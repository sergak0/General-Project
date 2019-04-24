package com.example.serg.clock;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Ring extends AppCompatActivity {
    int time_per;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        final ArrayList<String> allApps = getAllApps();
        Button perenos = findViewById(R.id.Perenos);
        Button switch_of = findViewById(R.id.Switch_off);
//        final MediaPlayer mediaPlayer = MediaPlayer.create(Ring.this, R.raw.mymusic);
//        mediaPlayer.start();
          Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
          Ringtone ringtone = RingtoneManager.getRingtone(this, notification);
          ringtone.play();
        switch_of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.os.Process.killProcess(android.os.Process.myUid());
            }
        });
        perenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String App = allApps.remove(0);
                new Block(App).start();

                onStop();
                //mediaPlayer.stop();
                try {
                    wait(time_per*60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                onStart();
            }
        });
    }
    protected void onStop(){
        super.onStop();
    }
    protected void onStart(){
        super.onStart();
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

    public String getForegroundApp() {
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

    public class Block extends Thread {
        String App;
        Block(String s){
            App = s;
        }
        public void run() {
            int i = 0;
            while(i <= 10){//!!!!!!!!!!!!!!!!!1
                if(App == getForegroundApp()){
                    showHomeScreen();
                }
                //Thread.sleep(1000);
                i++;
            }
        }
    }



}
