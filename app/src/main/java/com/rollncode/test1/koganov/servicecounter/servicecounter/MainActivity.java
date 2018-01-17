package com.rollncode.test1.koganov.servicecounter.servicecounter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import static com.rollncode.test1.koganov.servicecounter.servicecounter.ServiceCounter.COUNTER;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;

    private Button btnStartService;
    private Button btnStopService;

    private TextView tvLastLaunching;
    private TextView tvValueCounter;

    private BroadcastReceiver br;

    public final static String COUNTFROMSERVICE = "COUNTFROMSERVICE";
    public final static String TIMEFROMSERVICE = "TIMEFROMSERVICE";

    public final static String SAVETIME = "SAVETIME";
    public final static String SAVECOUNTER = "SAVECOUNTER";

    public final static String BROADCAST_ACTION = "com.rollncode.test1.koganov.servicecounter.servicecounter";

    public void loadContent()
    {
        Log.d("MyTag","loadContent in Main");
        String tmp = sp.getString(SAVETIME, "");

        if(!tmp.equals(""))
            tvLastLaunching.setText(sp.getString(SAVETIME, ""));

        tmp = sp.getString(SAVECOUNTER,"");
        if(tmp.equals(""))
        {
            tvValueCounter.setText("0");
        }
        else
        {
            tvValueCounter.setText(tmp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);

        tvLastLaunching = findViewById(R.id.tvLastLaunching);
        tvValueCounter = findViewById(R.id.tvValueCounter);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        loadContent();

        br = new BroadcastReceiver()
        {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {

                tvLastLaunching.setText(intent.getStringExtra(TIMEFROMSERVICE));

                tvValueCounter.setText(Integer.toString(intent.getIntExtra(COUNTFROMSERVICE,0)));

            }

        };


            // создаем фильтр для BroadcastReceiver
            IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);

            // регистрируем (включаем) BroadcastReceiver
            registerReceiver(br, intFilt);




        btnStartService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), ServiceCounter.class));
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(), ServiceCounter.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), ServiceCounter.class));
        unregisterReceiver(br);
    }
}
