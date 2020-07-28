package com.example.youtubesearchclient;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class YoutubeConfig {
    private static final String TAG = YoutubeConfig.class.getSimpleName();
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/search";
    private static final String part = "snippet";
    private static final String order = "ViewCount";
    private static final String query = "Oregon State University";
    private static final String type = "video";
    private static final String API_KEY = "AIzaSyDmewgMhAomWnfke33G_jqjVtnfRx2WzD4";

    static class youtubeSearchResults {
        youtubeListItem[] items;
    }

    static class youtubeListItem {
        youtubeItemID id;
        youtubeItemSnippet snippet;
    }

    static class youtubeItemSnippet {
        String title;
    }

    static class youtubeItemID {
        String videoId;
    }

    public static String buildUrl(String q) {
        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter("part", part)
                .appendQueryParameter("order", order)
                .appendQueryParameter("q", q)
                .appendQueryParameter("type", type)
                .appendQueryParameter("key", API_KEY)
                .build()
                .toString();
    }

    public static ArrayList<YoutubeVideos> parseResults(String json) {
        Gson gson = new Gson();
        youtubeSearchResults results = gson.fromJson(json, youtubeSearchResults.class);
        if (results != null && results.items != null) {
            ArrayList<YoutubeVideos> videos = new ArrayList<>();
            for (youtubeListItem video : results.items) {
                YoutubeVideos youtubeVideo = new YoutubeVideos();
                youtubeVideo.title = video.snippet.title;
                youtubeVideo.videoId = video.id.videoId;
                videos.add(youtubeVideo);
                Log.d(TAG, "Video Title: " + youtubeVideo.title);
                Log.d(TAG, "Video ID: " + youtubeVideo.videoId);
            }
            return videos;
        } else {
            return null;
        }

    }

    public static String getApiKey() {
        return API_KEY;
    }
}
