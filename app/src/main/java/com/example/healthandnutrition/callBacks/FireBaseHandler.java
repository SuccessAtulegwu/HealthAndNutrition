package com.example.healthandnutrition.callBacks;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FireBaseHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
