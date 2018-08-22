package com.aacreations.waterbillgeneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class UserProfile extends AppCompatActivity {

    EditText name,address,age,phone,email,password;

    EditText aadharET;
    String name_str="";
    String aadhar_str = "";
    String address_str = "";
    String age_str = "";
    String phone_str = "";
    String email_str = "";
    String password_str = "";

    ProgressDialog mProgress;
    String from = "";

    MediaPlayer click;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        //GetExtra

        Bundle extra = getIntent().getExtras();
        aadhar_str = extra.getString("aadhar");
        from = extra.getString("from");

        click = MediaPlayer.create(this,R.raw.click);

        //aadhar_str is extra value from getIntent.. So to visible aadhar_str, need to store in another variable

        String aadharId = aadhar_str;

        aadharET = (EditText)findViewById(R.id.user_aadhar);
        update = (Button)findViewById(R.id.update);
        name = (EditText)findViewById(R.id.user_name);
        address = (EditText)findViewById(R.id.user_address);
        age = (EditText)findViewById(R.id.user_age);
        phone = (EditText)findViewById(R.id.user_phone);
        email = (EditText)findViewById(R.id.user_email);
        password = (EditText)findViewById(R.id.user_password);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setCancelable(false);
        mProgress.show();
        aadharET.setText(aadharId);
        aadharET.setEnabled(false);
        Firebase mRefName = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/name");
        Firebase mRefAddress = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/address");


        if(from.equals("admin"))
        {
            update.setVisibility(View.INVISIBLE);
            password.setVisibility(View.INVISIBLE);
        }

        mRefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name_str = dataSnapshot.getValue(String.class);
                name.setText(name_str);
                name.setEnabled(false);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Firebase mRefAge = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/age");
        Firebase mRefPhone = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/phone");
        Firebase mRefEmail = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/email");
        Firebase mRefPassword = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/password");

        mRefAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                address_str = dataSnapshot.getValue(String.class);
                address.setText(address_str);
                if(from.equals("admin"))
                {
                    address.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRefAge.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                age_str = dataSnapshot.getValue(String.class);
                age.setText(age_str);

                if(from.equals("admin"))
                {
                    age.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                email_str = dataSnapshot.getValue(String.class);
                email.setText(email_str);
                if(from.equals("admin"))
                {
                    email.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRefPassword.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                password_str = dataSnapshot.getValue(String.class);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRefPhone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                phone_str = dataSnapshot.getValue(String.class);
                phone.setText(phone_str);
                mProgress.dismiss();
                if(from.equals("admin"))
                {
                    phone.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                mProgress = new ProgressDialog(v.getContext());
                mProgress.setMessage("Please wait...");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.setCancelable(false);
                mProgress.show();
                String entered_password = password.getText().toString();
                if(entered_password.equals(""))
                {
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"Please enter the password",Toast.LENGTH_SHORT).show();
                }
                else if(password_str.equals(entered_password))
                {
                    Firebase mRefStorageAadhar = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str);
                    mRefStorageAadhar.child("age").setValue(age.getText().toString());
                    mRefStorageAadhar.child("address").setValue(address.getText().toString());
                    mRefStorageAadhar.child("phone").setValue(phone.getText().toString());
                    mRefStorageAadhar.child("email").setValue(email.getText().toString());
                    Toast.makeText(getApplicationContext(),"Updated Successfully..)",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),UserView.class);
                    mProgress.dismiss();
                    i.putExtra("aadhar",aadhar_str);
                    startActivity(i);
                    finish();
                }
                else
                {
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"Invalid password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(from.equals("user")) {
            Intent i = new Intent(getApplicationContext(), UserView.class);
            i.putExtra("aadhar", aadhar_str);
            startActivity(i);
            finish();
        }
        else if(from.equals("admin"))
        {
            Intent i = new Intent(getApplicationContext(), AdminProfile.class);
            startActivity(i);
            finish();
        }

    }

}
