package com.aacreations.waterbillgeneration;


import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Logo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        new CountDownTimer(5000, 1000) {
            public void onFinish() {


        Intent i = new Intent(getApplicationContext(),Login.class);
        startActivity(i);
                finish();


            }

            public void onTick(long s) {



            }
        }.start();

    }
}
