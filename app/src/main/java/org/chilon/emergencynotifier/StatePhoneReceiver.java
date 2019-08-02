package org.chilon.emergencynotifier;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class StatePhoneReceiver extends PhoneStateListener {
    Context context;
    public StatePhoneReceiver(Context context){
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state){
            case TelephonyManager
                    .CALL_STATE_OFFHOOK:
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e){

                }
                //Activate Loudspeaker
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                AudioManager audioManager2 = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager2.setMode(AudioManager.MODE_NORMAL);

        }
    }
}
