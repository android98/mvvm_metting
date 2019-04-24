package com.example.mvvmmeeting.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mvvmmeeting.Models.Recording;
import com.example.mvvmmeeting.R;

import java.io.IOException;
import java.util.ArrayList;

public class Recycler_Adapter_Recording extends RecyclerView.Adapter
        <Recycler_Adapter_Recording.ViewHolder> {


    private ArrayList<Recording> recordingArrayList;
    private Context context;
    private MediaPlayer mPlayer;
    private boolean isPlaying = false;
    private int last_index = -1;

    public Recycler_Adapter_Recording(ArrayList<Recording> recordingArrayList, Context context) {
        this.recordingArrayList = recordingArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.recycler_recording_list
                , viewGroup, false);

        return new ViewHolder(inflate);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        setUpData(viewHolder, i);
    }

    @Override
    public int getItemCount() {
        return recordingArrayList.size();
    }


    private void setUpData(ViewHolder holder, int position) {

        Recording recording = recordingArrayList.get(position);
        holder.textViewName.setText(recording.getFileName());

        if( recording.isPlaying() ){
            holder.imageViewPlay.setImageResource(R.drawable.ic_pause);
            TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            holder.seekBar.setVisibility(View.VISIBLE);
            holder.seekUpdation(holder);
        }else{
            holder.imageViewPlay.setImageResource(R.drawable.ic_play);
            TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            holder.seekBar.setVisibility(View.GONE);
        }

        holder.manageSeekBar(holder);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewPlay;
        SeekBar seekBar;
        TextView textViewName;
        private String recordingUri;
        private int lastProgress = 0;
        private Handler mHandler = new Handler();
        ViewHolder holder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewPlay = itemView.findViewById(R.id.imageViewPlay);
            seekBar = itemView.findViewById(R.id.seekBar);
            textViewName = itemView.findViewById(R.id.textViewRecordingname);

            imageViewPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Recording recording = recordingArrayList.get(position);

                    recordingUri = recording.getUri();

                    if (isPlaying) {
                        stopPlaying();
                        if (position == last_index) {
                            recording.setPlaying(false);
                            stopPlaying();
                            notifyItemChanged(position);
                        } else {
                            markAllPaused();
                            recording.setPlaying(true);
                            notifyItemChanged(position);
                            startPlaying(recording, position);
                            last_index = position;
                        }

                    } else {
                        if (recording.isPlaying()) {
                            recording.setPlaying(false);
                            stopPlaying();
                            Log.d("isPlayin", "True");
                        } else {
                            startPlaying(recording, position);
                            recording.setPlaying(true);
                            seekBar.setMax(mPlayer.getDuration());
                            Log.d("isPlayin", "False");
                        }
                        notifyItemChanged(position);
                        last_index = position;
                    }
                }

            });
        }


        public void manageSeekBar(ViewHolder holder){
            holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if( mPlayer!=null && fromUser ){
                        mPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }


        private void markAllPaused() {
            for( int i=0; i < recordingArrayList.size(); i++ ){
                recordingArrayList.get(i).setPlaying(false);
                recordingArrayList.set(i,recordingArrayList.get(i));
            }
            notifyDataSetChanged();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                seekUpdation(holder);
            }
        };

        private void seekUpdation(ViewHolder holder) {
            this.holder = holder;
            if(mPlayer != null){
                int mCurrentPosition = mPlayer.getCurrentPosition() ;
                holder.seekBar.setMax(mPlayer.getDuration());
                holder.seekBar.setProgress(mCurrentPosition);
                lastProgress = mCurrentPosition;
            }
            mHandler.postDelayed(runnable, 100);
        }

        private void stopPlaying() {
            try{
                mPlayer.release();
            }catch (Exception e){
                e.printStackTrace();
            }
            mPlayer = null;
            isPlaying = false;
        }


        private void startPlaying(final Recording audio, final int position) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(recordingUri);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e("LOG_TAG", "prepare() failed");
            }
            //showing the pause button
            seekBar.setMax(mPlayer.getDuration());
            isPlaying = true;

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audio.setPlaying(false);
                    notifyItemChanged(position);
                }
            });
        }

    }
}
