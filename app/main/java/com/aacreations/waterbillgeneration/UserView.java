package com.aacreations.waterbillgeneration;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class UserView extends AppCompatActivity {


    String welcome_str = "";
    String aadhar = "";
    TextView welcomeText;
    ImageView profileTab;
    ImageView billTab,userLogout;
    MediaPlayer click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        welcomeText = (TextView)findViewById(R.id.user_welcome_text);
        userLogout = (ImageView)findViewById(R.id.user_logout_btn);
        profileTab = (ImageView)findViewById(R.id.user_profile_tab);
        billTab = (ImageView)findViewById(R.id.user_bill_tab);
        Bundle extra = getIntent().getExtras();
        aadhar = extra.getString("aadhar");

        click = MediaPlayer.create(this,R.raw.click);
        if(aadhar.equals(""))
        {

        }
        else {
            Firebase mRef = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar+"/name");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.getValue(String.class);
                    welcome_str = "Welcome "+name+" ....:)";
                    welcomeText.setText(welcome_str);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }


        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
            }
        });


        profileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                Intent i = new Intent(getApplicationContext(),UserProfile.class);
                i.putExtra("aadhar",aadhar);
                i.putExtra("from","user");
                startActivity(i);
                finish();

            }
        });

        billTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                Intent i = new Intent(getApplicationContext(),UserBill.class);
                i.putExtra("aadhar",aadhar);
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
