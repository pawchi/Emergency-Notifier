package org.chilon.emergencynotifier;

import android.os.Parcel;
import android.os.Parcelable;

public class ObjectToSend implements Parcelable {
    String phoneNumber;
    String smsContent;
    String sendingTime;

    public ObjectToSend(String phoneNumber, String smsContent, String sendingTime){
        this.phoneNumber = phoneNumber;
        this.smsContent = smsContent;
        this.sendingTime = sendingTime;
    }

    protected ObjectToSend(Parcel in) {
        phoneNumber = in.readString();
        smsContent = in.readString();
        sendingTime = in.readString();
    }

    public static final Creator<ObjectToSend> CREATOR = new Creator<ObjectToSend>() {
        @Override
        public ObjectToSend createFromParcel(Parcel in) {
            return new ObjectToSend(in);
        }

        @Override
        public ObjectToSend[] newArray(int size) {
            return new ObjectToSend[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phoneNumber);
        parcel.writeString(smsContent);
        parcel.writeString(sendingTime);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public String getSendingTime() {
        return sendingTime;
    }
}
