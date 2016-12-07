package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PickTimePeriodDetails extends AppCompatActivity {

    private TextView selectedRoomTextView;
    private TextView selectedDateTextView;
    private TextView timeLimitTextView;
    private TextView seekBarTextView;
    private EditText startTimeEditText;
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
        timeLimitTextView = (TextView) findViewById(R.id.timeLimitTextView);
        seekBarTextView = (TextView) findViewById(R.id.seekBarTextView);
        startTimeEditText = (EditText) findViewById(R.id.startTimeEditText);
        hoursSeekBar = (SeekBar) findViewById(R.id.hoursSeekBar);

        Intent intentFromPickBookingTimeActivity = getIntent();
        room = intentFromPickBookingTimeActivity.getIntExtra("Room", 0);
        date = intentFromPickBookingTimeActivity.getStringExtra("Date");
        TimePeriod timePeriod = intentFromPickBookingTimeActivity.getExtras().getParcelable("SelectedPeriod");

        // Update TextView
        selectedRoomTextView.setText(getString(R.string.selected_room, room));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date ddate = sdf.parse(date);
            selectedDateTextView.setText(getString(R.string.selected_date, sdf.format(ddate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sdf = new SimpleDateFormat("HH:mm");
        try {
            timeLimitTextView.setText(getString(R.string.limit_time, sdf.format(sdf.parse(timePeriod.getStartTime())), sdf.format(sdf.parse(timePeriod.getEndTime()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // EditText

        // SeekView
        hoursSeekBar.setOnSeekBarChangeListener(hoursChangeListener);
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
    public void showRoomBookingDetails(View view)
    {
        // Get login studentID
        prefs = getSharedPreferences(MainActivity.STUDENT_PREFS, 0);
        int studentId = prefs.getInt("studentId", 0);

        float hoursUsed = (float) (hoursSeekBar.getProgress() + 30) / 60;
        String startTime = startTimeEditText.getText().toString() + ":00";
        RoomBooking roomBooking = new RoomBooking(room, studentId, date, startTime, hoursUsed);

        // Store to database
        db = new DBHelper(this);
        db.addRoomBooking(roomBooking);

        Intent roomBookingDetailsIntent = new Intent(this, RoomBookingDetailsActivity.class);

        roomBookingDetailsIntent.putExtra("RoomBooking", roomBooking);

        startActivity(roomBookingDetailsIntent);
    }
}
