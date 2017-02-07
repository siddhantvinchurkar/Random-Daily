package com.avolatile.randomdaily;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

public class MainActivity extends AppCompatActivity {

    // Variable and Object Declaration

    CardView card_1, card_2, card_3, card_4, card_5;
    TextView card_title_1, card_title_2, card_title_3, card_title_4, card_title_5, card_caption_1,
            card_caption_2, card_caption_3, card_caption_4, card_caption_5, error_fetch_content;
    ImageView card_cover_1, card_cover_2, card_cover_3, card_cover_4, card_cover_5, featured_video_thumbnail;
    AppCompatButton card_button_more_1, card_button_more_2, card_button_more_3, card_button_more_4,
                    card_button_more_5, card_button_share_1, card_button_share_2, card_button_share_3,
                    card_button_share_4, card_button_share_5;
    FloatingActionButton fab_play_featured_video, fab_share_featured_video;
    View rootView;
    DatabaseReference databaseReference;
    String featured_video_source_url, card_source_url_1, card_source_url_2, card_source_url_3,
           card_source_url_4, card_source_url_5;
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    boolean service_status, ad_displayed;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Variable and Object Initialization

        databaseReference = FirebaseDatabase.getInstance().getReference();
        service_status = true;
        ad_displayed = false;

        /* The following Objects pertain to ads and will only be
         * initialized if the user is running a non-rooted device */

        if(!GlobalClass.rootAvailable)
        {

            mInterstitialAd = new InterstitialAd(this);
            handler = new Handler();

        }

        // View Bindings

        // Cards

        card_1 = (CardView)findViewById(R.id.card_1);
        card_2 = (CardView)findViewById(R.id.card_2);
        card_3 = (CardView)findViewById(R.id.card_3);
        card_4 = (CardView)findViewById(R.id.card_4);
        card_5 = (CardView)findViewById(R.id.card_5);

        // Card Titles

        card_title_1 = (TextView)findViewById(R.id.card_title_1);
        card_title_2 = (TextView)findViewById(R.id.card_title_2);
        card_title_3 = (TextView)findViewById(R.id.card_title_3);
        card_title_4 = (TextView)findViewById(R.id.card_title_4);
        card_title_5 = (TextView)findViewById(R.id.card_title_5);

        // Card Captions

        card_caption_1 = (TextView)findViewById(R.id.card_caption_1);
        card_caption_2 = (TextView)findViewById(R.id.card_caption_2);
        card_caption_3 = (TextView)findViewById(R.id.card_caption_3);
        card_caption_4 = (TextView)findViewById(R.id.card_caption_4);
        card_caption_5 = (TextView)findViewById(R.id.card_caption_5);

        // Error Message Text View

        error_fetch_content = (TextView)findViewById(R.id.error_fetch_content);

        // Card Cover Images

        card_cover_1 = (ImageView)findViewById(R.id.card_cover_1);
        card_cover_2 = (ImageView)findViewById(R.id.card_cover_2);
        card_cover_3 = (ImageView)findViewById(R.id.card_cover_3);
        card_cover_4 = (ImageView)findViewById(R.id.card_cover_4);
        card_cover_5 = (ImageView)findViewById(R.id.card_cover_5);

        // Featured Video Thumbnail Image

        featured_video_thumbnail = (ImageView)findViewById(R.id.featured_video_thumbnail);

        // Card 'MORE' Buttons

        card_button_more_1 = (AppCompatButton)findViewById(R.id.card_button_more_1);
        card_button_more_2 = (AppCompatButton)findViewById(R.id.card_button_more_2);
        card_button_more_3 = (AppCompatButton)findViewById(R.id.card_button_more_3);
        card_button_more_4 = (AppCompatButton)findViewById(R.id.card_button_more_4);
        card_button_more_5 = (AppCompatButton)findViewById(R.id.card_button_more_5);

        // Card 'SHARE' Buttons

        card_button_share_1 = (AppCompatButton)findViewById(R.id.card_button_share_1);
        card_button_share_2 = (AppCompatButton)findViewById(R.id.card_button_share_2);
        card_button_share_3 = (AppCompatButton)findViewById(R.id.card_button_share_3);
        card_button_share_4 = (AppCompatButton)findViewById(R.id.card_button_share_4);
        card_button_share_5 = (AppCompatButton)findViewById(R.id.card_button_share_5);

        // Floating Action Buttons

        fab_play_featured_video = (FloatingActionButton)findViewById(R.id.fab_play_featured_video);
        fab_share_featured_video = (FloatingActionButton)findViewById(R.id.fab_share_featured_video);

        // Root View

        rootView = (View)findViewById(R.id.root_view);

        /* The following view binding is pertaining to ads and will only be executed
         * if the user is running a non-rooted device */

        if(!GlobalClass.rootAvailable) mAdView = (AdView)findViewById(R.id.adView);

        /* The following block of code will read the Firebase database and set the initial values of
         * all elements within the activity. Values will be kept synchronized with the database
         * and will be updated instantly if a change is detected. */

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Service Status

                service_status = (boolean)dataSnapshot.child("Service Status").getValue();

                // Card Titles

                card_title_1.setText(dataSnapshot.child("Card 1").child("Title").getValue().toString());
                card_title_2.setText(dataSnapshot.child("Card 2").child("Title").getValue().toString());
                card_title_3.setText(dataSnapshot.child("Card 3").child("Title").getValue().toString());
                card_title_4.setText(dataSnapshot.child("Card 4").child("Title").getValue().toString());
                card_title_5.setText(dataSnapshot.child("Card 5").child("Title").getValue().toString());

                // Card Captions

                card_caption_1.setText(dataSnapshot.child("Card 1").child("Caption").getValue().toString());
                card_caption_2.setText(dataSnapshot.child("Card 2").child("Caption").getValue().toString());
                card_caption_3.setText(dataSnapshot.child("Card 3").child("Caption").getValue().toString());
                card_caption_4.setText(dataSnapshot.child("Card 4").child("Caption").getValue().toString());
                card_caption_5.setText(dataSnapshot.child("Card 5").child("Caption").getValue().toString());

                // Source URLs

                card_source_url_1 = dataSnapshot.child("Card 1").child("Source URL").getValue().toString();
                card_source_url_2 = dataSnapshot.child("Card 2").child("Source URL").getValue().toString();
                card_source_url_3 = dataSnapshot.child("Card 3").child("Source URL").getValue().toString();
                card_source_url_4 = dataSnapshot.child("Card 4").child("Source URL").getValue().toString();
                card_source_url_5 = dataSnapshot.child("Card 5").child("Source URL").getValue().toString();

                featured_video_source_url = dataSnapshot.child("Featured Video Source URL").getValue().toString();

                // Cover Images

                Picasso.with(getApplicationContext()).load(dataSnapshot.child("Card 1").child("Cover URL").getValue().toString()).into(card_cover_1);
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("Card 2").child("Cover URL").getValue().toString()).into(card_cover_2);
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("Card 3").child("Cover URL").getValue().toString()).into(card_cover_3);
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("Card 4").child("Cover URL").getValue().toString()).into(card_cover_4);
                Picasso.with(getApplicationContext()).load(dataSnapshot.child("Card 5").child("Cover URL").getValue().toString()).into(card_cover_5);

                // Thumbnail Image

                Picasso.with(getApplicationContext()).load(dataSnapshot.child("Featured Video Thumbnail URL").getValue().toString()).into(featured_video_thumbnail);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                databaseError.toException().printStackTrace();

            }
        });

        // Display welcome message (snackbar) on startup

        Snackbar.make(rootView,"Welcome to Random Daily!",Snackbar.LENGTH_LONG).show();

        /* The following block of code is pertaining to ads and will only be executed
         * if the user is running a non-rooted device */

        if(!GlobalClass.rootAvailable)
        {

            // Change visibility of the banner ad view

            mAdView.setVisibility(View.VISIBLE);

            // Initialize Google Mobile Ads SDK

            MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));

            // Set Ad Unit ID for Interstitial Ads

            mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

            // Load and display banner ads in the app

            mAdView.loadAd(new AdRequest.Builder().build());

            // Load interstitial ads in the app

            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            // Wait for exactly thirty seconds to load the ad and then display it again

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    ad_displayed = false;

                }
            }, 30000);

            // Prevent the interstitial ad from being displayed over and over again

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();

                    ad_displayed = true;

                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();

                    if(mInterstitialAd.isLoaded() && !ad_displayed)
                    {

                        mInterstitialAd.show();
                        ad_displayed = true;

                    }

                }
            });

        }

        /* The following patch of code will render all cards invisible and display an error
         * message on the screen should the service or internet connectivity be unavailable */

        if(!GlobalClass.networkAvailable || !service_status)
        {

            // Render floating action buttons invisible

            fab_play_featured_video.setVisibility(View.GONE);
            fab_share_featured_video.setVisibility(View.GONE);

            // Render all cards invisible

            card_1.setVisibility(View.GONE);
            card_2.setVisibility(View.GONE);
            card_3.setVisibility(View.GONE);
            card_4.setVisibility(View.GONE);
            card_5.setVisibility(View.GONE);

            // Display error message

            error_fetch_content.setVisibility(View.VISIBLE);

        }

        // OnClickListeners for all buttons

        fab_play_featured_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.openURL(getApplicationContext(), featured_video_source_url);

            }
        });

        card_button_more_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.openURL(getApplicationContext(), card_source_url_1);

            }
        });

        card_button_more_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.openURL(getApplicationContext(), card_source_url_2);

            }
        });

        card_button_more_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.openURL(getApplicationContext(), card_source_url_3);

            }
        });

        card_button_more_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.openURL(getApplicationContext(), card_source_url_4);

            }
        });

        card_button_more_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.openURL(getApplicationContext(), card_source_url_5);

            }
        });

        fab_share_featured_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.shareText(getApplicationContext(), featured_video_source_url);

            }
        });

        card_button_share_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.shareText(getApplicationContext(), card_source_url_1);

            }
        });

        card_button_share_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.shareText(getApplicationContext(), card_source_url_2);

            }
        });

        card_button_share_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.shareText(getApplicationContext(), card_source_url_3);

            }
        });

        card_button_share_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.shareText(getApplicationContext(), card_source_url_4);

            }
        });

        card_button_share_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.shareText(getApplicationContext(), card_source_url_5);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            // Send an intent to the settings activity

            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
