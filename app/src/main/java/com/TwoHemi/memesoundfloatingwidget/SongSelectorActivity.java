package com.TwoHemi.memesoundfloatingwidget;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.SplittableRandom;

public class SongSelectorActivity extends AppCompatActivity implements OnStartDragListener, RecyclerViewAdapter.RecyclerViewClickListener {
    ArrayList<String> nameArrayList;
    ArrayList<String> uriArrayList;
    Button addNew;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerViewAdapter.RecyclerViewClickListener listener ; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selector);

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            if (data != null)
                               musicLoader(data);
                        }
                    }
                });



        recyclerView = findViewById(R.id.music_list);

        nameArrayList = new ArrayList<>();
        uriArrayList = new ArrayList<>();

        loadData();


        adapter = new RecyclerViewAdapter(this,nameArrayList,uriArrayList,this,getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.music_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


        addNew = findViewById(R.id.add_new);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameArrayList.size() < 6 && uriArrayList.size() < 6) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/mpeg");
                    someActivityResultLauncher.launch(Intent.createChooser(intent, "shitter"));
                }
                else
                    Toast.makeText(SongSelectorActivity.this, "the list is full", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
        saveData(nameArrayList,uriArrayList);

    }

    @Override
    public void onClick(int position) {

        playMusic(position);

    }

    private void musicLoader(Intent data) {
        Uri uri = data.getData();
        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String songName = returnCursor.getString(nameIndex);
        uriArrayList.add(uri.toString());
        nameArrayList.add(songName);

        saveData(nameArrayList,uriArrayList);

        adapter.notifyDataSetChanged();
    }


    private void playMusic(int position) {
        Uri uri = Uri.parse(uriArrayList.get(position));

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(SongSelectorActivity.this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }


    private void saveData(ArrayList<String> nameList ,ArrayList<String> uriList) {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        Gson gson = new Gson();

        String nameJson = gson.toJson(nameList);
        String uriJson = gson.toJson(uriList);

        editor.putString("names", nameJson);
        editor.putString("uris", uriJson);

        editor.apply();
    }

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        Gson gson = new Gson();
        String namesJson = sharedPreferences.getString("names", null);
        String uriJson = sharedPreferences.getString("uris", null);

        if (namesJson != null && uriJson != null) {

            Type nameType = new TypeToken<ArrayList<String>>() {}.getType();
            Type uriType = new TypeToken<ArrayList<String>>() {}.getType();

            nameArrayList = gson.fromJson(namesJson, nameType);
            uriArrayList = gson.fromJson(uriJson, uriType);
        } else
            Toast.makeText(this, "list is empty", Toast.LENGTH_SHORT).show();

    }
}