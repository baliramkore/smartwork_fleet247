package com.fleet247.driver.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fleet247.driver.R;
import com.fleet247.driver.activities.MainActivity;

/**
 * Created by sandeep on 21/12/17.
 */

public class FloatingHeadDialog extends Service implements View.OnClickListener,View.OnTouchListener{


    WindowManager windowManager;
    WindowManager.LayoutParams params;
    ImageView drawrer;
    SharedPreferences dialogPref;
    String booking;
    int initialX;
    int initialY;
    float initialTouchX;
    float initialTouchY;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service","Created");
        windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);

        drawrer=new ImageView(this);
        drawrer.setImageResource(R.mipmap.ic_launcher_round);

        int layoutFlag;
        if(Build.VERSION.SDK_INT>=26){
            layoutFlag=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            layoutFlag=WindowManager.LayoutParams.TYPE_PHONE;
        }

        params=new WindowManager.LayoutParams(
                200,
                200,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity= Gravity.TOP|Gravity.LEFT;
        params.x=0;
        params.y=400;
        windowManager.addView(drawrer,params);
        drawrer.setOnClickListener(this);
        drawrer.setOnTouchListener(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dialogPref=getSharedPreferences("dialogPref",MODE_PRIVATE);
        booking=dialogPref.getString("bookingInfo",null);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (drawrer!=null){
            windowManager.removeView(drawrer);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(FloatingHeadDialog.this,"Running App Overlay",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(FloatingHeadDialog.this, MainActivity.class);
        intent.putExtra("bookingInfo",booking);
        startActivity(intent);
        stopSelf();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("Action","Down");
                initialX=params.x;
                initialY=params.y;

                initialTouchX=motionEvent.getRawX();
                initialTouchY=motionEvent.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.d("Action","Move");
                params.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                params.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                windowManager.updateViewLayout(drawrer,params);
                return true;
            case MotionEvent.ACTION_UP:
                float diffX=motionEvent.getRawX() - initialTouchX;
                float diffY=motionEvent.getRawY() - initialTouchY;
                if(diffX<3 && diffY<3) {
                    Log.d("Action", "Pressed");
                    Intent intent = new Intent(FloatingHeadDialog.this, MainActivity.class);
                    intent.putExtra("bookingInfo", booking);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogPref.edit().clear().commit();
                    startActivity(intent);
                    stopSelf();
                }
                return true;


        }
        return false;
    }
}
