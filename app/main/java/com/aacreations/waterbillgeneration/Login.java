package com.aacreations.waterbillgeneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button login;
    TextView newReg;
    EditText aadhar,password;
    String aadhar_str="";
    String password_str="";
    ProgressDialog mProgress;

    MediaPlayer click;

    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        click = MediaPlayer.create(this,R.raw.click);
        aadhar = (EditText)findViewById(R.id.aadhar);
        password = (EditText)findViewById(R.id.password);

        login = (Button)findViewById(R.id.login);
        newReg = (TextView)findViewById(R.id.new_registration);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                click.start();

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                {
                    connected = true;
                }
                else
                {
                    connected = false;
                }

                if(connected)
                {


                    mProgress = new ProgressDialog(v.getContext());
                    mProgress.setMessage("Please wait...");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.setCancelable(false);
                    mProgress.show();

                    aadhar_str = aadhar.getText().toString();
                    password_str = password.getText().toString();

                    if(aadhar_str.equals("admin"))
                    {

                        if(password_str.equals("1234"))
                        {
                            Toast.makeText(getApplicationContext(), "Welcome admin....:)", Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            Intent i = new Intent(getApplicationContext(), AdminView.class);
                            i.putExtra("from",aadhar_str);
                            startActivity(i);
                            finish();

                        }
                        else
                        {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Invalid secret code", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {



                        Firebase mRef = new Firebase("https://waterbillgeneration.firebaseio.com/aadhar/");

                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Map<String,String>map = dataSnapshot.getValue(Map.class);

                                if (map.containsValue(aadhar_str))
                                {

                                    Firebase mRef = new Firebase("https://waterbillgeneration.firebaseio.com/accounts/"+aadhar_str);

                                    mRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            String pass = dataSnapshot.getValue(String.class);
                                            if (pass.equals(password_str))
                                            {
                                                Toast.makeText(getApplicationContext(),"Welcome......:)",Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(),UserView.class);
                                                i.putExtra("aadhar",aadhar_str);

                                                mProgress.dismiss();
                                                startActivity(i);
                                                finish();
                                            }
                                            else
                                            {
                                                mProgress.dismiss();
                                                Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });

                                }
                                else
                                {
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(),"Check your Aadhar number...",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }


                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please check your internet connection..",Toast.LENGTH_SHORT).show();
                }



            }
        });

        newReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                Intent i = new Intent(getApplicationContext(),SignUp.class);
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
