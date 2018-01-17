package com.rollncode.test1.koganov.servicecounter.servicecounter;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServiceCounter extends Service
{
    private Controller con;

    private ExecutorService es;

    final static String COUNTER = "COUNTER";

    @Override
    public void onCreate() {
        super.onCreate();
        es = Executors.newFixedThreadPool(2);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        con = new Controller(sp);
        con.loadCounter();

        con.setCurrentTime();
        con.saveTime();//при своем старте запускает Поток и записывает в Настройки текущее время

        Log.d("MyTag Serv Time = ",con.getLastLaunching());

        Intent intent1 = new Intent(MainActivity.BROADCAST_ACTION);
        intent1.putExtra(MainActivity.COUNTFROMSERVICE, con.getValueCounter())
                .putExtra(MainActivity.TIMEFROMSERVICE, con.getLastLaunching());
        sendBroadcast(intent1);

        MyRun mr = new MyRun();
        es.execute(mr);

        return super.onStartCommand(intent, flags, startId);//TODO
    }

    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyRun implements Runnable {

        public void run() {

            con.loadCounter(); //при запуске получает последнее значение счетчика из Настроек

//            while(true)            {
                try
                {
                    TimeUnit.SECONDS.sleep(5);
                    con.setValueCounter(con.getValueCounter()+1);
                    con.saveCounter();// по завершению работы потока записывать измененное значение счетчика в Настройки

                    Log.d("MyTag Service count= ",Integer.toString(con.getValueCounter()));

                    Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
                    intent.putExtra(MainActivity.COUNTFROMSERVICE, con.getValueCounter())
                    .putExtra(MainActivity.TIMEFROMSERVICE, con.getLastLaunching());
                    sendBroadcast(intent);
                    stopSelf();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

//            }


/*            new CountDownTimer(999999999, 5000) {

                public void onTick(long millisUntilFinished) {
                    con.setValueCounter(con.getValueCounter()+1);
                    intent.putExtra(MainActivity.COUNTFROMSERVICE, con.getValueCounter());
                    sendBroadcast(intent);

                }

                public void onFinish() {
                    con.saveToSP();//типа записали каунтер и время прицепом хоть и не надо
                }
            }.start();//счетчика*/

            //stop
        }

    }
}
