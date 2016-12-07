package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

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
    private Button roomReservingEditButton;
    private Button bookBorrowingEditButton;
    private Button resetButton;
    private Button savePasswordButton;

    private SharedPreferences prefs;
    private DBHelper db;
    private Student student;
    private Room room;

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

        roomReservingEditButton = (Button) findViewById(R.id.roomReservingEditButton);
        bookBorrowingEditButton = (Button) findViewById(R.id.bookBorrowingEditButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        savePasswordButton = (Button) findViewById(R.id.savePasswordButton);

        db = new DBHelper(this);
        // get student information
        prefs = getSharedPreferences(MainActivity.STUDENT_PREFS, 0);
        int studentId = prefs.getInt("studentId", 0);
        student = db.getStudent(studentId);

        if(studentId != 0)
        {
            idTextView.setText(String.valueOf(student.getId()));
            lastNameTextView.setText(student.getLastName());
            firstNameTextView.setText(student.getFirstName());
            noShowTimesTextView.setText(String.valueOf(student.getNoShowTimes()));
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // get room name
        /*room = db.getRoom(studentId);
        roomReservingTextView.setText(room.getmName());*/
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
