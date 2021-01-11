package com.example.healthandnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthandnutrition.model.ModelPager;
import com.example.healthandnutrition.viewModel.TipsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Tips extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference tips;
    FirebaseRecyclerOptions<ModelPager> options;
    FirebaseRecyclerAdapter<ModelPager, TipsViewHolder> adapter;

    RecyclerView recyclerView;
    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        Toolbar toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.tips_recycler);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8027186520767837/8417445875");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        adListener();
        AdView adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());

     //   recyclerView.setHasFixedSize(true);
        LinearLayoutManager linear = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linear);

        database = FirebaseDatabase.getInstance();
        tips = database.getReference("Tips");
        tips.keepSynced(true);

        loadTips();

            }

    private void loadTips() {
        options = new FirebaseRecyclerOptions.Builder<ModelPager>()
                .setQuery(tips, ModelPager.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ModelPager, TipsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TipsViewHolder holder, int position, @NonNull ModelPager model) {
                holder.name.setText(model.getName());
                holder.desc.setText(model.getDesc());
                Picasso.get().load(model.getImage()).into(holder.image);

            }

            @NonNull
            @Override
            public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_items,parent,false);
               return new TipsViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    public  void share(View view){
        try {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, "Health And Nutrition");
            String shareMessage = "https://play.google.com/store/apps/details=" + BuildConfig.APPLICATION_ID + "\n\n";
            share.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(share, "Share by"));
        } catch (Exception e) {
            Toast.makeText(Tips.this, "Error Occurred",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        if (adapter != null) {
            adapter.startListening();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (adapter != null) {
            adapter.startListening();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
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

    @Override
    public void onBackPressed() {
        loadAds();
        super.onBackPressed();
    }

    private void loadAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}