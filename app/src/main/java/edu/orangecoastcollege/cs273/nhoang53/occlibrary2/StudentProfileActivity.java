package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudentProfileActivity extends AppCompatActivity {

    private LinearLayout activity_student_profile;
    private TextView idTextView;
    private TextView lastNameTextView;
    private TextView firstNameTextView;
    private TextView noShowTimesTextView;
    private TextView roomReservingTextView;
    private TextView bookBorrowTextView;
    private TextView alertTextView;
    private TextView oldPasswordWrongTextView;
    private TextView confirmPasswordWrongTextView;

    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confrimPasswordEditText;
    private Button roomReservingCancelButton;
    private Button resetButton;
    private Button savePasswordButton;

    private SharedPreferences prefs;
    private DBHelper db;
    private Student student;
    private Room room;
    private RoomBooking roomBooking;
    private String roomReserving;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        activity_student_profile = (LinearLayout) findViewById(R.id.activity_student_profile);
        activity_student_profile.requestFocus();

        idTextView = (TextView) findViewById(R.id.idTextView);
        lastNameTextView = (TextView) findViewById(R.id.lastNameTextView);
        firstNameTextView = (TextView) findViewById(R.id.firstNameTextView);
        noShowTimesTextView = (TextView) findViewById(R.id.noShowTimesTextView);
        roomReservingTextView = (TextView) findViewById(R.id.roomReservingTextView);
        bookBorrowTextView = (TextView) findViewById(R.id.bookBorrowTextView);
        alertTextView = (TextView) findViewById(R.id.alertTextView);
        oldPasswordWrongTextView = (TextView) findViewById(R.id.oldPasswordWrongTextView);
        confirmPasswordWrongTextView = (TextView) findViewById(R.id.confirmPasswordWrongTextView);

        oldPasswordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        confrimPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);

        roomReservingCancelButton = (Button) findViewById(R.id.roomReservingCancelButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        savePasswordButton = (Button) findViewById(R.id.savePasswordButton);

        db = new DBHelper(this);
        // get student information
        prefs = getSharedPreferences(MainActivity.STUDENT_PREFS, 0);
        studentId = prefs.getInt("studentId", 0);
        student = db.getStudent(studentId);

        if(studentId != 0)
        {
            idTextView.setText(String.valueOf(student.getId()));
            lastNameTextView.setText(student.getLastName());
            firstNameTextView.setText(student.getFirstName());
            noShowTimesTextView.setText(String.valueOf(student.getNoShowTimes()));

            // get room name
            List<RoomBooking> roomBookings = db.getAllRoomBookings();

            Date date= Calendar.getInstance().getTime();
            for(RoomBooking booking : roomBookings)
            {
                if(booking.getmStudentId() == studentId && (date.compareTo(new Date(booking.getmDate())) < 0))
                {
                    roomBooking = booking;
                }
            }
            //roomBooking = db.getRoomBooking(studentId);
            if(roomBooking != null)
            {
                room = db.getRoom(roomBooking.getmRoomId());
                if (room != null) {
                    roomReservingTextView.setText(room.getmName());
                    roomReservingTextView.setTag(roomBooking);
                }
                else
                    roomReservingTextView.setText("0");
            }
            else {
                roomReservingTextView.setText("0");
                roomReservingCancelButton.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void cancelRoomReserving(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to cancel?");
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RoomBooking roomBooking = (RoomBooking) roomReservingTextView.getTag();
                db.deleteRoomBooking(roomBooking.getmId());
                startActivity(getIntent());
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        //builder.show();
    }

    public void reset (View view)
    {
        oldPasswordEditText.setText("");
        newPasswordEditText.setText("");
        confrimPasswordEditText.setText("");
    }

    public void changePassword(View view)
    {
        if(student.getPassword().equals(oldPasswordEditText.getText().toString()))
        {
            if(newPasswordEditText.getText().toString().equals(confrimPasswordEditText.getText().toString()))
            {
                db.changePassword(Integer.parseInt(idTextView.getText().toString()),
                        newPasswordEditText.getText().toString());
                startActivity(getIntent()); // restart activity
                finish();
                Toast.makeText(this, "Password changed", Toast.LENGTH_LONG).show();
            }
            else
            {
                alertTextView.setText(R.string.password_not_match);
                confirmPasswordWrongTextView.setText("*");
                Toast.makeText(this, "new password do not match", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            alertTextView.setText(R.string.password_wrong);
            oldPasswordWrongTextView.setText("*");
            Toast.makeText(this, "Old password do not match", Toast.LENGTH_SHORT).show();
        }
    }
}
