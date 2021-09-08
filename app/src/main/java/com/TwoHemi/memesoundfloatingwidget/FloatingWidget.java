package com.TwoHemi.memesoundfloatingwidget;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.Calendar;

public class FloatingWidget extends Service implements View.OnClickListener{

    WindowManager windowManager;
    View floatingView,floatingViewClose,musicListView;
    float height,width;
    TextView tempo,butt1,butt2,butt3,butt4,butt5,butt6;
    ArrayList<String> nameArraylist;
    ArrayList<String> uriArrayList;
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


        nameArraylist = new ArrayList<>();
        uriArrayList = new ArrayList<>();
        loadData();

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
             final int MAX_CLICK_DURATION = 300;
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
                                setList();
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


                         return true;

                 }

                 return false;
             }

         });


        butt1 = musicListView.findViewById(R.id.butt1);
        butt2 = musicListView.findViewById(R.id.butt2);
        butt3 = musicListView.findViewById(R.id.butt3);
        butt4 = musicListView.findViewById(R.id.butt4);
        butt5 = musicListView.findViewById(R.id.butt5);
        butt6 = musicListView.findViewById(R.id.butt6);
        butt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMusic(0);
            }
        });
        butt2.setOnClickListener(this);
        butt3.setOnClickListener(this);
        butt4.setOnClickListener(this);
        butt5.setOnClickListener(this);
        butt6.setOnClickListener(this);

        return START_STICKY;
    }

    private void setList() {
        //this is bad I know :)
        if (nameArraylist.size() >= 1) {
            butt1.setText(nameArraylist.get(0));
            if (nameArraylist.size() >= 2) {
                butt2.setText(nameArraylist.get(1));
                if (nameArraylist.size() >= 3) {
                    butt3.setText(nameArraylist.get(2));
                    if (nameArraylist.size() >= 4) {
                        butt4.setText(nameArraylist.get(3));
                        if (nameArraylist.size() >= 5) {
                            butt5.setText(nameArraylist.get(4));
                            if (nameArraylist.size() >= 6) {
                                butt6.setText(nameArraylist.get(5));
                            }
                        }
                    }
                }
            }
        }
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

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String namesJson = sharedPreferences.getString("names", null);
        String uriJson = sharedPreferences.getString("uris", null);

        if (namesJson != null && uriJson != null) {

            Type nameType = new TypeToken<ArrayList<String>>() {}.getType();
            Type uriType = new TypeToken<ArrayList<String>>() {}.getType();

            nameArraylist = gson.fromJson(namesJson, nameType);
            uriArrayList = gson.fromJson(uriJson, uriType);
        } else
            Toast.makeText(this, "list is empty", Toast.LENGTH_SHORT).show();

    }

    private void playMusic(int position) {
        Uri uri = Uri.parse(uriArrayList.get(position));
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            System.out.println("Fuuuuuck1 ");
            mediaPlayer.setDataSource(FloatingWidget.this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Fuuuuuck 2 " + uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Fuuuuuck3 " + uri);

        mediaPlayer.start();
    }


    @Override
    public void onClick(View view) {

        if (view == butt1)
            playMusic(0);

        if (view == butt2 && nameArraylist.size() == 2)
            playMusic(1);

        if (view == butt3 && nameArraylist.size() == 3)
            playMusic(2);

        if (view == butt4 && nameArraylist.size() == 4)
            playMusic(3);

        if (view == butt5 && nameArraylist.size() == 5)
            playMusic(4);

        if (view == butt6 && nameArraylist.size() == 6)
            playMusic(5);
    }
}
