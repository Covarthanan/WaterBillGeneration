package com.aacreations.waterbillgeneration;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.annotation.FloatRange;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.collection.LLRBNode;
import com.firebase.client.core.view.filter.IndexedFilter;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class UserBill extends AppCompatActivity {


    Button payment;
    ImageView downloadtxt;
    TextView download_text;
    String stringForTextFile ="";



    TextView name,aadhar,address,phone,current_reading,previous_reading,consumption,total_bill,paid,maintenance,costPerLiter;

    Firebase mRefCostPerLiter;
    Firebase mRefPrevious;
    Firebase mRefMaintenance;
    String aadhar_str = "";
    String current_str = "";
    String previous_str = "";
    String maintenance_str = "";
    float tot_bal = 0.0f;
    String paid_check = "";

    MediaPlayer click;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bill);

        Bundle extra = getIntent().getExtras();
        aadhar_str = extra.getString("aadhar");

        String aadharId = aadhar_str;

        click = MediaPlayer.create(this,R.raw.click);
        download_text = (TextView)findViewById(R.id.download_text_view);
        download_text.setVisibility(View.INVISIBLE);
        downloadtxt = (ImageView)findViewById(R.id.download_txt);
        downloadtxt.setVisibility(View.INVISIBLE);

        name = (TextView)findViewById(R.id.user_bill_name);
        aadhar = (TextView)findViewById(R.id.user_bill_aadhar);
        address = (TextView)findViewById(R.id.user_bill_address);
        phone = (TextView)findViewById(R.id.user_bill_phone);
        current_reading = (TextView)findViewById(R.id.user_bill_current_reading);
        previous_reading = (TextView)findViewById(R.id.user_bill_previous_reading);
        consumption = (TextView)findViewById(R.id.user_bill_consumption);
        maintenance = (TextView)findViewById(R.id.user_bill_maintenance);
        total_bill = (TextView)findViewById(R.id.user_bill_total_charge);
        paid = (TextView)findViewById(R.id.user_bill_paid);
        costPerLiter = (TextView)findViewById(R.id.user_bill_cost_liter);


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setCancelable(false);
        mProgress.show();

        Firebase mRefName = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/name");
        Firebase mRefAddress = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/address");
        Firebase mRefPhone = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/phone");

        Firebase mRefCurrent = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/current");
        mRefPrevious = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/previous");
        Firebase mRefPaid = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/paid");
        mRefMaintenance = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/maintenance");
        mRefCostPerLiter = new Firebase("https://waterbillgeneration.firebaseio.com/storage/"+aadhar_str+"/costPerLiter");


        aadhar.setText("Aadhar Number     : "+aadharId);

        mRefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name_str = dataSnapshot.getValue(String.class);
                name.setText("Name      : "+name_str);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRefAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String address_str = dataSnapshot.getValue(String.class);
                address.setText("Address     :"+address_str);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRefPhone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String phone_str = dataSnapshot.getValue(String.class);
                phone.setText("Phone     :"+phone_str);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRefCurrent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                current_str = dataSnapshot.getValue(String.class);
                current_reading.setText("Current Reading    :"+current_str);
                mRefPrevious.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        previous_str = dataSnapshot.getValue(String.class);
                        previous_reading.setText("Previous Reading     : "+previous_str);

                        mRefMaintenance.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                maintenance_str = dataSnapshot.getValue(String.class);

                                mRefCostPerLiter.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        String liter = dataSnapshot.getValue(String.class);

                                        Float cur = Float.parseFloat(current_str);
                                        Float pre = Float.parseFloat(previous_str);
                                        Float main = Float.parseFloat(maintenance_str);
                                        Float lit = Float.parseFloat(liter);
                                        Float consumpt = cur - pre;
                                        Float tot = (consumpt * lit) + main;
                                        tot_bal = tot;

                                        consumption.setText("Consumption     : "+consumpt+" liters");
                                        maintenance.setText("Maintenance Charge     : " +main);
                                        total_bill.setText("Total Charge     : Rs."+tot+"/-");
                                        costPerLiter.setText("Cost Per Liter     : Rs."+lit+"/-");

                                        //After loading the data
                                        downloadtxt.setVisibility(View.VISIBLE);
                                        download_text.setVisibility(View.VISIBLE);




                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRefPaid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mProgress.dismiss();
                String paid_str = dataSnapshot.getValue(String.class);
                paid_check = paid_str;
                paid.setText("Paid     : "+paid_str);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        payment = (Button)findViewById(R.id.payment);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();

                if(paid_check.equals("no")) {
                    Intent i = new Intent(getApplicationContext(), Payment.class);
                    i.putExtra("aadhar", aadhar_str);
                    i.putExtra("totalcharge",tot_bal);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Already paid..",Toast.LENGTH_SHORT).show();
                }
            }
        });


        downloadtxt.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                click.start();
                downloadtxt.setVisibility(View.INVISIBLE);
                download_text.setVisibility(View.INVISIBLE);

                //marshmallow permission
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission();
                }


                stringForTextFile = "\t\tWater Bill Generation\n\n\t------- Service Information-------\n"+name.getText().toString()+"\n"+aadhar.getText().toString()
                        +"\n"+address.getText().toString()+"\n"+phone.getText().toString()+"\n\t-------Meter Information-------"+"\n"+current_reading.getText().toString()+"\n"+
                        previous_reading.getText().toString()+"\n"+consumption.getText().toString()+"\n\t-------Payment Information-------"+"\n"+maintenance.getText().toString()+"\n"+costPerLiter.getText().toString()
                        +"\n"+total_bill.getText().toString()+"\n"+paid.getText().toString();


                try {
                        File root = new File(Environment.getExternalStorageDirectory(), "Waterbill");
                        if (!root.exists()) {
                            root.mkdirs();
                        }


                        File gpxfile = new File(root, aadhar_str+"-Waterbill.pdf");
/*                       FileOutputStream fileOutputStream = new FileOutputStream(gpxfile);
                        fileOutputStream.write(stringForTextFile.getBytes(),1,stringForTextFile.length());
                        PdfDocument writer = new PdfDocument();
                        writer.writeTo(fileOutputStream);
                        writer.close();
                        Toast.makeText(getApplicationContext(),""+root, Toast.LENGTH_SHORT).show();
*/
                    FileOutputStream fOut = new FileOutputStream(gpxfile);
                    PdfDocument document= new PdfDocument();
                    PdfDocument.PageInfo pageInfo = new
                            PdfDocument.PageInfo.Builder(300, 335, 1).create();
                    PdfDocument.Page page = document.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    Paint paint1 =new Paint();
                    paint1.setColor(Color.WHITE);
                    paint.setColor(Color.RED);
                    canvas.drawPaint(paint);
                    canvas.drawPaint(paint1);

                    canvas.drawText("Water Bill Generation", 100, 30, paint);
                    canvas.drawText("-------Service Information-------",60,50,paint);
                    canvas.drawText(name.getText().toString(),40,70,paint);
                    canvas.drawText(aadhar.getText().toString(),40,90,paint);
                    canvas.drawText(address.getText().toString(),40,110,paint);
                    canvas.drawText(phone.getText().toString(),40,130,paint);
                    canvas.drawText("-------Reading Information-------",60,160,paint);
                    canvas.drawText(previous_reading.getText().toString(),40,180,paint);
                    canvas.drawText(current_reading.getText().toString(),40,200,paint);
                    canvas.drawText(consumption.getText().toString(),40,220,paint);
                    canvas.drawText("-------Paymemt Information-------",60,240,paint);
                    canvas.drawText(maintenance.getText().toString(),40,260,paint);
                    canvas.drawText(costPerLiter.getText().toString(),40,280,paint);
                    canvas.drawText(total_bill.getText().toString(),40,300,paint);
                    canvas.drawText(paid.getText().toString(),40,320,paint);



                    document.finishPage(page);
                    document.writeTo(fOut);
                    document.close();
                    Toast.makeText(getApplicationContext(),"File stored in "+root, Toast.LENGTH_SHORT).show();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),UserView.class);
        i.putExtra("aadhar",aadhar_str);
        startActivity(i);
        finish();
    }

    //marshmalloW permission

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
        }
    }
}
