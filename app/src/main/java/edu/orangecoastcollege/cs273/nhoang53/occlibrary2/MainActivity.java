package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.Preference;
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

/**
 * Main activity
 */
public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static final String STUDENT_PREFS = "studentPrefs"; // save infor of student
    public static final String SPLASH_PREF = "splashPrefs"; // control ON - OFF splash screen
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

    /**
     *  onCreate generates the layout when its View is created.
     *  Check the login status on SharedPreferences
     * @param savedInstanceState any saved state to restore in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // go to SplashActivity
        splashSharedPrefs = getSharedPreferences(SPLASH_PREF, 0);
        editorSplash = splashSharedPrefs.edit();
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

    /**
     * start the application
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * call the login activity
     * @param view
     */
    public void login(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * It will call the login activity if student haven't login yet.
     * Otherwise, it will call student profile activity
     * @param view
     */
    public void studentProfile(View view)
    {
        if(id == 0)
        {
            login(view);
        }
        else {
            Intent intent = new Intent(this, StudentProfileActivity.class);
            startActivity(intent);
        }
    }

    /**
     * stop the application
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     *  onResume is call when user go back to MainActivity
     *  It will turn on or off the SplashActivity
     *  Register the SharedPreference listener
     */
    @Override
    protected void onResume()
    {
        // This work on Portrait
        settingPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean switchPrefs = settingPrefs.getBoolean("splashPrefs", true);
        if(!switchPrefs) //if not true
        {
            editorSplash.putInt("splash", -1);
            editorSplash.apply();
        }
        else
        {
            if(splashSharedPrefs.getInt("splash", 0) == -1) {
                editorSplash.putInt("splash", 0);
                editorSplash.apply();
            }
        }
        super.onResume();
        // this work on Landscape
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * unregister SharePreferences listener
     */
    @Override
    protected void onPause() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /**
     * Show the Menu option setting
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Control the Options Menu setting. It will change menu title to Login or Logout
     * It also remove setting option on landscape orientated.
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            menu.findItem(R.id.action_settings).setVisible(false);

        if(id == 0)
        {
            menu.findItem(R.id.action_log).setTitle(R.string.login);
        }
        else
            menu.findItem(R.id.action_log).setTitle(R.string.logout);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Depend on what option user click. It will go to setting activity or login activity
     * or logout.
     * @param item
     * @return
     */
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

    /**
     * Start ContactActivity by use of an Intent (no data passed)
     * @param view
     */
    public void contactUs(View view)
    {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    /**
     * Pop-up Alert dialog because this function haven't set up yet.
     * @param view
     */
    public void borrowBook(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This function coming soon");
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

    /**
     * Letting user reserve room with PickDateActivity by use of an Intent (no data passed)
     * @param view
     */
    public void reserveRoom(View view)
    {
        Intent intent = new Intent(this, PickDateActivity.class);
        startActivity(intent);
    }

    /**
     * Listener to handle changes in setting of the app's shared preferences (preferences.xml)
     * If the switch preference are changed, it will change the SharedPreferences to turn on or off splashActivity
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        settingPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean switchPrefs = settingPrefs.getBoolean("splashPrefs", true);
        if(!switchPrefs) //if not true
        {
            editorSplash.putInt("splash", -1);
            editorSplash.apply();
        }
        else
        {
            if(splashSharedPrefs.getInt("splash", 0) == -1) {
                editorSplash.putInt("splash", 0);
                editorSplash.apply();
            }
        }

        /*if(key.equals(SPLASH_PREF))
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

        }*/
    }
}
