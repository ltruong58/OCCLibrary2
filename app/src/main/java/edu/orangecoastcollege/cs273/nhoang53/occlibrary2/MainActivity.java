package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
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

    private TextView mNotLoginTextView;
    int id;
    private DBHelper db;
    private Student student;


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
            Intent intent = new Intent(this, SplashActivity.class);
            finish();
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

        // Restore preferences
        studentSharedPrefs = getSharedPreferences(STUDENT_PREFS, 0);
        editorStudent = studentSharedPrefs.edit();
        id = studentSharedPrefs.getInt("studentId", 0); // if not, id = 0
        if(id == 0)
        {
            mNotLoginTextView.setText(R.string.not_login);
            mNotLoginTextView.setEnabled(true);
        }
        else
        {
            db = new DBHelper(this);
            student = db.getStudent(id);
            mNotLoginTextView.setText("Have a nice day "
                                    + student.getFirstName() + " "
                                    + student.getLastName());
            mNotLoginTextView.setTextSize(18);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    // login
    public void login(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void studentProfile(View view)
    {
        if(id == 0)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, StudentProfileActivity.class);
            startActivity(intent);
        }
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

    // to change value in Menu item
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(id == 0)
        {
            menu.findItem(R.id.action_log).setTitle(R.string.login);
        }
        else
            menu.findItem(R.id.action_log).setTitle(R.string.logout);
        return super.onPrepareOptionsMenu(menu);
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
            case R.id.action_log:
                if(id != 0) {
                    editorStudent.clear();
                    editorStudent.apply();

                    editorSplash.putInt("splash", 1); // put it 1, so it will not splash
                    editorSplash.apply();

                    Toast.makeText(this, "You successfully logout", Toast.LENGTH_SHORT).show();

                    this.finish();
                    startActivity(getIntent()); // refresh itself
                    return true;
                }
                else{
                    Intent intent = new Intent(this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
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

    // Up coming activity
    public void borrowBook(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This function comming soon");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        /*AlertDialog alert = builder.create();
        alert.show();*/
        builder.show();
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
