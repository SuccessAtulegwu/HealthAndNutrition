package com.example.healthandnutrition;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthandnutrition.listemers.ItemClickListener;
import com.example.healthandnutrition.model.MenuModel;
import com.example.healthandnutrition.model.Model;
import com.example.healthandnutrition.viewModel.MenuViewModel;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Menu extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference menu, cat;
    FirebaseRecyclerAdapter<MenuModel, MenuViewModel> mAdapter;
    Query query;
    private InterstitialAd mInterstitialAd;


    ImageView image,imageBack;
    // Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String categoryId = "";
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        recyclerView = (RecyclerView) findViewById(R.id.menu_recycler);

        //call hooks here
        image = findViewById(R.id.header_image);
        toolbar = findViewById(R.id.toolbar);
        imageBack = findViewById(R.id.back);

        //admob ads
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
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

        //load collapsing toolbar
        collapsingToolbarLayout = findViewById(R.id.menu_collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedAppbar);

        //load swipe refresh layout

        //load progress dialog
        dialog = new ProgressDialog(this,R.style.dialog);
        dialog.setMessage("Loading...");
        dialog.setTitle("Application");
        dialog.setCancelable(false);
        dialog.show();

        //load Firebase
        database = FirebaseDatabase.getInstance();
        menu = database.getReference("Menu");
        menu.keepSynced(true);
        cat = database.getReference("Category");
        cat.keepSynced(true);



      //  recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);



        //load hooks
    imageBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Menu.super.onBackPressed();
        }
    });

          // recyclerView.setHasFixedSize(true);


        //call to firebase
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");

        if (categoryId != null) {
            loadMenu(categoryId);
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



    private void loadMenu(String categoryId) {
        if (categoryId != null) {
            cat.child(categoryId).addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Model model = snapshot.getValue(Model.class);
                    Picasso.get().load(model.getImage())
                            .into(image);
                    collapsingToolbarLayout.setTitle(model.getName());
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle(model.getName());


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        dialog.show();
        query = menu.orderByChild("menuId").equalTo(categoryId);

        FirebaseRecyclerOptions<MenuModel> options = new FirebaseRecyclerOptions.Builder<MenuModel>()
                .setQuery(query, MenuModel.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<MenuModel, MenuViewModel>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewModel holder, int position, @NonNull MenuModel model) {
                holder.menuName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.menuImage);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent detail = new Intent(getApplicationContext(), Details.class);
                        detail.putExtra("MenuId",mAdapter.getRef(position).getKey());
                        startActivity(detail);

                    }
                });
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();
                    }
                };
                Handler handler = new Handler();
                handler.postDelayed(runnable,4000);

            }

            @NonNull
            @Override
            public MenuViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                return new MenuViewModel(view);

            }

        };
        mAdapter.startListening();
        recyclerView.setAdapter(mAdapter);
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount());


    }

    @Override
    protected void onStart() {
        if (mAdapter != null){
            mAdapter.startListening();
        }
        super.onStart();

    }

    @Override
    protected void onResume() {
        if (mAdapter != null){
            mAdapter.startListening();
        }
        super.onResume();

    }

    @Override
    protected void onStop() {
        if (mAdapter != null){
            mAdapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        dialog.dismiss();
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

    /* void load(){
        mAdapter = new FirebaseRecyclerAdapter<MenuModel, MenuViewModel>(MenuModel.class, R.layout.menu_item, MenuViewModel.class,
                menu.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(MenuViewModel menuViewModel, MenuModel menuModel, int i) {

                menuViewModel.menuName.setText(menuModel.getName());
                Picasso.get().load(menuModel.getImage())
                        .into(menuViewModel.menuImage);


                menuViewModel.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(Menu.this, "clicked " + position, Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        recyclerView.setAdapter(mAdapter);
    }
*/
}