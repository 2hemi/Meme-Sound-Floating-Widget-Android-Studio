package com.TwoHemi.memesoundfloatingwidget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class SongSelectorActivity extends AppCompatActivity implements OnStartDragListener{
    String[] list;
    RecyclerView recyclerView;
    private ItemTouchHelper mItemTouchHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selector);

        recyclerView = findViewById(R.id.music_list);

        list = new String[]{"djsuhf","dpidhf","ofhweuh","ofhweuh","ofhweuh","ofhweuh","ofhweuh","ofhweuh"};

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.music_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);

    }
}