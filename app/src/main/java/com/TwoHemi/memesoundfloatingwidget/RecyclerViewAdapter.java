package com.TwoHemi.memesoundfloatingwidget;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter{


    private ArrayList<String> nameArrayList,uriArrayList;
    public RecyclerViewClickListener recyclerViewClickListener;
    private Context context;
    private final OnStartDragListener mDragStartListener;

    public RecyclerViewAdapter(OnStartDragListener dragStartListener, ArrayList<String> list,ArrayList<String> uriList,RecyclerViewClickListener recyclerViewClickListener,Context context) {
        mDragStartListener = dragStartListener;
        this.nameArrayList = list;
        this.uriArrayList = uriList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.context = context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        public final ImageView handle;
        private RecyclerViewClickListener listener;

        public ViewHolder(View view, RecyclerViewClickListener recyclerViewClickListener) {
            super(view);
            listener = recyclerViewClickListener;
            textView = view.findViewById(R.id.music_name);
            handle = view.findViewById(R.id.handle);
            view.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(getAdapterPosition());

        }

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.music_list, viewGroup, false);

        return new ViewHolder(view,recyclerViewClickListener);
    }



    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        holder.getTextView().setText(nameArrayList.get(position));

//
        holder.handle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return nameArrayList.size();
    }



    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(nameArrayList, i, i + 1);
                Collections.swap(uriArrayList, i, i+1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(nameArrayList, i, i - 1);
                Collections.swap(uriArrayList, i, i+1);
            }
        }
        saveData(nameArrayList,uriArrayList);
        notifyItemMoved(fromPosition, toPosition);

        //return true;
    }

    @Override
    public void onItemDismiss(int position) {
        nameArrayList.remove(position);
        uriArrayList.remove(position);
        saveData(nameArrayList,uriArrayList);
        notifyItemRemoved(position);
    }


    private void saveData(ArrayList<String> nameList ,ArrayList<String> uriList) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();


        Gson gson = new Gson();

        String nameJson = gson.toJson(nameList);
        String uriJson = gson.toJson(uriList);

        editor.putString("names", nameJson);
        editor.putString("uris", uriJson);

        editor.apply();
    }


    public interface RecyclerViewClickListener {
        void onClick(int position);
    }
}

