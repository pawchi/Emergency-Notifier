package org.chilon.emergencynotifier;

public class ObjectToSend {
    String phoneNumber;
    String smsContent;
    String timeLeft;

    public ObjectToSend(String phoneNumber, String smsContent, String timeLeft){
        this.phoneNumber = phoneNumber;
        this.smsContent = smsContent;
        this.timeLeft = timeLeft;
    }
}
