package org.chilon.emergencynotifier;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.notificationbadge.NotificationBadge;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MenuItem item;
    public static final String MAIN_RESPONSE = "method_of_sending";
    int numberOfMessages = 0;
    ListView listView;
    String smsNo = "7";
    String callsNo = "0";
    String emailsNo = "0";
    String[] itemColor = {smsNo, callsNo, emailsNo};
    Integer[] images = {R.drawable.sms, android.R.drawable.sym_action_call, android.R.drawable.sym_action_email};
    TextView showNoOfMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] actionNames = {getResources().getString(R.string.sms_button), getResources().getString(R.string.call_button), getResources().getString(R.string.email_button)};

        smsNo = "9";
        itemColor = new String[] {smsNo, callsNo, emailsNo};

        listView = (ListView) findViewById(R.id.listViewMain);
        CustomListView customListView = new CustomListView(this, actionNames, itemColor, images );
        listView.setAdapter(customListView);

        //Adjust items height according screen height
        /*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LinearLayout layoutToSet = (LinearLayout) findViewById(R.id.ll_to_set_height);
        ViewGroup.LayoutParams params = layoutToSet.getLayoutParams();
        params.height = (metrics.heightPixels)/3;
        */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CreateNotification.class);
                intent.putExtra(MAIN_RESPONSE, position);
                setResult(RESULT_OK, intent);
                startActivity(intent);
            }
        });

        try {
            //Get Parcelable data from Activity CreateNotification
            Intent intent = getIntent();
            ObjectToSend objectToSend = intent.getParcelableExtra("sms_content");
            String smsContent = objectToSend.getSmsContent();
            Toast.makeText(MainActivity.this, smsContent, Toast.LENGTH_LONG).show();

            //Add sent content to DataBase
            DatabaseHelper db = new DatabaseHelper(this);
            boolean success = db.addData(1, smsContent);
            if (success) {
                Toast.makeText(MainActivity.this, "Data saved in DataBase!! ",  Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Not saved in DB :( ", Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException e) {}


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
