package com.rollncode.test1.koganov.servicecounter.servicecounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private Boolean isService;
    private Intent intent;

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

    public void saveContent()
    {
        stopService(intent);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(MainActivity.SAVECOUNTER, tvValueCounter.getText().toString());
        ed.putString(MainActivity.SAVETIME, tvLastLaunching.getText().toString());
        ed.commit();
    }

    public void loadContent()
    {
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        isService=false;

        intent = new Intent(MainActivity.this, ServiceCounter.class);

        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);

        tvLastLaunching = findViewById(R.id.tvLastLaunching);
        tvValueCounter = findViewById(R.id.tvValueCounter);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        loadContent();

        br = new BroadcastReceiver()
        {
            public void onReceive(Context context, Intent intent) {

                tvLastLaunching.setText(intent.getStringExtra(TIMEFROMSERVICE));

                tvValueCounter.setText(Integer.toString(intent.getIntExtra(COUNTFROMSERVICE,0)));
            }
        };
            IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
            registerReceiver(br, intFilt);

        btnStartService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!isService)
                {
                    startService(intent);
                    isService=true;
                }
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(isService)
                {
                    stopService(intent);
                    isService=false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        isService=false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveContent();
        unregisterReceiver(br);
    }
}
