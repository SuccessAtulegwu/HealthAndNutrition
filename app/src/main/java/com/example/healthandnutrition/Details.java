package com.example.healthandnutrition;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthandnutrition.listemers.ItemClickListener;
import com.example.healthandnutrition.model.MenuModel;
import com.example.healthandnutrition.viewModel.SubViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {
    FirebaseRecyclerAdapter<MenuModel, SubViewHolder> adapter;
    FirebaseDatabase mData;
    DatabaseReference refMenu;
    ImageView imgHeader,imgBack;
    TextView about, why, benefits,nutrients;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String menuId = "";
    RecyclerView subRecycler;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //load Admob ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8027186520767837/8417445875");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        adListener();
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-8027186520767837/5699250365")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    private ColorDrawable background;

                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(background).build();

                        TemplateView template = findViewById(R.id.my_template);
                        TemplateView templateView = findViewById(R.id.my_template2);
                        templateView.setStyles(styles);
                        templateView.setNativeAd(unifiedNativeAd);
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

        //hooks
        collapsingToolbarLayout = findViewById(R.id.details_collapsing);
        imgHeader = findViewById(R.id.header_image);
        imgBack = findViewById(R.id.back);
        about = findViewById(R.id.txtAbout);
        why = findViewById(R.id.txtWhy);
        benefits = findViewById(R.id.txtBenefits);
        nutrients = findViewById(R.id.txtNutrients);
        subRecycler = findViewById(R.id.sub_recycler);

        //load image back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Details.super.onBackPressed();


            }
        });

        //set collapsing toolbar
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedAppbar);

      //database
        mData = FirebaseDatabase.getInstance();
        refMenu = mData.getReference("Menu");

        if (getIntent() != null){
            menuId = getIntent().getStringExtra("MenuId");
        }
        if (!menuId.isEmpty()){
            loadDetails(menuId);
        }

        loadSub();
    }

    private void adListener() {
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                adDelay();
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

    private void adDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd == null || !mInterstitialAd.isLoaded() ){
                    recreate();
                }else {
                    mInterstitialAd.show();
                }
            }
        },7000*60);
    }

    private void loadSub() {

        subRecycler.setHasFixedSize(true);
        LinearLayoutManager linear = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        subRecycler.setLayoutManager(linear);
        FirebaseRecyclerOptions<MenuModel> options = new FirebaseRecyclerOptions.Builder<MenuModel>()
                .setQuery(refMenu,MenuModel.class)
                .build();



        adapter = new FirebaseRecyclerAdapter<MenuModel, SubViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SubViewHolder holder, int position, @NonNull MenuModel model) {
                Picasso.get().load(model.getImage()).into(holder.subImage);
                holder.subName.setText(model.getName());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent detail = new Intent(getApplicationContext(), Details.class);
                        detail.putExtra("MenuId",adapter.getRef(position).getKey());
                        startActivity(detail);
                        finish();
                    }
                });

            }

            @NonNull
            @Override
            public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_item,parent,false);
                return new SubViewHolder(view);
            }
        };
        adapter.startListening();
        subRecycler.setAdapter(adapter);
    }

    private void loadDetails(String menuId) {
        refMenu.child(menuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuModel model = snapshot.getValue(MenuModel.class);
                Picasso.get().load(model.getImage()).into(imgHeader);
                collapsingToolbarLayout.setTitle(model.getName());
                why.setText(model.getWhy());
                benefits.setText(model.getBenefits());
                nutrients.setText(model.getNutrients());
                about.setText(model.getAbout());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        loadAds();
    }

    private void loadAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
        }
    }
}