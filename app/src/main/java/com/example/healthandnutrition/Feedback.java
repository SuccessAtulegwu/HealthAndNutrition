package com.example.healthandnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputLayout;

public class Feedback extends AppCompatActivity {

    Button submit;
    TextInputLayout name,subject,email;
    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        submit = findViewById(R.id.submit);
        subject = findViewById(R.id.subject);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        //load ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView adView = findViewById(R.id.adView);
        AdView adView2 = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView2.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8027186520767837/8417445875");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        adListener();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (!validateName() || !validateSubject() || !validateEmail()){
                   Toast.makeText(Feedback.this, "Form is empty", Toast.LENGTH_LONG).show();
                   recreate();

               }else {

                   final String na = name.getEditText().getText().toString().trim();
                   final String   su = subject.getEditText().getText().toString().trim();
                   final String   em = email.getEditText().getText().toString().trim();


                   Intent intent = new Intent(Intent.ACTION_SEND);
                   intent.putExtra(Intent.EXTRA_TEXT,na);
                   intent.putExtra(Intent.EXTRA_SUBJECT, su);
                   intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"csuccessatulegwu@gmail.com"});


                   intent.setType("message/rfc822");
                   startActivity(Intent.createChooser(intent, "Share with"));


               }
            }
        });




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


    private boolean validateName() {
        String val =  name.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            name.setError("field can not be empty");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateSubject() {
        String val =  subject.getEditText().getText().toString().trim();
        String checkSpace = "\\A\\w{1,200}\\z";
        if (val.isEmpty()) {
            subject.setError("field can not be empty");
            return false;
        }  else {
            subject.setError(null);
            subject.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("field can not be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid email !");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
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