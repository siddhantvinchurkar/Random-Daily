package com.avolatile.randomdaily;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Copyright 2017 Siddhant Vinchurkar

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

public class SplashActivity extends AppCompatActivity {

    // Variable and Object Declaration

    Handler handler;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    boolean service_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Variable and Object initialization

        handler = new Handler();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        service_status = true;

        // View Bindings

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        /* The following block of code will check for root access and update the
         * corresponding variable's value in the Global Class. The creator of
         * Random Daily (yours truly) supports root access and ads are automatically
         * disabled if root access is detected. Hence it is crucial to check for
         * root access each time the app is launched */

        if(GlobalClass.isRootAvailable(getApplicationContext())) GlobalClass.rootAvailable = true;
        else GlobalClass.rootAvailable = false;

        /* The following chunk of code will read once from the Firebase database
         * on creation of the root view and re-read every time a value in the
         * database is changed */

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /* The 'service_status' variable is directly controlled by the creator
                 * of Random Daily (yours truly) and decides whether the services of the
                 * app are available or not */

                service_status = (boolean)dataSnapshot.child("Service Status").getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                databaseError.toException().printStackTrace();

            }
        });

        /* The following piece of code will display the splash screen for exactly
         * three seconds before starting the main activity */

        class Time implements Runnable{

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (int i=0;i<=1;i++){
                    try{
                        Thread.sleep(3000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            /* Intent to start the main activity or the introduction activity
                             * depending upon the value of 'APP_FIRST_LAUNCH' in the app's
                             * shared preferences */

                            if(GlobalClass.isAppLaunchFirst(getApplicationContext()))
                                startActivity(new Intent(SplashActivity.this, IntroductionActivity.class));
                            else
                                startActivity(new Intent(SplashActivity.this,MainActivity.class));

                        }
                    });
                }

            }}

        /* Random Daily uses an active internet connection to fetch content. Hence it is
         * absolutely necessary to check for internet access before the app starts. The
         * following piece of code ensures that the app starts only if an active
         * internet connection is detected. */

        progressBar.setIndeterminate(true);    // Sets style of ProgressBar
        progressBar.setVisibility(View.VISIBLE);    // Displays ProgressBar

        if(GlobalClass.isNetworkAvailable(getApplicationContext()))
        {

            // Update 'GlobalClass.networkAvailable' variable for future use in the package

            GlobalClass.networkAvailable = true;

            // Internet connection available; check service status
            if(service_status)
            {

                // Service is active; continue app startup process

                progressBar.setVisibility(View.GONE);   // Hides ProgressBar
                new Thread(new Time()).start();    // Starts thread

            }

            else
            {

                // Service is unavailable; notify the user and end the current activity

                /* The following block of code creates and displays an error message to the
                 *  user in the form of a dialog box */

                AlertDialog.Builder error_service_unavailable_dialog = new AlertDialog.Builder(SplashActivity.this);

                // Set the title of the dialog box

                error_service_unavailable_dialog.setTitle("Service Unavailable");

                // Set the message to be displayed in the dialog box

                error_service_unavailable_dialog.setMessage("Random Daily is currently experiencing " +
                        "technical difficulties and is temporarily unavailable. Please check back later.");

                // Set the icon of the dialog box

                error_service_unavailable_dialog.setIcon(R.mipmap.ic_launcher);

                // Prevent the user from dismissing the dialog box

                error_service_unavailable_dialog.setCancelable(false);

                // Add an action button to the dialog box

                error_service_unavailable_dialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // End the activity

                        finish();

                    }
                });

                // Create and display the dialog box to the user

                error_service_unavailable_dialog.create();
                error_service_unavailable_dialog.show();

            }

        }

        else
        {

            // Update 'GlobalClass.networkAvailable' variable for future use in the package

            GlobalClass.networkAvailable = false;

            // Internet connection unavailable; notify the user and end the current activity

            progressBar.setVisibility(View.GONE);   // Hides ProgressBar

            /* The following block of code creates and displays an error message to the
            *  user in the form of a dialog box */

            AlertDialog.Builder error_no_internet_dialog = new AlertDialog.Builder(SplashActivity.this);

            // Set the title of the dialog box

            error_no_internet_dialog.setTitle("Unable to access the internet");

            // Set the message to be displayed in the dialog box

            error_no_internet_dialog.setMessage("Random Daily uses the internet to fetch content. Please " +
                    "check your internet connection and try again.");

            // Set the icon of the dialog box

            error_no_internet_dialog.setIcon(R.mipmap.ic_launcher);

            // Prevent the user from dismissing the dialog box

            error_no_internet_dialog.setCancelable(false);

            // Add an action button to the dialog box

            error_no_internet_dialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // End the activity

                    finish();

                }
            });

            // Create and display the dialog box to the user

            error_no_internet_dialog.create();
            error_no_internet_dialog.show();

        }

    }

    @Override
    protected void onResume() {

        /* The following code will enable immersive mode for the splash screen
         * for devices running on Android 3.0 Honeycomb or higher. This will effectively
         * enable immersive mode all of the app's instances as the app is only compatible
         * with devices running on Android 4.1 Jelly Bean or higher */

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {

        /* The 'finish()' method will end the splash activity to prevent it from appearing
         * should the user press the back button */

        finish();

        super.onPause();
    }
}
