package com.aacreations.waterbillgeneration;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by kumar on 05-12-2017.
 */
public class WaterBillGeneration extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();

        Firebase.setAndroidContext(this);

    }

}

