package com.dotify.music.dotify;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.HashMap;

public class Alarm extends AppCompatActivity {

    private static final int ALARM_ACTIVITY = 10001;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private boolean exists;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_builder);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        exists = false;
        id = -1;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            exists = bundle.getBoolean("exists");
            id = bundle.getInt("id");
        }

        Button deleteAlarmBtn = findViewById(R.id.delete_alarm);
        TimePicker alarmTimePicker = findViewById(R.id.timePicker);

        if (exists) {
            AlarmDatabase db = new AlarmDatabase(this);
            HashMap<String, Integer> time = db.getAlarm(id);
            if (time != null) {
                alarmTimePicker.setCurrentHour(time.get("hour"));
                alarmTimePicker.setCurrentMinute(time.get("minute"));
            }
        }

        deleteAlarmBtn.setOnClickListener((v) -> deleteAlarm(id));
    }

    @Override
    public void onBackPressed() {
        saveAndSetAlarm();
    }

    private void saveAndSetAlarm() {
        TimePicker alarmTimePicker = findViewById(R.id.timePicker);
        int hour = alarmTimePicker.getCurrentHour();
        int minute = alarmTimePicker.getCurrentMinute();
        saveAlarm(hour, minute);
        setAlarm(hour, minute);
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void saveAlarm(int hour, int minute) {
        AlarmDatabase db = new AlarmDatabase(this);
        if (!exists) {
            db.addAlarm(hour, minute);
        } else {
            db.updateAlarm(id, hour, minute);
        }
    }

    private void setAlarm(int hour, int minute) {
        ToggleButton button = findViewById(R.id.set_alarm);
        if (button.isChecked()) {
            long time;
            Toast toast = Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 400);
            toast.show();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            Intent intent = new Intent(this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
            if(System.currentTimeMillis()>time)
            {
                if (calendar.AM_PM == 0)
                    time = time + (1000*60*60*12);
                else
                    time = time + (1000*60*60*24);
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);

        }
    }

    private void deleteAlarm(int id) {
        AlarmDatabase db = new AlarmDatabase(this);
        db.dropAlarm(id);
        setResult(Activity.RESULT_OK);
        finish();
    }
}
