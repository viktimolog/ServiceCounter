package com.rollncode.test1.koganov.servicecounter.servicecounter;


import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Controller
{
    private String lastLaunching;
    private int valueCounter;
    private SharedPreferences sp;

    public void saveTime()
    {
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(MainActivity.SAVETIME, lastLaunching);
        ed.commit();
    }

    public void saveCounter()
    {
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(MainActivity.SAVECOUNTER, Integer.toString(valueCounter));
        ed.commit();
    }

    public void loadCounter() {
        String tmp = sp.getString(MainActivity.SAVECOUNTER,"");
        if(tmp.equals(""))
        {
            valueCounter=0;
        }
        else
        {
            valueCounter = Integer.parseInt(tmp);
        }
    }

    public Controller(SharedPreferences sp) {
        this.sp = sp;
        lastLaunching="";
        valueCounter=0;
    }

    public String getLastLaunching() {
        return lastLaunching;
    }

    public void setCurrentTime() {
        this.lastLaunching = new SimpleDateFormat
                ("EEE, d MMM yyyy, HH:mm:ss")
                .format(Calendar.getInstance().getTime());
    }

    public int getValueCounter() {
        return valueCounter;
    }

    public void setValueCounter(int valueCounter) {
        this.valueCounter = valueCounter;
    }
}
