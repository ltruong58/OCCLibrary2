package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class RoomBookingDetailsActivity extends AppCompatActivity {
    DBHelper db;
    private RoomBooking roomBooking;
    private TextView detailsTextView;
    private List<Student> allStudentsList;
    private List<Room> allRoomsList;
    String studentName;
    String roomName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking_details);

        db = new DBHelper(this);
        allStudentsList = db.getAllStudents();
        allRoomsList = db.getAllRooms();

        detailsTextView = (TextView) findViewById(R.id.detailsTextView);

        Intent intentFromPickTimePeriodDetails = getIntent();

        roomBooking = intentFromPickTimePeriodDetails.getExtras().getParcelable("RoomBooking");

        for(Student student : allStudentsList)
        {
            if(student.getId() == roomBooking.getmStudentId())
            {
                studentName = student.getFirstName() + student.getLastName();
                break;
            }
        }
        for(Room room : allRoomsList)
        {
            if (room.getmId() == roomBooking.getmRoomId())
            {
                roomName = room.getmName();
                break;
            }
        }
        float totalHours = roomBooking.getmHoursUsed();
        int toMinutes = (int) totalHours * 60;
        String endTime = String.format("%02d", toMinutes / 60) + ":" + String.format("%02d", toMinutes % 60);
        detailsTextView.setText(getString(R.string.details, studentName, roomBooking.getmDate(), roomName, roomBooking.getmStartTime(), String.valueOf(roomBooking.getmHoursUsed())));
    }

    public void returnToMainMenu(View view )
    {
        Intent returnToMainMenuIntent = new Intent(this, MainActivity.class);
        startActivity(returnToMainMenuIntent);
    }
}
