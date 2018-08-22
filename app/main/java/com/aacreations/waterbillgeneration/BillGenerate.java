package com.aacreations.waterbillgeneration;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.PaintDrawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class BillGenerate extends AppCompatActivity {


    float consumption_mail, maitenance_mail, total_mail;

    String bill_for_mail = "";
    String aadhar_str = "";
    String current_str = "";
    String maintenence_Str = "";
    String costperliter = "";
    String secret_code = "";
    Float pr,cu;

    MediaPlayer click;
    TextView name, aadhar, address, phone, previous;
    EditText current, costPerLiter, maintenance, secretcode;
    Button generate;
    ProgressDialog mProgress;
    String prev_val = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_generate);

        Bundle extra = getIntent().getExtras();
        aadhar_str = extra.getString("aadhar");


        click = MediaPlayer.create(this, R.raw.click);

        name = (TextView) findViewById(R.id.generate_bill_name);
        aadhar = (TextView) findViewById(R.id.generate_bill_aadhar);
        address = (TextView) findViewById(R.id.generate_bill_address);
        phone = (TextView) findViewById(R.id.generate_bill_phone);
        previous = (TextView) findViewById(R.id.generate_bill_previous);

        current = (EditText) findViewById(R.id.generate_bill_current);
        costPerLiter = (EditText) findViewById(R.id.generate_bill_cost_per_liter);
        maintenance = (EditText) findViewById(R.id.generate_bill_maintenance);
        secretcode = (EditText) findViewById(R.id.secret_code);

        generate = (Button) findViewById(R.id.generate_bill_button);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setCancelable(false);
        mProgress.show();

        final String aadharNumber = aadhar_str;
        aadhar.setText("Aadhar number     : " + aadharNumber);

        Firebase mRefName = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/name");
        final Firebase mRefAddress = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/address");
        Firebase mRefPhone = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/phone");
        Firebase mRefPrevious = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/current");

        mRefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name_str = dataSnapshot.getValue(String.class);
                name.setText("Name     : " + name_str);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRefAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String address_str = dataSnapshot.getValue(String.class);
                address.setText("Address     : " + address_str);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRefPhone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String phone_Str = dataSnapshot.getValue(String.class);
                phone.setText("Phone Number     : " + phone_Str);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRefPrevious.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String prev = dataSnapshot.getValue(String.class);
                previous.setText("Previous Reading     : " + prev);
                prev_val = prev;
                mProgress.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                mProgress = new ProgressDialog(v.getContext());
                mProgress.setMessage("Please wait...");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.setCancelable(false);
                mProgress.show();


                Firebase mRef = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/paid");

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String paid_check = dataSnapshot.getValue(String.class);

                        if (paid_check.equals("no")) {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Bill not paid.....", Toast.LENGTH_SHORT).show();
                        } else {

                            current_str = current.getText().toString();
                            boolean currentStrCheck = checkfloat(current_str);
                            maintenence_Str = maintenance.getText().toString();
                            boolean maintenenceStrCheck = checkfloat(maintenence_Str);
                            costperliter = costPerLiter.getText().toString();
                            boolean costperliterStrCheck = checkfloat(costperliter);
                            secret_code = secretcode.getText().toString();

                            if (currentStrCheck && maintenenceStrCheck && costperliterStrCheck) {

                                 cu = Float.parseFloat(current_str);

                                 pr = Float.parseFloat(prev_val);

                                consumption_mail = cu - pr;

                                if (cu > pr) {
                                    if (current_str.equals("") || maintenence_Str.equals("") || costperliter.equals("")) {
                                        mProgress.dismiss();
                                        Toast.makeText(getApplicationContext(), "Please enter all the details...", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (secret_code.equals("1234")) {

                                            Firebase mRefCurrent = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/current");
                                            Firebase mRefPrevious = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/previous");
                                            Firebase mRefMaintenance = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/maintenance");
                                            Firebase mRefCostPerLiter = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/costPerLiter");
                                            Firebase mRefPaid = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/paid");

                                            mRefPrevious.setValue(prev_val);
                                            mRefCurrent.setValue(current_str);
                                            mRefMaintenance.setValue(maintenence_Str);
                                            mRefCostPerLiter.setValue(costperliter);
                                            mRefPaid.setValue("no");

                                            Toast.makeText(getApplicationContext(), "Generated successfully...:)", Toast.LENGTH_SHORT).show();

                                            mProgress.dismiss();

                                            //for sending mail
                                            Firebase mRefemail_send_mail = new Firebase("https://waterbillgeneration.firebaseio.com/storage/" + aadhar_str + "/email");
                                            mRefemail_send_mail.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    String send_mail = dataSnapshot.getValue(String.class);

                                                    float prev = Float.parseFloat(prev_val);
                                                    float curt = Float.parseFloat(current_str);
                                                    float costper = Float.parseFloat(costperliter);
                                                    float maintenance = Float.parseFloat(maintenence_Str);

                                                    float total_mail = (consumption_mail * costper) + maintenance;


                                                    bill_for_mail = "------- Service Information-------\n" + name.getText().toString() + "\n" + aadhar.getText().toString()
                                                            + "\n" + address.getText().toString() + "\n" + phone.getText().toString() + "\n-------Meter Information-------" + "\nCurrentReading      :" + String.valueOf(cu) + " L" + "\nPrevious reading      :" +
                                                            String.valueOf(pr) + " L" + "\nConsumption      :" + String.valueOf(consumption_mail) + " L" + "\n-------Payment Information-------" + "\nMaintenenace       : RS." + String.valueOf(maintenence_Str) + " /-" + "\nCost pre Liter       : RS." + costPerLiter.getText().toString() + " /-"
                                                            + "\nTotal Charge      : RS." + String.valueOf(total_mail) + " /-" + "\nPaid          : no";
                                                    sendEmail(bill_for_mail,send_mail);
                                                }

                                                @Override
                                                public void onCancelled(FirebaseError firebaseError) {

                                                }
                                            });
                                            Intent i = new Intent(getApplicationContext(), AdminView.class);
                                            startActivity(i);
                                            finish();

                                        } else {
                                            mProgress.dismiss();
                                            Toast.makeText(getApplicationContext(), "Invalid secret code...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Check your current reading...", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mProgress.dismiss();
                                Toast.makeText(getApplicationContext(), "Check the reading you are entered......", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }
        });
    }

    //check the values which entered in correct form not as (alphaets,contain more dots)

    public boolean checkfloat(String str) {

        int length = str.length();
        int count_length = 0;
        int dot_check = 0;

        if (length == 0) {
            return false;
        } else {
            for (int i = 0; i < length; i++) {
                char c = str.charAt(i);
                if (c == '.' && i != 0) {
                    if (dot_check == 0) {
                        dot_check = 1;
                        count_length++;
                    }
                } else if (Character.isDigit(c)) {
                    count_length++;
                }
            }
            if (count_length == length)
                return true;
            else {

                return false;
            }
        }
    }

    //Automatic mail to the user from admin while clicking genert button

    public void sendEmail(String bill, String gmail) {
        Log.i("Send To......", "");

        String[] TO = {gmail};
        String[] CC = {"waterbillboard@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Waterbill for your Home for this month ");
        emailIntent.putExtra(Intent.EXTRA_TEXT, bill);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed()
        {
            click.start();
        Intent i = new Intent(getApplicationContext(),AdminBill.class);
        startActivity(i);
        finish();
    }

}