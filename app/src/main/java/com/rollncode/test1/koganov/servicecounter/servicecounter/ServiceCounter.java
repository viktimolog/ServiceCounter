package com.rollncode.test1.koganov.servicecounter.servicecounter;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServiceCounter extends Service {
    private Boolean running;
    private Controller con;
    private ExecutorService es;

    @Override
    public void onCreate() {
        super.onCreate();
        es = Executors.newFixedThreadPool(1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        running = true;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        con = new Controller(sp);

        con.setCurrentTime();
        con.saveTime();
        con.loadCounter();

        Intent intent1 = new Intent(MainActivity.BROADCAST_ACTION);
        intent1.putExtra(MainActivity.COUNTFROMSERVICE, con.getValueCounter())
                .putExtra(MainActivity.TIMEFROMSERVICE, con.getLastLaunching());
        sendBroadcast(intent1);

        MyRun mr = new MyRun();
        es.execute(mr);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        running = false;
        es.shutdownNow();
        con.saveCounter();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyRun implements Runnable {

        public void run() {
            while (running) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    con.setValueCounter(con.getValueCounter() + 1);
                    Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                    intent.putExtra(MainActivity.COUNTFROMSERVICE, con.getValueCounter())
                            .putExtra(MainActivity.TIMEFROMSERVICE, con.getLastLaunching());
                    sendBroadcast(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
