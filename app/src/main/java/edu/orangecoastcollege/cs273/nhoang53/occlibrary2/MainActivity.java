package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String STUDENT_PREFS = "studentPrefs";
    private SharedPreferences prefs;

    //private Student student;

    private TextView mNotLoginTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotLoginTextView = (TextView) findViewById(R.id.notLoginTextView);

        // Restore preferences
        prefs = getSharedPreferences(STUDENT_PREFS, 0);
        int id = prefs.getInt("studentId", 0); // if not, id = 0

        /*Log.i("\nOCC Library. id:", String.valueOf(id));
        ///Log.i("\nOCC Library. id:", String.valueOf(prefs.getInt("studentId", 0)));
        Log.i("OCC Library. LastName:", prefs.getString("lastName", null));
        Log.i("OCC Library. FistName:", prefs.getString("firstName", null));
        Log.i("OCC Library. noShow:", String.valueOf(prefs.getInt("noShowTimes", 0)));*/

        if(id == 0)
        {
            //login(getCurrentFocus());
            mNotLoginTextView.setText(R.string.not_login);
            mNotLoginTextView.setEnabled(true);
        }
        else
        {
            String lastName = prefs.getString("lastName", null);
            String firstName = prefs.getString("firstName", null);
            //int noShowTime = prefs.getInt("noShowTimes", 0);
            //student = new Student(id, lastName, firstName, "", noShowTime);

            mNotLoginTextView.setText("Have a nice day " + firstName + " " + lastName);
            mNotLoginTextView.setTextSize(18);
            //mNotLoginTextView.setEnabled(false);
        }

    }

    public void login(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void studentProfile(View view)
    {
        Intent intent = new Intent(this, StudentProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        /*editor.remove("studentId");
        editor.remove("lastName");
        editor.remove("firstName");
        editor.remove("noShowTimes");*/

        /*editor.clear();
        editor.apply();*/

        super.onStop();
    }
}
