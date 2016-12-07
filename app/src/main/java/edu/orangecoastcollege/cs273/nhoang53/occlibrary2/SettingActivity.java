package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_setting);

        addPreferencesFromResource(R.xml.preferences);
    }
}
