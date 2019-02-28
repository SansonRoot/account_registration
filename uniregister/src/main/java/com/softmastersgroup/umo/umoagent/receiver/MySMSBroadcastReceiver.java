package com.softmastersgroup.umo.umoagent.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.softmastersgroup.umo.umoagent.MainApp;


import static com.softmastersgroup.umo.umoagent.VerifyPhone.etCode;

public class MySMSBroadcastReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (SMS_RECEIVED.equalsIgnoreCase(intent.getAction())){
            Bundle extras = intent.getExtras();

            if (extras !=null){
                Object[] pdus = (Object[]) extras.get("pdus");

                final SmsMessage[] msges = new SmsMessage[pdus.length];

                for (int i=0;i<pdus.length;i++){
                    msges[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                }

                if (msges.length > -1){

                    String address = msges[0].getDisplayOriginatingAddress();

                    if ("smasters".equalsIgnoreCase(address)){

                        /*Intent i = new Intent(context,MainApp.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);*/

                        String msg = msges[0].getMessageBody();

                        if (msg.length()<7) return;

                        String code = msg.substring(msg.length()-6,msg.length());

                        verify(code);

                        Log.d(TAG,msg);

                    }

                }

            }

        }

    }

    private void verify(String code){
        etCode.setText(code);
    }
}
