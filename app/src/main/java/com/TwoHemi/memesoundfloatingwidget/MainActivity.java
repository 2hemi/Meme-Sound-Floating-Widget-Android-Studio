package com.TwoHemi.memesoundfloatingwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RelativeLayout shortcut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shortcut = findViewById(R.id.shortcut);
        shortcut.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "working fine", Toast.LENGTH_SHORT).show();
        });
    }
}