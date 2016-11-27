package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StudentProfileActivity extends AppCompatActivity {

    private LinearLayout activity_student_profile;
    private TextView idTextView;
    private TextView lastNameTextView;
    private TextView firstNameTextView;
    private TextView noShowTimesTextView;
    private TextView roomReservingTextView;
    private TextView bookBorrowTextView;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confrimPasswordEditText;
    private Button roomReservingEditButton;
    private Button bookBorrowingEditButton;
    private Button resetButton;
    private Button savePasswordButton;

    private SharedPreferences prefs;

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

        oldPasswordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        confrimPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);

        roomReservingEditButton = (Button) findViewById(R.id.roomReservingEditButton);
        bookBorrowingEditButton = (Button) findViewById(R.id.bookBorrowingEditButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        savePasswordButton = (Button) findViewById(R.id.savePasswordButton);

        prefs = getSharedPreferences(MainActivity.STUDENT_PREFS, 0);
        idTextView.setText(String.valueOf(prefs.getInt("studentId", 0)));
        lastNameTextView.setText(prefs.getString("lastName", null));
        firstNameTextView.setText(prefs.getString("firstName", null));
        noShowTimesTextView.setText(String.valueOf(prefs.getInt("noShowTimes", 0)));



    }

    public void reset (View view)
    {
        oldPasswordEditText.setText("");
        newPasswordEditText.setText("");
        confrimPasswordEditText.setText("");
    }
}
