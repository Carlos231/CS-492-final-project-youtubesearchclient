package com.example.youtubesearchclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VideoInfoHolder> {
    private static final String TAG = RecyclerAdapter.class.getSimpleName();
    private List<YoutubeVideos> mVideos;
    //unique video IDs retrieved from URLs
    String[] VideoID = {"NKwwh2Iai2w", "hwebw3K7DiI", "aW7LPlp0-q4", "w_Q_K_Baxjw", "L09rxyRbJgs"};
    String[] VideoTitles = {"Dr.Anthony Fauci says everything is on the table to fight spread of coronavirus",
                            "U.S. Declares State of Emergency Over Coronavirus | Morning Joes | MSNBC",
                            "Sanders on Biden climate change policy:Nowhere near enough",
                            "A message from Trevor:Stay Home | The Daily Show",
                            "Biden and Sander's first one-on-one debate in 3 minutes"};
    Context context;
    private static String API_KEY = "AIzaSyBTSiIC7IChHAqvcAuJymSabvWFJ0kvslk";

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new VideoInfoHolder(itemView);
    }

    public void updateResults(List<YoutubeVideos> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {
        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                Log.d(TAG,"thumbnail error");
            }
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };

        holder.ThumbnailView.initialize(API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                if(mVideos != null) {
                    youTubeThumbnailLoader.setVideo(mVideos.get(position).videoId);
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                    holder.TitleTextView.setText(mVideos.get(position).title);
                }
            }
            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG,"initialization failure");
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mVideos != null) {
            return mVideos.size();
        }else
            return 0;
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView ThumbnailView;
        protected ImageView playButton;
        protected TextView TitleTextView;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            playButton = itemView.findViewById(R.id.play_button);
            TitleTextView = itemView.findViewById(R.id.title_tv);
            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView = itemView.findViewById(R.id.youtube_rl);
            ThumbnailView = itemView.findViewById(R.id.youtube_thumbnail);
        }

        @Override
        public void onClick(View v) {
            if(mVideos.get(getLayoutPosition()).videoId != null) {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, API_KEY, mVideos.get(getLayoutPosition()).videoId);
                context.startActivity(intent);
            } else {
                Toast.makeText(itemView.getContext(), "Error loading video", Toast.LENGTH_LONG).show();
            }
        }
    }
}