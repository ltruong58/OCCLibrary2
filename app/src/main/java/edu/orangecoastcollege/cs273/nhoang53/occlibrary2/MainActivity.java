package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String STUDENT_PREFS = "studentPrefs";
    public static final String SPLASH_PREF = "splashPrefs";
    private SharedPreferences studentSharedPrefs;
    private SharedPreferences splashSharedPrefs;
    private SharedPreferences settingPrefs;
    private SharedPreferences.Editor editorSplash;
    private SharedPreferences.Editor editorStudent;

    private boolean preferencesChanged = true;

    //private Student student;

    private TextView mNotLoginTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // go to SplashActivity
        splashSharedPrefs = getSharedPreferences(SPLASH_PREF, 0);
        editorSplash = splashSharedPrefs.edit();
        Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
        if(splashSharedPrefs.getInt("splash", 0) == 0)
        {
            finish();
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        }
        else if(splashSharedPrefs.getInt("splash", 0) == 1)
        {
            editorSplash.putInt("splash", 0); // put it back to 0, so it will splash next time
            editorSplash.apply();
            Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
        }


        /*PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferenceManager.getDefaultSharedPreferences(this).
                registerOnSharedPreferenceChangeListener(preferenceChangeListener);*/

        mNotLoginTextView = (TextView) findViewById(R.id.notLoginTextView);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Restore preferences
        studentSharedPrefs = getSharedPreferences(STUDENT_PREFS, 0);
        editorStudent = studentSharedPrefs.edit();
        int id = studentSharedPrefs.getInt("studentId", 0); // if not, id = 0

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
            String lastName = studentSharedPrefs.getString("lastName", null);
            String firstName = studentSharedPrefs.getString("firstName", null);
            //int noShowTime = prefs.getInt("noShowTimes", 0);
            //student = new Student(id, lastName, firstName, "", noShowTime);
            //mNotLoginTextView.setText("Have a nice day " + firstName + " " + lastName);
            mNotLoginTextView.setText("Have a nice day ");
            mNotLoginTextView.setTextSize(18);
            //mNotLoginTextView.setEnabled(false);
        }
    }

    // login
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

    @Override
    protected void onResume()
    {
        settingPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean switchPrefs = settingPrefs.getBoolean("splashPrefs", true);
        if(!switchPrefs) //if not true
        {
            Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
            editorSplash.putInt("splash", -1);
            editorSplash.apply();
            Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
        }
        else
        {
            if(splashSharedPrefs.getInt("splash", 0) == -1) {
                editorSplash.putInt("splash", 0);
                editorSplash.apply();
                Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
            }
        }
        super.onResume();
    }

    // Setting
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get the device's current orientation
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                return true;
            case R.id.action_logout:
                editorStudent.clear();
                editorStudent.apply();

                editorSplash.putInt("splash", 1); // put it 1, so it will not splash
                editorSplash.apply();

                Toast.makeText(this, "You successfully logout", Toast.LENGTH_SHORT).show();

                startActivity(getIntent()); // refresh itself
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Preferences doesn't work
    /*private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));

                    if(key.equals(SPLASH_PREF))
                    {
                        Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));

                        if(splashSharedPrefs.getInt("splash", 0) == 0)
                        {
                            editorSplash.putInt("splash", -1);
                            editorSplash.apply();
                            Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
                        }
                        else if(splashSharedPrefs.getInt("splash", 0) == -1)
                        {
                            editorSplash.putInt("splash", 0);
                            editorSplash.apply();
                            Log.i("\nOCC Library. splash:", String.valueOf(splashSharedPrefs.getInt("splash", 0)));
                        }

                    }
                }
            };*/

    // Contact Us
    public void contactUs(View view)
    {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    /**
     * Long Truong
     */

    public void reserveRoom(View view)
    {
        Intent intent = new Intent(this, PickDateActivity.class);
        startActivity(intent);
    }
}
