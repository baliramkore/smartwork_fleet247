package com.fleet247.driver.tracking;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;

public class ActivityRecognitionService extends IntentService {

    public ActivityRecognitionService(){
        super("ActivityRecognitionService");
    }

    public ActivityRecognitionService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("Activity","Recognised");
        /*if(ActivityRecognitionResult.hasResult(intent)){
            Log.d("Activity","Present");
            final ActivityRecognitionResult activityRecognitionResult=ActivityRecognitionResult.extractResult(intent);
            Log.d("Most Probable Activity",activityRecognitionResult.getMostProbableActivity().toString()
            +" "+activityRecognitionResult.getMostProbableActivity().getConfidence());

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),activityRecognitionResult.getMostProbableActivity().toString()+
                            " "+activityRecognitionResult.getMostProbableActivity().getConfidence() ,Toast.LENGTH_LONG).show();
                }
            });
        }*/

        if (ActivityTransitionResult.hasResult(intent)) {
            Log.d("Event","Available");
            ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);
            for (final ActivityTransitionEvent event : result.getTransitionEvents()) {
                Log.d("Event",event.toString());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),event.getActivityType()+
                                " "+event.getTransitionType(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
