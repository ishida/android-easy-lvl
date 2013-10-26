package com.example.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.i4da.easylvl.EasyLicenseChecker;

public class MainActivity extends Activity {
    private EasyLicenseChecker mLicenseChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLicenseChecker = new EasyLicenseChecker(this, new Handler());
        mLicenseChecker.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLicenseChecker != null) mLicenseChecker.finish();
    }
}
