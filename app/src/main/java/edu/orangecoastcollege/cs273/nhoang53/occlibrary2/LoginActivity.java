package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText mPasswordEditText;
    private EditText mStudentIdEditText;
    private Button mLogInButton;
    private Button mResetButton;
    private TextView mLogInStatusTextView;
    private TextView mStudentIdNullTextView;
    private TextView mPasswordNullTextView;

    private DBHelper db;
    private ArrayList<Student> allStudentList;

    //public static final String STUDENT_PREFS = "studentPrefs";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private SharedPreferences splashPrefs;
    private SharedPreferences.Editor editorSplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // database operation
        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importStudentFromCSV("students.csv");
        allStudentList = db.getAllStudents();
        Log.i("\nOCC Library.", allStudentList.toString());

        mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        mStudentIdEditText = (EditText) findViewById(R.id.studentIdEditText);
        mLogInButton = (Button) findViewById(R.id.logInButton);
        mResetButton = (Button) findViewById(R.id.resetButton);
        mLogInStatusTextView = (TextView) findViewById(R.id.logInStatusTextView);
        mStudentIdNullTextView = (TextView) findViewById(R.id.studentIdNullTextView);
        mPasswordNullTextView = (TextView) findViewById(R.id.passwordNullTextView);

        mStudentIdNullTextView.setEnabled(false);
        mPasswordNullTextView.setEnabled(false);

        // prefs
        //prefs = getSharedPreferences(STUDENT_PREFS, 0);
        prefs = getSharedPreferences(MainActivity.STUDENT_PREFS, 0);

        if(prefs.getInt("studentId", 0) != 0)
        {
            //go to main page
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logIn(View view)
    {
        if(!mStudentIdEditText.getText().toString().equals("") && !mPasswordEditText.getText().toString().equals("")) {
            for (int i = 0; i < allStudentList.size(); i++) {
                if (allStudentList.get(i).getId() == Integer.parseInt(mStudentIdEditText.getText().toString())
                        && allStudentList.get(i).getPassword().equals(mPasswordEditText.getText().toString()))
                {
                    mLogInStatusTextView.setText(R.string.login_success);
                    mLogInStatusTextView.setTextColor(getResources().getColor(R.color.colorLoginSuccess));

                    hideKeyboard(getApplicationContext(), view);

                    //store data in SharedPreference
                    editor = prefs.edit(); // edit
                    editor.putInt("studentId",allStudentList.get(i).getId());
                    /*editor.putString("lastName", allStudentList.get(i).getLastName());
                    editor.putString("firstName", allStudentList.get(i).getFirstName());
                    editor.putString("password", allStudentList.get(i).getPassword());
                    editor.putInt("noShowTimes", allStudentList.get(i).getNoShowTimes());*/
                    editor.apply();

                    // prevent it go to welcome splash page
                    splashPrefs = getSharedPreferences(MainActivity.SPLASH_PREF, 0);
                    editorSplash = splashPrefs.edit();
                    editorSplash.putInt("splash", 1); // stop welcome splash page
                    editorSplash.apply();

                    //go to main page
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);

                    i = allStudentList.size(); // stop the loop
                    finish(); // finish activity

                } else {
                    mLogInStatusTextView.setText(R.string.login_fail);
                    mLogInStatusTextView.setTextColor(getResources().getColor(R.color.colorLoginFail));
                    mStudentIdNullTextView.requestFocus();
                    hideKeyboard(getApplicationContext(), view);
                }
            }
        }
        else {
            if (mStudentIdEditText.getText().toString().equals(""))
            {
                mStudentIdNullTextView.setText("*");
                mLogInStatusTextView.setText(R.string.student_id_null);
                mLogInStatusTextView.setTextColor(getResources().getColor(R.color.colorLoginFail));
            }
            else if(mPasswordEditText.getText().toString().equals(""))
            {
                mPasswordNullTextView.setText("*");
                mLogInStatusTextView.setText(R.string.password_null);
                mLogInStatusTextView.setTextColor(getResources().getColor(R.color.colorLoginFail));
            }
        }
    }

    public void reset(View view)
    {
        mStudentIdEditText.setText("");
        mPasswordEditText.setText("");
        mLogInStatusTextView.setText("");
    }

    public void hideKeyboard(Context context, View view)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onStop() {
        Log.i("OCC Library. id:", String.valueOf(prefs.getInt("studentId", 0)));
        /*Log.i("OCC Library. LastName:", prefs.getString("lastName", null));
        Log.i("OCC Library. FistName:", prefs.getString("firstName", null));
        Log.i("OCC Library. Password:", prefs.getString("password", null));
        Log.i("OCC Library. noShow:", String.valueOf(prefs.getInt("noShowTimes", 0)));
*/
        /*editor.clear();
        editor.commit();*/

        super.onStop();
    }
}
