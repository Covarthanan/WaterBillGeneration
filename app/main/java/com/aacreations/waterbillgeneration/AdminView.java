package com.aacreations.waterbillgeneration;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminView extends AppCompatActivity {


    MediaPlayer click;
    ImageView adminList,adminBill,adminLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        click = MediaPlayer.create(this,R.raw.click);
        adminList = (ImageView)findViewById(R.id.admin_list);
        adminBill = (ImageView)findViewById(R.id.admin_bill);
        adminLogout = (ImageView)findViewById(R.id.Admin_logout_btn);


        adminBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                Intent i = new Intent(getApplicationContext(),AdminBill.class);
                startActivity(i);
                finish();

            }
        });

        adminList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.start();
                Intent i = new Intent(getApplicationContext(),AdminProfile.class);
                startActivity(i);
                finish();


            }
        });

        adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }
        });
    }


    private Boolean exit = false;

    @Override
    public void onBackPressed() {

        if(exit)
        {
            finish();
        }
        else {

            Toast.makeText(this,"Press Back again to Exit",Toast.LENGTH_SHORT).show();
            exit = true;

            new CountDownTimer(2000, 1000) {
                public void onFinish() {

                    exit = false;
                }

                public void onTick(long s) {
                }
            }.start();
        }


    }

}
