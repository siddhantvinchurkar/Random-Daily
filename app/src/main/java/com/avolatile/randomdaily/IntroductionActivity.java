package com.avolatile.randomdaily;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;

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

public class IntroductionActivity extends AppIntro{

    // Variable and Object Declaration

    Fragment slideFragment1, slideFragment2, slideFragment3;

    @Override

    // The 'setImmersive(boolean)' method works on API 18 onwards; hence the @TargetApi(18) Annotation

    @TargetApi(18)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The 'showStatusBar(boolean)' method decides the visibility of the status bar

        showStatusBar(false);

        // The 'showSkipButton(boolean)' method decides the visibility of the skip button

        showSkipButton(true);

        // The 'setColorTransitionsEnabled(boolean)' method decides whether color transitions are enabled

        setColorTransitionsEnabled(true);

        /* The following conditional statement ensures that immersive mode is enabled only
         * on devices running Android 4.3 Jelly Bean MR2 or higher */

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2) setImmersive(true);

        // Variable and Object Initialization

        slideFragment1 = new IntroductionActivitySlideFragment1();
        slideFragment3 = new IntroductionActivitySlideFragment3();

        /* The following object (slideFragment2) is only required for rooted devices and hence will
         * be initialized only if root access is detected */

        slideFragment2 = new IntroductionActivitySlideFragment2();

        /* The 'addSlide(Fragment)' method adds slides to the introduction screen sequentially;
         * and hence it is important that slides are added in the order one wishes to see them in */

        // Slide 1 (Welcome screen)

        addSlide(slideFragment1);

        /* The following slide (slideFragment2) is only required for rooted devices and hence will
         * be added to the introduction screen only if root access is detected */

        if(GlobalClass.rootAvailable)
        {

            // Slide 2 (Root access detected; enjoy ad-free version screen)

            addSlide(slideFragment2);

        }

        // Slide 3 (Introduction screen)

        addSlide(slideFragment3);

    }

    @Override
    protected void onResume() {

        /* The following code will enable immersive mode for the introduction screen
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
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        /* When the user skips the optional introduction screen, the value of 'APP_LAUNCH_FIRST'
         * in the app's shared preferences is updated to ensure that the introduction screen is not
         * displayed more than once */

        GlobalClass.setAppLaunchFirst(getApplicationContext());

        // Take the user to the app's main activity by sending an intent

        startActivity(new Intent(IntroductionActivity.this, MainActivity.class));

        /* Destroy the introduction activity to prevent it from being displayed
         * should the user press the back button */

        finish();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        /* When the user finishes viewing the optional introduction screen, the value of 'APP_LAUNCH_FIRST'
         * in the app's shared preferences is updated to ensure that the introduction screen is not
         * displayed more than once */

        GlobalClass.setAppLaunchFirst(getApplicationContext());

        // Take the user to the app's main activity by sending an intent

        startActivity(new Intent(IntroductionActivity.this, MainActivity.class));

        /* Destroy the introduction activity to prevent it from being displayed
         * should the user press the back button */

        finish();

    }

}
