package org.chilon.emergencynotifier;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateNotification extends AppCompatActivity {

    LinearLayout myparent;
    final  int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    EditText number;
    EditText message;
    Button send;
    Button okCountdown;
    Button cancelCountdown;
    int secondsCountdown;
    int minutesCountdown;
    int hoursCountdown;
    private long timeLeftInMillis = 0;
    TextView setSendingTime;
    ArrayList<ObjectToSend> waitedMessages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notifier);

        //Response from MainActivity
        int extras = getIntent().getIntExtra(MainActivity.MAIN_RESPONSE, -1);

        //Inflate with phone number view
        myparent = findViewById(R.id.content_main_layout_to_inflate_into);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View phoneNumber = layoutInflater.inflate(R.layout.enter_phone_number, null, false);
        View sendingTime = layoutInflater.inflate(R.layout.setup_sending_time, null, false);
        View writeSms = layoutInflater.inflate(R.layout.write_sms, null, false);

        send = findViewById(R.id.sms_send_button_in_inflation_view);
        waitedMessages = new ArrayList<>();

        switch (extras){
            case 1:
                myparent.addView(phoneNumber);
                myparent.addView(writeSms);
                myparent.addView(sendingTime);
                break;
            case 2:
                myparent.addView(phoneNumber);
                myparent.addView(sendingTime);
                break;
            case 3:
                myparent.addView(phoneNumber);
                break;
        }

        send.setEnabled(false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            send.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public void onSend(View v){
        number = findViewById(R.id.input_phone_number_field);
        message = findViewById(R.id.sms_input_text_field);
        String messageWillBeSend = getResources().getString(R.string.toast_message_will_be_send);
        Toast.makeText(this, messageWillBeSend, Toast.LENGTH_LONG).show();
        startTimer();
    }

    public void sendSms(String smsMessage, String phoneNumber){

        String messageSent = getResources().getString(R.string.toast_message_sent);
        String noPermission = getResources().getString(R.string.toast_no_permission);
        if (phoneNumber == null || phoneNumber.length() == 0 || smsMessage == null || smsMessage.length() == 0){
            return ;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null,null);
            Toast.makeText(this, messageSent, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, noPermission, Toast.LENGTH_LONG).show();
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

        //dismiss the popup window when touched outside or click cancel button
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });

        //Popup countdown window action
        okCountdown = popupView.findViewById(R.id.ok_button_countdown);
        cancelCountdown = popupView.findViewById(R.id.cancel_button_countdown);

        final EditText secondCountdownInput = popupView.findViewById(R.id.input_seconds);
        final EditText minutesCountdownInput = popupView.findViewById(R.id.input_minutes);
        final EditText hoursCountdownInput = popupView.findViewById(R.id.input_hours);


        cancelCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        okCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!secondCountdownInput.getText().toString().isEmpty() && secondCountdownInput.getText().toString() != "" ) {
                    secondsCountdown = Integer.parseInt(secondCountdownInput.getText().toString());
                } else {
                    secondsCountdown = 0;
                }

                if (!minutesCountdownInput.getText().toString().isEmpty() && minutesCountdownInput.getText().toString() != ""){
                    minutesCountdown = Integer.parseInt(minutesCountdownInput.getText().toString());
                } else {
                    minutesCountdown = 0;
                }

                if (!hoursCountdownInput.getText().toString().isEmpty() && hoursCountdownInput.getText().toString() != ""){
                    hoursCountdown = Integer.parseInt(hoursCountdownInput.getText().toString());
                } else {
                    hoursCountdown = 0;
                }

                updateSendingTime(secondsCountdown, minutesCountdown, hoursCountdown);
                popupWindow.dismiss();
            }
        });
    }

    private void updateSendingTime(int seconds, int minutes, int hours){
        setSendingTime = findViewById(R.id.choosen_sending_time_value);
        String formatForTime = getResources().getString(R.string.format_show_sending_time);
        String formatedTimeToSend = String.format(Locale.getDefault(), formatForTime, hours, minutes, seconds);
        setSendingTime.setText(formatedTimeToSend);
        timeLeftInMillis = (seconds * 1000)+(minutes * 60 * 1000)+(hours * 3600 * 1000);
    }

    private void updateCountDownText(){

    }


    private void startTimer(){
        final String phoneNumber = number.getText().toString();
        final String smsMessage  = message.getText().toString();
        ObjectToSend objectToSend = new ObjectToSend(phoneNumber, smsMessage, "");
        waitedMessages.add(objectToSend);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("sendingList", waitedMessages);
        new CountDownTimer(timeLeftInMillis, 1000){

            public void onTick(long millisUntilFinished){
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();

            }

            public void onFinish(){
                try {
                    sendSms(smsMessage, phoneNumber);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

}
