package com.TwoHemi.memesoundfloatingwidget;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class FloatingWidget extends Service {

    WindowManager windowManager;
    LinearLayout linearLayout;
    ImageView imageClose;
    View floatingView;
    float height,width;
    TextView tempo;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout,null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,LAYOUT_FLAG,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        params.x = 0;
        params.y = 100;
        params.gravity = Gravity.TOP | Gravity.RIGHT;

        WindowManager.LayoutParams imageparams = new WindowManager.LayoutParams(140,140,LAYOUT_FLAG,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        imageparams.y = 100;
        imageparams.gravity = Gravity.BOTTOM | Gravity.CENTER;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        imageClose = new ImageView(this);
        imageClose.setImageResource(R.mipmap.ic_close_round);
        imageClose.setVisibility(View.INVISIBLE);


        windowManager.addView(imageClose,imageparams);
        windowManager.addView(floatingView,params);

        floatingView.setVisibility(View.VISIBLE);

        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getWidth();

         tempo = floatingView.findViewById(R.id.temp);

         tempo.setOnTouchListener(new View.OnTouchListener() {
             int initialX,initialY;
             float initialTouchX,initialTouchY;
             long startClickTime;
             int MAX_CLICK_DURATION = 200;
             @Override
             public boolean onTouch(View view, MotionEvent motionEvent) {
                 switch (motionEvent.getAction()){
                     case MotionEvent.ACTION_DOWN:
                         startClickTime = Calendar.getInstance().getTimeInMillis();
                         imageClose.setVisibility(View.VISIBLE);

                         initialX = params.x;
                         initialY = params.y;

                         initialTouchX = motionEvent.getRawX();
                         initialTouchY = motionEvent.getRawY();

                         return true;

                     case MotionEvent.ACTION_UP:
                         long clickDuration = Calendar.getInstance().getTimeInMillis();
                         imageClose.setVisibility(View.GONE);

                         params.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
                         params.y = initialY + (int)(motionEvent.getRawY() - initialTouchY );


                         if(clickDuration < MAX_CLICK_DURATION)
                             Toast.makeText(FloatingWidget.this, "Time"  , Toast.LENGTH_SHORT).show();
                         else
                             if(params.y > (height*0.6))
                                 stopSelf();
                         return true;
                     case MotionEvent.ACTION_MOVE:

                         params.x = initialX +(int)(initialTouchX - motionEvent.getRawX());
                         params.y = initialY + (int)(motionEvent.getRawY() - initialTouchY );

                         windowManager.updateViewLayout(floatingView,params);

                         if(params.y > (height*0.6))
                            imageClose.setImageResource(R.mipmap.ic_close_round);
                         else
                            imageClose.setImageResource(R.mipmap.ic_close_white_round);


                         return true;

                 }

                 return false;
             }
         });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(floatingView != null)
            windowManager.removeView(floatingView);
        if(imageClose != null)
            windowManager.removeView(imageClose);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
