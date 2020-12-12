package com.androidprojects.bartek.warehouseinventoryapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_FILTER = "SMS_FILTER";
    public static final String SMS_MSG_KEY = "SMS_MSG_KEY";

    @Override
    public void onReceive(Context context, Intent inte) {
        //this receives the sms
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(inte);
        for (int i = 0; i < messages.length; i++) {
            SmsMessage curMsg = messages[i];
            String msg = curMsg.getDisplayMessageBody();

            Intent msgIntent = new Intent();
            msgIntent.setAction(SMS_FILTER);
            msgIntent.putExtra(SMS_MSG_KEY, msg);
            context.sendBroadcast(msgIntent);
        }
    }
}