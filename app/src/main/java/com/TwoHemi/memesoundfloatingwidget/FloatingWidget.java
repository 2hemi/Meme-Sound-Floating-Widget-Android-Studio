package com.TwoHemi.memesoundfloatingwidget;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class FloatingWidget extends Service {

    WindowManager windowManager;
    View floatingView,floatingViewClose,musicListView;
    float height,width;
    TextView tempo,butt1,butt2,butt3,butt4,butt5,butt6;
    boolean open = true;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


        height = windowManager.getDefaultDisplay().getHeight();
        width = windowManager.getDefaultDisplay().getWidth();


        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout,null);
        floatingViewClose = LayoutInflater.from(this).inflate(R.layout.close_widget,null);
        musicListView = LayoutInflater.from(this).inflate(R.layout.music_list_layout,null);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,LAYOUT_FLAG,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        params.x = 0;
        params.y = 100;
        params.gravity = Gravity.TOP | Gravity.RIGHT;


        WindowManager.LayoutParams closeParams = new WindowManager.LayoutParams((int) width,140,LAYOUT_FLAG,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        closeParams.y = 0;
        closeParams.gravity = Gravity.BOTTOM | Gravity.CENTER;


        WindowManager.LayoutParams musicListViewParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,LAYOUT_FLAG,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        musicListViewParams.x = 0;
        musicListViewParams.y = 160;
        musicListViewParams.gravity = Gravity.TOP | Gravity.RIGHT;


        windowManager.addView(floatingViewClose,closeParams);
        windowManager.addView(floatingView,params);
        windowManager.addView(musicListView,musicListViewParams);


        floatingView.setVisibility(View.VISIBLE);
        floatingViewClose.setVisibility(View.INVISIBLE);
        musicListView.setVisibility(View.INVISIBLE);


        tempo = floatingView.findViewById(R.id.temp);

        tempo.setOnTouchListener(new View.OnTouchListener() {
             int initialX,initialY;
             float initialTouchX,initialTouchY;
             long startClickTime;
             int MAX_CLICK_DURATION = 300;
             @Override
             public boolean onTouch(View view, MotionEvent motionEvent) {
                 switch (motionEvent.getAction()){
                     case MotionEvent.ACTION_DOWN:
                         startClickTime = Calendar.getInstance().getTimeInMillis();

                         initialX = params.x;
                         initialY = params.y;

                         initialTouchX = motionEvent.getRawX();
                         initialTouchY = motionEvent.getRawY();

                         return true;

                     case MotionEvent.ACTION_UP:
                         long clickDuration = Calendar.getInstance().getTimeInMillis();
                         floatingViewClose.setVisibility(View.GONE);

                         params.x = initialX + (int)(initialTouchX - motionEvent.getRawX());
                         params.y = initialY + (int)(motionEvent.getRawY() - initialTouchY );


                        // if (params.x < (int)width / 2 ){

                            params.x = 0;
                             windowManager.updateViewLayout(floatingView,params);
//                         }
//                         else{
//                             params.x =(int) width - 200;
//                             floatingView.setX(width);
//                         }

                         if(clickDuration-startClickTime < MAX_CLICK_DURATION) {
                             //Toast.makeText(FloatingWidget.this, "Time", Toast.LENGTH_SHORT).show();
                             if (open) {
                                musicListViewParams.y = params.y + 150;
                                windowManager.updateViewLayout(musicListView, musicListViewParams);
                                musicListView.setVisibility(View.VISIBLE);
                                open = false;
                             }
                             else {
                                musicListView.setVisibility(View.GONE);
                                open = true;
                             }

                         }
                         else
                             if(params.y > (height*0.8))
                                 stopSelf();

                        return true;

                     case MotionEvent.ACTION_MOVE:

                         params.x = initialX +(int)(initialTouchX - motionEvent.getRawX());
                         params.y = initialY + (int)(motionEvent.getRawY() - initialTouchY );

                         windowManager.updateViewLayout(floatingView,params);
                         if (params.y > (height*0.4))
                             floatingViewClose.setVisibility(View.VISIBLE);
                         else
                             floatingViewClose.setVisibility(View.INVISIBLE);
                         if(params.y > (height*0.8))
                            floatingViewClose.setBackgroundResource(R.drawable.close_gradient);
                         else
                            floatingViewClose.setBackgroundResource(R.drawable.normal_gradient);

                         System.out.println("Fuuuuuuck "+( params.y > (height*0.4))+" ");

                         return true;

                 }

                 return false;
             }

         });

        butt1 = musicListView.findViewById(R.id.butt1);
        butt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FloatingWidget.this, "Time Fuck YOU", Toast.LENGTH_SHORT).show();

            }
        });

        butt2 = musicListView.findViewById(R.id.butt2);
        butt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FloatingWidget.this, "Time Fuck YOU", Toast.LENGTH_SHORT).show();

            }
        });

        butt3 = musicListView.findViewById(R.id.butt2);
        butt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FloatingWidget.this, "Time Fuck YOU", Toast.LENGTH_SHORT).show();

            }
        });

        butt4 = musicListView.findViewById(R.id.butt3);
        butt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FloatingWidget.this, "Time Fuck YOU", Toast.LENGTH_SHORT).show();

            }
        });

        butt5 = musicListView.findViewById(R.id.butt4);
        butt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FloatingWidget.this, "Time Fuck YOU", Toast.LENGTH_SHORT).show();

            }
        });

        butt6 = musicListView.findViewById(R.id.butt5);
        butt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FloatingWidget.this, "Time Fuck YOU", Toast.LENGTH_SHORT).show();

            }
        });

        butt1 = musicListView.findViewById(R.id.butt6);
        butt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FloatingWidget.this, "Time Fuck YOU", Toast.LENGTH_SHORT).show();

            }
        });


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(floatingView != null)
            windowManager.removeView(floatingView);
        if(floatingViewClose != null)
            windowManager.removeView(floatingViewClose);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
