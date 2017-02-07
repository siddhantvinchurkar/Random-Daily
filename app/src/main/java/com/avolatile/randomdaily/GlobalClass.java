package com.avolatile.randomdaily;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.scottyab.rootbeer.RootBeer;

import static android.content.Context.MODE_PRIVATE;

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

/* This class is meant for variables, objects or methods that are common
 * to two or more classes within this package */

public class GlobalClass {

    /* The following boolean variable 'networkAvailable' can be used anywhere in the
     * package to verify internet access instantly. However, it is very likely that it
     * contains a stale value and thus needs regular updating */

    public static boolean networkAvailable = false;

    /* The following boolean variable 'rootAvailable' can be used anywhere in the package
     * to verify root access instantly. However, it is very likely that it contains a
     * stale value and thus needs regular updating */

    public static boolean rootAvailable = false;

    /* The following method will check for internet access and return a boolean value:
     * true: internet access available
     * false: internet access unavailable */

    public static final boolean isNetworkAvailable(Context context)
    {

        final ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }

    /* The following method will check for root access and return a boolean value:
     * true: root access available
     * false: root access unavailable */

    public static final boolean isRootAvailable(Context context)
    {

        if(new RootBeer(context).isRooted()) return true;
        else return false;

    }

    /* The following method will check if the app is being launched for the first time
     * since installation and return a boolean value:
     * true: app has been launched for the first time since installation
     * false: app has been launched earlier after installation and this is not the first time*/

    public static final boolean isAppLaunchFirst(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.volatile.randomdaily_preferences", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("APP_LAUNCH_FIRST", true)) return true;
        else return false;

    }

    /* The following method will set the value of 'APP_LAUNCH_FIRST' in the app's shared preferences
     * to false to ensure that the introduction screen is not displayed more than once */

    public static final void setAppLaunchFirst(Context context)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences("com.volatile.randomdaily_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("APP_LAUNCH_FIRST", false);
        editor.commit();

    }

    /* The following method will create a chooser for the user to share a string using an
     * ACTION_SEND intent */

    public static final void shareText(Context context, String text)
    {

        /* The 'FLAG_ACTIVITY_NEW_TASK' flag is required to be a part of every
         * 'startActivity(Intent)' call outside of the activity class */

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check this out! \n\n" + text);
        context.startActivity(Intent.createChooser(shareIntent, "Share Via").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    /* The following method will open a link in a browser using an ACTION_VIEW intent
     * It is necessary for the string passed to be a well-formed URL or the app might crash */

    public static final void openURL(Context context, String url)
    {

        /* The 'FLAG_ACTIVITY_NEW_TASK' flag is required to be a part of every
         * 'startActivity(Intent)' call outside of the activity class */

        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

    }

}
