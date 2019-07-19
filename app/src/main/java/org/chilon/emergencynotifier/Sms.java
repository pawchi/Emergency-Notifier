package org.chilon.emergencynotifier;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Sms extends AppCompatActivity {
    /*
    final  int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    EditText number;
    EditText message;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_sms);

        number = findViewById(R.id.input_phone_number_field);
        message = findViewById(R.id.sms_input_text_field);
        send = findViewById(R.id.sms_send_button);

        send.setEnabled(false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            send.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public void onCountdownTimerClick(View view){
        //inflate the layout of popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_timer_small, null);

        //create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; //lets taps outside popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //show the popup window
        //which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void onSend(View v){
        String phoneNumber = number.getText().toString();
        String smsMessage  = message.getText().toString();

        if (phoneNumber == null || phoneNumber.length() == 0 || smsMessage == null || smsMessage.length() == 0){

            return ;
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null,null);
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
        }

    }
    */
}
