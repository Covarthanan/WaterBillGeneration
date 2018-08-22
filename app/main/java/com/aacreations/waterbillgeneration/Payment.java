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

import com.firebase.client.Firebase;

public class Payment extends AppCompatActivity {

    String aadhar = "";
    float amnt = 0.0f;
    EditText card_number,amount;
    Button pay;
    MediaPlayer click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle extra = getIntent().getExtras();
        aadhar = extra.getString("aadhar");
        amnt = extra.getFloat("totalcharge");


        click = MediaPlayer.create(this,R.raw.click);
        card_number = (EditText)findViewById(R.id.card_number);
        amount = (EditText)findViewById(R.id.amount);
        pay = (Button)findViewById(R.id.pay);

        float val = amnt;
        amount.setText(val+"");
        amount.setEnabled(false);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                String cardNum = card_number.getText().toString();
                if(cardNum.length()>3)
                {

                    Firebase mRefPaid = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar+"/paid");
                    mRefPaid.setValue("yes");
                    Toast.makeText(getApplicationContext(),"Payment successfully done..",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),UserView.class);
                    i.putExtra("aadhar",aadhar);
                    startActivity(i);
                    finish();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Card number must be greater than 3 digits...",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),UserBill.class);
        i.putExtra("aadhar",aadhar);
        startActivity(i);
        finish();
    }
}
