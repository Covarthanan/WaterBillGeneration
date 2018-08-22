package com.aacreations.waterbillgeneration;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class AdminProfile extends AppCompatActivity {


    ImageView backbtn;
    ListView userListView;
    Firebase mRefAadhar;
    ArrayList<String> userArrayList = new ArrayList<>();
    MediaPlayer click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        backbtn = (ImageView)findViewById(R.id.Admin_profile_backbtn);
        backbtn.setVisibility(View.INVISIBLE);
        click = MediaPlayer.create(this, R.raw.click);
        userListView = (ListView) findViewById(R.id.user_list);
        mRefAadhar = new Firebase("https://waterbillgeneration.firebaseio.com/aadhar");


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userArrayList);
        userListView.setAdapter(arrayAdapter);

        mRefAadhar.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String aadhar_str = dataSnapshot.getValue(String.class);
                userArrayList.add(aadhar_str);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click.start();
                Intent i =new Intent(getApplicationContext(),AdminView.class);
                startActivity(i);
                finish();
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                click.start();
                String aadhar_strr = (String) userListView.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), UserProfile.class);
                i.putExtra("aadhar", aadhar_strr);
                i.putExtra("from", "admin");
                startActivity(i);
                finish();

            }
        });
    }


    @Override
    public void onBackPressed() {

        backbtn.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"Press the back button.....",Toast.LENGTH_SHORT).show();
    }

}

