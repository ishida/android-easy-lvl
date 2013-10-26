package com.i4da.easylvl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;

public class EasyLicenseChecker {
    //TODO: Put your app license key.
    private static final String BASE64_PUBLIC_KEY = "REPLACE THIS WITH YOUR PUBLIC KEY";
    //TODO: Generate your own 20 random bytes, and put them here.
    private static final byte[] SALT = new byte[]{
            -46, 65, 30, -128, -103, -57, 74, -64, 51, 88, -95, -45, 77, -117, -36, -113, -11, 32,
            -64, 89
    };
    private LicenseChecker mChecker = null;
    private Handler mHandler = null;
    private Activity mActivity = null;

    public EasyLicenseChecker(Activity activity, Handler handler) {
        if (activity == null) throw new IllegalArgumentException("activity is null");
        if (handler == null) throw new IllegalArgumentException("handler is null");

        mHandler = handler;
        mActivity = activity;
        String deviceId = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mChecker = new LicenseChecker(mActivity,
                new ServerManagedPolicy(mActivity, new AESObfuscator(SALT,
                        mActivity.getPackageName(), deviceId)),
                BASE64_PUBLIC_KEY);
    }

    public void start() {
        final AlertDialog waitingDialog = new AlertDialog.Builder(mActivity)
                .setMessage("Verifying License...")
                .setCancelable(false)
                .show();

        mChecker.checkAccess(new LicenseCheckerCallback() {
            public void allow(int policyReason) {
                if (mActivity.isFinishing()) return;

                waitingDialog.dismiss();
            }

            public void dontAllow(int policyReason) {
                if (mActivity.isFinishing()) return;

                waitingDialog.dismiss();
                if (policyReason == Policy.RETRY) {
                    displayTechnicalError("P" + policyReason);
                } else {
                    displayInvalidLicenseError();
                }
            }

            public void applicationError(int errorCode) {
                if (mActivity.isFinishing()) return;

                waitingDialog.dismiss();
                displayTechnicalError("E" + errorCode);
            }
        });
    }

    public void finish() {
        if (mChecker != null) mChecker.onDestroy();
    }

    private void displayTechnicalError(final String reasonCode) {
        showAlertDialog(
                "Error occurred while verifying license. Please restart the application after a while." +
                        "[" + reasonCode + "]",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActivity.finish();
                    }
                }
        );
    }

    private void displayInvalidLicenseError() {
        showAlertDialog("License is invalid. Please download the application again on the store.",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActivity.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" +
                                        mActivity.getPackageName())));
                        mActivity.finish();
                    }
                }
        );
    }

    private void showAlertDialog(final String message,
                                 final DialogInterface.OnClickListener listener) {
        mHandler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(mActivity)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("OK", listener)
                        .show();
            }
        });
    }
}
