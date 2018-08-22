package com.aacreations.waterbillgeneration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class SignUp extends AppCompatActivity {

    String name_str = "";
    String aadhar_str = "";
    String address_str = "";
    String age_str = "";
    String phone_str = "";
    String email_str = "";
    String password_str = "";
    String repassword_str = "";
    String gender_str = "";

    MediaPlayer click;
    Button signup;

    ProgressDialog mProgress;

    EditText name,aadhar,address,age,phone,email,password,repassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        click = MediaPlayer.create(this,R.raw.click);
        signup = (Button)findViewById(R.id.sign_up);

        name = (EditText)findViewById(R.id.login_name);
        aadhar = (EditText)findViewById(R.id.login_aadhar);
        address = (EditText)findViewById(R.id.login_address);
        age = (EditText)findViewById(R.id.login_age);
        phone = (EditText)findViewById(R.id.login_phone);
        email = (EditText)findViewById(R.id.login_email);
        password = (EditText)findViewById(R.id.login_password);
        repassword = (EditText)findViewById(R.id.login_re_password);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                mProgress = new ProgressDialog(v.getContext());
                mProgress.setMessage("Please wait...");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.setCancelable(false);
                mProgress.show();
                name_str = name.getText().toString();
                aadhar_str = aadhar.getText().toString();
                address_str = address.getText().toString();
                age_str = age.getText().toString();
                phone_str = phone.getText().toString();
                email_str = email.getText().toString();
                password_str = password.getText().toString();
                repassword_str = repassword.getText().toString();

                if(aadhar_str.length()==12) {
                    if (password.length() > 5) {

                        if (name_str.equals("") || aadhar_str.equals("") || address_str.equals("") || age_str.equals("") || phone_str.equals("") || email_str.equals("") || password_str.equals("") || repassword_str.equals("") || gender_str.equals("")) {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Please enter all details", Toast.LENGTH_SHORT).show();
                        } else {
                            if (password_str.equals(repassword_str)) {
                                if (email_str.endsWith("@gmail.com")) {
                                    if(phone_str.length()==10) {


                                        Firebase mRefAadhar = new Firebase("https://waterbillgeneration.firebaseio.com/aadhar");
                                        mRefAadhar.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                Map<String,String> map = dataSnapshot.getValue(Map.class);

                                                if(map.containsValue(aadhar_str))
                                                {
                                                    mProgress.dismiss();
                                                    Toast.makeText(getApplicationContext(),"You are already registered..",Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Firebase mRef = new Firebase("https://waterbillgeneration.firebaseio.com/");
                                                    Firebase mRefAccounts = mRef.child("accounts");
                                                    mRefAccounts.child(aadhar_str).setValue(password_str);
                                                    Firebase mRefStorage = mRef.child("storage");
                                                    Firebase mRefUserFolder = mRefStorage.child(aadhar_str);
                                                    mRefUserFolder.child("aadhar").setValue(aadhar_str);
                                                    mRefUserFolder.child("name").setValue(name_str);
                                                    mRefUserFolder.child("address").setValue(address_str);
                                                    mRefUserFolder.child("age").setValue(age_str);
                                                    mRefUserFolder.child("phone").setValue(phone_str);
                                                    mRefUserFolder.child("email").setValue(email_str);
                                                    mRefUserFolder.child("gender").setValue(gender_str);
                                                    mRefUserFolder.child("current").setValue("0.0");
                                                    mRefUserFolder.child("previous").setValue("0.0");
                                                    mRefUserFolder.child("paid").setValue("yes");
                                                    mRefUserFolder.child("password").setValue(password_str);
                                                    mRefUserFolder.child("maintenance").setValue("0.0");
                                                    mRefUserFolder.child("costPerLiter").setValue("0.0");
                                                    //code for checking aadhar is alreaddy registered..
                                                    mRef.child("aadhar").push().setValue(aadhar_str);
                                                    mProgress.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Registerd successfully", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(getApplicationContext(), UserView.class);
                                                    i.putExtra("aadhar", aadhar_str);
                                                    startActivity(i);
                                                    finish();
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
                                        Toast.makeText(getApplicationContext(), "Phone number must contains 10 digits", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(), "email must contains @gmail.com", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mProgress.dismiss();
                                Toast.makeText(getApplicationContext(), "Passwords not same", Toast.LENGTH_SHORT).show();
                                password.setText("");
                                repassword.setText("");
                            }
                        }
                    } else {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(), "Password must be more than 5 letters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    mProgress.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid aadhar number.. (must be 12 digits)", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    gender_str = "male";
                    break;
            case R.id.radio_female:
                if (checked)
                    gender_str = "female";
                    break;
        }
    }
}
