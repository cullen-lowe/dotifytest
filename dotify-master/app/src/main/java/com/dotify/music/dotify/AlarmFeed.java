package com.dotify.music.dotify;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class AlarmFeed extends Fragment {

    private static final int ALARM_ACTIVITY = 10001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alarm_display, container, false);

        ListView alarmList = rootView.findViewById(R.id.alarm_list);
        Button button = rootView.findViewById(R.id.alarm_add);

        alarmList.setAdapter(buildAlarmAdapter());
        button.setOnClickListener((v) -> openAlarmCreator());

        return rootView;
    }

    public void openAlarmCreator() {
        Intent intent = new Intent(getActivity(), Alarm.class);
        startActivityForResult(intent, ALARM_ACTIVITY);
    }

    private SimpleAdapter buildAlarmAdapter() {
        ArrayList<HashMap<String, String>> alarms = getAlarms();
        ArrayList<HashMap<String, String>> alarmsAdapted = new ArrayList<>();
        for (HashMap<String, String> alarm : alarms) {
            HashMap<String, String> alarmDetail = new HashMap<>();
            alarmDetail.put("time", alarm.get("hour") + ":" + alarm.get("minute"));
            alarmDetail.put("id", alarm.get("id"));
            alarmsAdapted.add(alarmDetail);
        }
        String[] from = {"time"};
        int[] to = {R.id.alarm_time};
        return new SimpleAdapter(getContext(), alarmsAdapted, R.layout.alarm_info, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                LinearLayout layout = view.findViewById(R.id.alarm_container);
                layout.setOnClickListener((v) -> editAlarm(alarmsAdapted.get(position).get("id")));
                return view;
            }
        };
    }

    private ArrayList<HashMap<String, String>> getAlarms() {
        AlarmDatabase db = new AlarmDatabase(getContext());
        ArrayList<HashMap<String, String>> alarmTimes = new ArrayList<>();
        ArrayList<HashMap<String, Integer>> alarms = db.getAlarms();

        for (HashMap<String, Integer> alarm : alarms) {
            HashMap<String, String> data = new HashMap<>();
            data.put("hour", alarm.get("hour") + "");
            data.put("minute", alarm.get("minute") + "");
            data.put("id", alarm.get("id") + "");
            alarmTimes.add(data);
        }

        return alarmTimes;
    }

    private void editAlarm(String alarmId) {
        Log.d("Alarm", "Editing " + alarmId);
        Intent intent = new Intent(getActivity(), Alarm.class);
        intent.putExtra("exists", true);
        intent.putExtra("id", Integer.parseInt(alarmId));
        startActivityForResult(intent, ALARM_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ALARM_ACTIVITY) && (resultCode == Activity.RESULT_OK)) {
            View view = getView();
            ListView listView = view.findViewById(R.id.alarm_list);
            listView.setAdapter(buildAlarmAdapter());
        }
    }
}
