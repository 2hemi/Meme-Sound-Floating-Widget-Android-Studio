package com.TwoHemi.memesoundfloatingwidget;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    private static final String[] STRINGS = new String[]{
            "MusicOne", "MusicTwo", "MusicThree", "MusicFour", "MusicFive", "MusicSix" };

    private final List<String> list = new ArrayList<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        public final ImageView handle;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.music_name);
            handle = view.findViewById(R.id.handle);
        }

        public TextView getTextView() {
            return textView;
        }
    }


    private final OnStartDragListener mDragStartListener;

    public RecyclerViewAdapter(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        list.addAll(Arrays.asList(STRINGS));
    }

//    public RecyclerViewAdapter() {
//        list.addAll(Arrays.asList(STRINGS));
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.music_list, viewGroup, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        holder.getTextView().setText(list.get(position));

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

//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//
//        viewHolder.getTextView().setText(list[position]);
//
//
////        viewHolder.handle.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
////                    mDragStartListener.onStartDrag(viewHolder);
////                }
////                return false;
////            }
////        });
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        //return true;
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

}

