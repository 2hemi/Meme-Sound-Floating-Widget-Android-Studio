package com.TwoHemi.memesoundfloatingwidget;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    RelativeLayout shortcut,settings;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Settings.canDrawOverlays(this))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        aSwitch = findViewById(R.id.switch1);

        aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (aSwitch.isChecked()) {
                startService( new Intent(MainActivity.this , FloatingWidget.class));
            }
        });

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(v -> {
         //   Toast.makeText(MainActivity.this, "working fine", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(MainActivity.this, com.TwoHemi.memesoundfloatingwidget.Settings.class);
            startActivity(intent);
        });

        shortcut = findViewById(R.id.shortcut);
        shortcut.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "working fine", Toast.LENGTH_SHORT).show();
        });

        Button button = findViewById(R.id.butt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity.this , com.TwoHemi.memesoundfloatingwidget.Settings.class));
            }
        });


    }



}