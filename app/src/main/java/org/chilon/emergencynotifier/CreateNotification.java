package org.chilon.emergencynotifier;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
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
import java.util.Locale;

public class CreateNotification extends AppCompatActivity {

    LinearLayout myparent;
    final  int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CALL = 1;
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
    int smsesWaiting;
    int smsesSent;

    StatePhoneReceiver myPhoneStateListner;
    TelephonyManager manager;
    boolean callFromApp = false;
    boolean callFromOffHook = false;

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

        //Call action
        myPhoneStateListner = new StatePhoneReceiver(this);
        manager = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));

        switch (extras){
            case 0:
                myparent.addView(phoneNumber);
                myparent.addView(writeSms);
                myparent.addView(sendingTime);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSend(view);
                    }
                });
                break;
            case 1:
                myparent.addView(phoneNumber);
                myparent.addView(sendingTime);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        manager.listen(myPhoneStateListner, PhoneStateListener.LISTEN_CALL_STATE);
                        callFromApp = true;
                        preparePhoneCall();
                    }
                });
                break;
            case 2:
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


    //Popup Window Countdown*******************************************************************************
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
        //which view you pass it doesn't matter, it is only used for the window tolken
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

    //Popup Window Calendar*******************************************************************************
    public void onCalendarClick(View view){
        //inflate the layout of popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View calendarView = inflater.inflate(R.layout.popup_calendar, null);
        //create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; //lets taps outside popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(calendarView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        calendarView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
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

    //SMS **************************************************************************************
    public void onSend(View v){
        number = findViewById(R.id.input_phone_number_field);
        message = findViewById(R.id.sms_input_text_field);
        String messageWillBeSend = getResources().getString(R.string.toast_message_will_be_send);
        Toast.makeText(this, messageWillBeSend, Toast.LENGTH_LONG).show();
        startSmsTimer();
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

    public void prepareSmsHistory(String phoneNo, String smsMessage){
        Intent intent = new Intent(this, MainActivity.class);
        ObjectToSend objectToSend = new ObjectToSend(phoneNo, smsMessage, "20:14");
        intent.putExtra("sms_content", objectToSend);
        startActivity(intent);
    }

    public void updateNumberOfMessages(){
        //waitedMessages.remove(0);
        //Integer amountOfMessages = waitedMessages.size();
        saveAndSubstractNumberOfListToSharedPreferencesTemp();

        Intent resultIntent = new Intent(this, MainActivity.class);
        //resultIntent.putExtra(UPDATED_NUMBER_OF_MESSAGES, amountOfMessages);
        //setResult(RESULT_OK, resultIntent);
        //finish();
        startActivity(resultIntent);
    }

    public void saveAndSubstractNumberOfListToSharedPreferencesTemp(){
        SharedPreferences sharedPreferences = getSharedPreferences("smsList", Activity.MODE_PRIVATE);
        int sharedNoOfSms = sharedPreferences.getInt("numberOfSms", 0);
        sharedNoOfSms = sharedNoOfSms - 1;

        SharedPreferences sharedPref = getSharedPreferences("smsList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("numberOfSms", sharedNoOfSms);
        editor.apply();
    }

    public void saveAndAddNumberOfListToSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("smsList", Activity.MODE_PRIVATE);
        int sharedNoOfSms = sharedPreferences.getInt("numberOfSms", 0);
        sharedNoOfSms = sharedNoOfSms + 1;

        SharedPreferences sharedPref = getSharedPreferences("smsList", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("numberOfSms", sharedNoOfSms);
        editor.apply();
    }

    private void startSmsTimer(){
        final String phoneNumber = number.getText().toString();
        final String smsMessage  = message.getText().toString();
        prepareSmsHistory(phoneNumber, smsMessage);
        new CountDownTimer(timeLeftInMillis, 1000){

            public void onTick(long millisUntilFinished){
                timeLeftInMillis = millisUntilFinished;

            }

            public void onFinish(){
                try {
                    sendSms(smsMessage, phoneNumber);
                    updateNumberOfMessages();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    //Phone Call ****************************************************************************
    public void preparePhoneCall(){
        number = findViewById(R.id.input_phone_number_field);
        String phoneCallWillStart = getResources().getString(R.string.toast_phone_call_wiil_start);
        Toast.makeText(this, phoneCallWillStart, Toast.LENGTH_LONG).show();
        startPhoneCallTimer(number.getText().toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void startPhoneCallTimer(final String phoneNo){
        new CountDownTimer(timeLeftInMillis, 1000){

            public void onTick(long millisUntilFinished){
                timeLeftInMillis = millisUntilFinished;

            }

            public void onFinish(){
                try {
                    startPhoneCall(phoneNo);;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }

    public void startPhoneCall(String phoneNo){
        //String callNumber = number.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));
        startActivity(callIntent);
        //AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //audioManager.setMode(AudioManager.MODE_IN_CALL);
        //audioManager.setSpeakerphoneOn(true);
        //audioManager.setMode(AudioManager.MODE_NORMAL);
    }


}
