package org.chilon.emergencynotifier;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MenuItem item;
    public static final String MAIN_RESPONSE = "method_of_sending";
    ArrayList<ObjectToSend> listOfWaitedMessages;
    int numberOfMessages = 0;
    NotificationBadge nBadge;
    ListView listView;
    String[] itemColor = {"#9ad0f0", "#6f87c9", "#407b9f"};
    Integer[] images = {R.drawable.sms, android.R.drawable.sym_action_call, android.R.drawable.sym_action_email};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] actionNames = {getResources().getString(R.string.sms_button), getResources().getString(R.string.call_button), getResources().getString(R.string.email_button)};


        listView = (ListView) findViewById(R.id.listViewMain);
        CustomListView customListView = new CustomListView(this, actionNames, itemColor, images );
        listView.setAdapter(customListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CreateNotification.class);
                intent.putExtra(MAIN_RESPONSE, position);
                setResult(RESULT_OK, intent);
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
