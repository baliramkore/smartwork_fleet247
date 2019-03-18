package com.fleet247.driver.broadcastreceiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.fleet247.driver.data.repository.LoginRepository;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//This Receiver is used to auto fetch OTP from message
public class MessageReceiver extends BroadcastReceiver {

    String msg_from;
    String msgBody;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
        SmsMessage[] msgs = null;
        LoginRepository loginRepository=LoginRepository.getInstance((Application)context);

        if (bundle != null) {
            //---retrieve the SMS message received---
            try {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    msg_from = msgs[i].getOriginatingAddress();
                    msgBody = msgs[i].getMessageBody();
                }
                if(msgBody.contains("Fleet247 Driver App")) {
                    Pattern p = Pattern.compile("\\d+");
                    Matcher m = p.matcher(msgBody);
                    ArrayList<String> otpList = new ArrayList<>();
                    while (m.find()) {
                        otpList.add(m.group());
                    }
                    loginRepository.setOtp(otpList.get(0)); //Save otp in Repository.
                }
            } catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
            }
        }
    }
}
