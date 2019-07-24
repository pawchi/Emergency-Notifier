package org.chilon.emergencynotifier;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button smsButton;
    Button callButton;
    Button emailButton;
    MenuItem item;
    public static final String MAIN_RESPONSE = "method_of_sending";
    ArrayList<ObjectToSend> listOfWaitedMessages;
    int numberOfMessages = 0;
    NotificationBadge nBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        smsButton = (Button) findViewById(R.id.smsButton);
        callButton = (Button) findViewById(R.id.callButton);
        emailButton = (Button) findViewById(R.id.emailButton);

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNotification.class);
                intent.putExtra(MAIN_RESPONSE, 1);
                startActivity(intent);
            }
        });

        /*
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNotification.class);
                startActivity(intent);
            }
        });*/
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNotification.class);
                intent.putExtra(MAIN_RESPONSE, 2);
                startActivity(intent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNotification.class);
                intent.putExtra(MAIN_RESPONSE, 3);
                startActivity(intent);
            }
        });

        /*
        if (listOfWaitedMessages != null){
            int arrayLength = listOfWaitedMessages.size();
            String lengthText = "Number of waited messages: " + arrayLength;
            Toast.makeText(this, lengthText, Toast.LENGTH_LONG).show();
        }*/

        //Show number in sms_badge
        //numberOfMessages = getIntent().getIntExtra(CreateNotification.UPDATED_NUMBER_OF_MESSAGES,0);

        SharedPreferences sharedPreferences = getSharedPreferences("smsList", Activity.MODE_PRIVATE);
        numberOfMessages = sharedPreferences.getInt("numberOfSms", 0);
        nBadge = (NotificationBadge) findViewById(R.id.badge);
        nBadge.setNumber(numberOfMessages);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
