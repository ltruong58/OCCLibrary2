package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PickTimePeriodDetails extends AppCompatActivity {

    private TextView selectedRoomTextView;
    private TextView selectedDateTextView;
    private TextView seekBarTextView;
    private Spinner pickTimeSpinner;
    private SeekBar hoursSeekBar;
    private String date;
    private int room;
    private DBHelper db;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time_period_details);

        selectedDateTextView = (TextView) findViewById(R.id.selectedDateTextView);
        selectedRoomTextView = (TextView) findViewById(R.id.selectedRoomTextView);
        seekBarTextView = (TextView) findViewById(R.id.seekBarTextView);
        hoursSeekBar = (SeekBar) findViewById(R.id.hoursSeekBar);

        Intent intentFromPickBookingTimeActivity = getIntent();
        room = intentFromPickBookingTimeActivity.getIntExtra("Room", 0);
        date = intentFromPickBookingTimeActivity.getStringExtra("Date");
        TimePeriod timePeriod = intentFromPickBookingTimeActivity.getExtras().getParcelable("SelectedPeriod");

        // Update TextView
        selectedRoomTextView.setText(getString(R.string.selected_room_text_view, room));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date ddate = sdf.parse(date);
            selectedDateTextView.setText(getString(R.string.selected_date_text_view, sdf.format(ddate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat("HH:mm");

        // Spinner
        pickTimeSpinner = (Spinner) findViewById(R.id.pickTimeSpinner);
        addItemsOnSpinner(pickTimeSpinner, timePeriod);

        // SeekView
        hoursSeekBar.setOnSeekBarChangeListener(hoursChangeListener);
    }

    public void addItemsOnSpinner(Spinner spinner, TimePeriod timePeriod) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        double duration = 0.5;
        List<String> list = new ArrayList<>();
        //String sTime;
        Time tStartTime = new Time(Time.valueOf(timePeriod.getStartTime()).getTime());
        Time tEndTime = Time.valueOf(timePeriod.getEndTime());
        while (tStartTime.getTime() < (tEndTime.getTime() ))
        {
            list.add(sdf.format(tStartTime));
            tStartTime = new Time(tStartTime.getTime() + (long)(duration*60*60*1000));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private SeekBar.OnSeekBarChangeListener hoursChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int stepSize = 30;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int totalMinutes = progress + 30;
            int hour = totalMinutes / 60;
            int minute = totalMinutes % 60;
            seekBarTextView.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute));
            progress = ((int)Math.round(progress/stepSize))*stepSize;
            seekBar.setProgress(progress);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    public void showRoomBookingDetails(View view) throws ParseException {
        // Get login studentID
        prefs = getSharedPreferences(MainActivity.STUDENT_PREFS, 0);
        int studentId = prefs.getInt("studentId", 0);

        float hoursUsed = (float) (hoursSeekBar.getProgress() + 30) / 60;
        String startTime = (String) pickTimeSpinner.getSelectedItem();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        Date tTime = sdf.parse(startTime);
        sdf = new SimpleDateFormat("HH:mm:ss");
        String sTime = sdf.format(tTime);
        RoomBooking roomBooking = new RoomBooking(room, studentId, date, sTime, hoursUsed);

        // Store to database
        db = new DBHelper(this);
        db.addRoomBooking(roomBooking);

        Intent roomBookingDetailsIntent = new Intent(this, RoomBookingDetailsActivity.class);

        roomBookingDetailsIntent.putExtra("RoomBooking", roomBooking);

        startActivity(roomBookingDetailsIntent);
    }
}
