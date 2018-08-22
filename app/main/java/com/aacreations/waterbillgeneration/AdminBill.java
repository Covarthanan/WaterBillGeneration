package com.aacreations.waterbillgeneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.renderscript.Script;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class AdminBill extends AppCompatActivity {

    MediaPlayer click;
    ProgressDialog mProgress;
    String aadhar_str = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bill);

        click = MediaPlayer.create(this,R.raw.click);
        final EditText aadhar = (EditText)findViewById(R.id.admin_aadhar_number);
        Button generate = (Button)findViewById(R.id.admin_generate_bill);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                mProgress = new ProgressDialog(v.getContext());
                mProgress.setMessage("Please wait...");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.setCancelable(false);
                mProgress.show();
                aadhar_str = aadhar.getText().toString();

                Firebase mRef = new Firebase("https://waterbillgeneration.firebaseio.com/aadhar/");

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,String>map = dataSnapshot.getValue(Map.class);

                        if(map.containsValue(aadhar_str))
                        {
                            Firebase mRef = new Firebase("https://waterbillgeneration.firebaseio.com/accounts/" + aadhar_str);

                            mRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String pass = dataSnapshot.getValue(String.class);
                                    if (pass.length()>5) {
                                        Intent i = new Intent(getApplicationContext(), BillGenerate.class);
                                        i.putExtra("aadhar", aadhar_str);
                                        mProgress.dismiss();
                                        startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        }
                        else {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Check your aadhar number...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), AdminView.class);
        startActivity(i);
        finish();

    }
}
