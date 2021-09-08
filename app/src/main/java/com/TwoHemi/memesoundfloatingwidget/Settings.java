package com.TwoHemi.memesoundfloatingwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    SeekBar seekBar;
//    TextView circle;
    ImageView circle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        seekBar = findViewById(R.id.seekBar1);
        circle = findViewById(R.id.circle);

        Drawable drawable = getResources().getDrawable(R.drawable.circle);
        drawable.setBounds(0,0,50,50);
        //seekBar.getThumb();
        seekBar.setProgress(0);
        seekBar.setMax(5);
        seekBar.setProgress(1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                circle.setWidth(i+60);
//                circle.setHeight(i+60);
//                circle.setTextSize(i);
                circle.setScaleX(i);
                circle.setScaleY(i);
                System.out.println("Fuuuuuck " + i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("Fuuuuuck done");

            }
        });

    }
}