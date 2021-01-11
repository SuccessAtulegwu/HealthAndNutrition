package com.example.healthandnutrition;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.healthandnutrition.listemers.ItemClickListener;
import com.example.healthandnutrition.model.Model;
import com.example.healthandnutrition.viewModel.ViewModel;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseRecyclerOptions<Model> options;
    FirebaseRecyclerAdapter<Model, ViewModel> adapter;
    ProgressDialog dialog;
    SwipeRefreshLayout swipeRefreshLayout;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView navigationView;
    TextView txtBadge;
    private InterstitialAd mInterstitialAd;
    final static float END_SCALE = 0.7f;
    LinearLayout content;



    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.home_recycler);
        //hooks
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        content = findViewById(R.id.linear);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        drawerLoad();


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

        AdView adView = findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());


        //recyclerview hooks
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Dialog mDialog   = new Dialog(this);
            mDialog.setContentView(R.layout.dialog);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            mDialog.getWindow().getAttributes().windowAnimations =
                    android.R.style.Animation_Dialog;
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button btn = mDialog.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });
            try {
                mDialog.show();
            }catch (Exception m){
                mDialog.dismiss();
            }


            /*

             AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert
                            .setCancelable(false)
                            .setTitle("Application")
                            .setMessage("Please check your internet connection")
                            .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity( new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(MainActivity.this,Internet.class));
                                }
                            });
                    alert.show();

            */

        } else {

            dialog = new ProgressDialog(MainActivity.this);
            dialog = new ProgressDialog(this, R.style.dialog);
            dialog.setMessage("Loading....");
            dialog.setTitle("Application");
            dialog.setCancelable(false);
            dialog.show();

            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
            swipeRefreshLayout.setColorSchemeResources(R.color.halo,
                    R.color.fux,
                    R.color.colorPrimary,
                    R.color.grey_dialog_box);

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadCat();

                }
            });
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    loadCat();
                }
            });

        }


        //load swipe refresh layout


        //load progress dialog


        //load firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        category.keepSynced(true);


        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(2)
                .setDebug(false)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);


    }

    private void drawerLoad() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaleSlide = slideOffset *(1-END_SCALE);
                final float offSetScale = 1-diffScaleSlide;
                content.setScaleX(offSetScale);
                content.setScaleY(offSetScale);
                final float xOffSet = drawerView.getWidth() * slideOffset;
                final float xOffSetDiff = content.getWidth() * diffScaleSlide /2;
                final float xTranslate = xOffSet-xOffSetDiff;
                content.setTranslationX(xTranslate);
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


    private void loadCat() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);

        options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(category, Model.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewModel>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewModel holder, int position, @NonNull Model model) {

                holder.catName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.catImage);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), Menu.class);
                        intent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        //intent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catlist, parent, false);
                return new ViewModel(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);


    }

    @Override
    protected void onStart() {
        if (adapter != null) {
            adapter.startListening();
        }
        super.onStart();

    }

    @Override
    public void onBackPressed() {
       dialog.dismiss();
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {

               loadAds();
            AlertDialog.Builder nDialog  = new AlertDialog.Builder(this, R.style.alertDialog);
            nDialog.setTitle("Application");
            nDialog.setIcon(R.drawable.exit);
            nDialog.setMessage("Are you sure you want to exit?");
            nDialog.setCancelable(false);
            nDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (dialog!=null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    finish();
                }
            });
            nDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (dialog!=null && dialog.isShowing()){
                        dialog.cancel();
                    }
                    Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog kAlert = nDialog.create();
                kAlert.show();


            // super.onBackPressed();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog!=null && dialog.isShowing()){
            dialog.cancel();
        }

    }

    private void loadAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();

    }


    @Override
    protected void onResume() {
        if (adapter != null) {
            adapter.startListening();
        }
        super.onResume();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object

        switch (item.getItemId()) {
            case R.id.nav_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(MainActivity.this,"Home",Toast.LENGTH_SHORT).show();
                break;

            case R.id.tips:
                startActivity(new Intent(getApplicationContext(),Tips.class));
                Toast.makeText(MainActivity.this,"Health Tips",Toast.LENGTH_SHORT).show();
                break;

            case R.id.cal:
                Toast.makeText(MainActivity.this,"Coming soon..",Toast.LENGTH_LONG).show();
                break;

            case R.id.about:
                Intent about = new Intent(getApplicationContext(), About.class);
                startActivity(about);
                break;

            case R.id.feed:
                Intent fed = new Intent(getApplicationContext(), Feedback.class);
                startActivity(fed);
                break;

            case R.id.rate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;

            case R.id.share:
                try {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT, "Health And Nutrition");
                    String shareMessage = "https://play.google.com/store/apps/details=" + BuildConfig.APPLICATION_ID + "\n\n";
                    share.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(share, "Share by"));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.more:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                break;
            case R.id.ads:
                Toast.makeText(MainActivity.this, "ads clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem menuItem = (MenuItem) menu.findItem(R.id.ads);
        View view = menuItem.getActionView();
        txtBadge = (TextView) view.findViewById(R.id.txt_badge);


        setUpBadge();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"No Ads", Toast.LENGTH_SHORT).show();
                mInterstitialAd.show();


            }
        });
        return true;
    }

    private void setUpBadge() {

  /*      if (txtBadge != null){
            if (badgeCount == 0){
                if (txtBadge.getVisibility() != View.GONE){
                    txtBadge.setVisibility(View.GONE);
                }
            }else {

            }
        }

   */
    }
}