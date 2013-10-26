# android-easy-lvl

Android Studio project sample with easy license checker and LVL (License Verification Library / play_licensing).  
You can implement lvl codes easily by (1)copying libraries into your project and (2)editing minimum codes.

## How to use

Download android-easy-lvl.

    $ git clone https://github.com/ishida/android-easy-lvl EasyLVLProject

Copy LicenseVerificationLibrary to Your Android Studio Project

    $ cp -r EasyLVLProject/LicenseVerificationLibrary YourAppProject/
    $ mkdir -p YourAppProject/YourApp/src/main/java/com/i4da/easylvl
    $ cp EasyLVLProject/EasyLVL/src/main/java/com/i4da/easylvl/EasyLicenseChecker.java YourAppProject/YourApp/src/main/java/com/i4da/easylvl/
    $ cd YourAppProject

Get your android application licence key on Google Play Developer Console.  
Note that your application need to be paid one and be released.

Set the key into `EasyLicenseChecker.BASE64_PUBLIC_KEY` in EasyLVL/src/main/java/com/i4da/easylvl/EasyLicenseChecker.java.

    private static final String BASE64_PUBLIC_KEY = "REPLACE THIS WITH YOUR PUBLIC KEY";

Add codes into AndroidManifest.xml

    $ vi YourApp/src/main/AndroidManifest.xml

        <manifest
            ...

            <!-- Devices >= 3 have version of Android Market that supports licensing. -->
            <uses-sdk android:minSdkVersion="3" />
            <!-- Required permission to check licensing. -->
            <uses-permission android:name="com.android.vending.CHECK_LICENSE" />

            ...
        </manifest>

Add codes into YourApp/build.gradle

    $ vi YourApp/build.gradle

        dependencies {
            ...

            compile 'com.android.support:support-v4:+'
            compile project(':LicenseVerificationLibrary')

            ...
        }

Edit settings.gradle

    $ vi settings.gradle

        include ':YourApp', ':LicenseVerificationLibrary'

Click "Sync Project with Gradle Files" and "Rebuild Project" at Android Studio Menu.

Add codes to use EasyLicenseChecker like the sample MainActivity.java.

    import com.i4da.easylvl.EasyLicenseChecker;

    ...

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

That's all.