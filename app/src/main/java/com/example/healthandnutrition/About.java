package com.example.healthandnutrition;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-8027186520767837/8457491244");
        adView.loadAd(new AdRequest.Builder().build());
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8027186520767837/8417445875");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        adListener();

        Element about = new Element();
        about.setTitle("Version 1.0");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.logoo)
                .setDescription("Human beings do not eat nutrients, they eat food. Nutrition is the science of food and its relationship to health. Food plays an important role in health as well as in disease.\n" +
                        "Whether your goal is to lose weight, get fit or gain weight, follow Health and Nutrition: Nutrition Food Guide for getting results and living a sustainable, happy, healthy lifestyle. Are you Vegetarian or Non-Vegetarian, it is important to choose a variety of foods, including whole grains, fruits, vegetables, legumes, nuts, seeds or chicken, fish, mutton, prawns etc.")
                .addItem(about)
                .addGroup("Connect with Us")
                .addEmail("csuccessatulegwu@gmail.com")
                .addFacebook("chiboy atulegwu")
                .addInstagram("chiboy atulegwu")
                .addItem(createCopyRight())
                .create();


        setContentView(aboutPage);


    }

    private void adListener() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private Element createCopyRight() {
        Element copyRight = new Element();
        final String copyRightString = String.format("Copyright %d by HN Nutrition", Calendar.getInstance().get(Calendar.YEAR));
        copyRight.setTitle(copyRightString);
        copyRight.setIconDrawable(R.mipmap.ic_launcher);
        copyRight.setGravity(Gravity.CENTER);
        copyRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(About.this, copyRightString, Toast.LENGTH_SHORT).show();
            }
        });

    return copyRight;
    }

    @Override
    public void onBackPressed() {
        loadAds();
        super.onBackPressed();

    }

    private void loadAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
        }
    }
}