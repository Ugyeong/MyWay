package com.example.myway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            Log.d("TAG", "SmsReceiver : onReceiver");
            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    ObservableObject.getInstance().updateValue(message);
                    Log.d("TAG", "SmsReceiver : onReceiver(CommonStatusCodes.SUCCESS)");
                    Log.d("TAG", "message : "+message);

                    break;
                case CommonStatusCodes.TIMEOUT: //타임아웃 기본값 5분
                    Log.d("TAG", "SmsReceiver : onReceiver(CommonStatusCodes.TIMEOUT)");
                    break;
            }
        }
    }
}