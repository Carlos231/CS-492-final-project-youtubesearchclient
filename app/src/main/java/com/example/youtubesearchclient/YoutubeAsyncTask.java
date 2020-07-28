package com.example.youtubesearchclient;

import android.os.AsyncTask;
import android.telecom.Call;

import java.io.IOException;
import java.util.List;


public class YoutubeAsyncTask extends AsyncTask<String, Void, String> {

    private Callback mCallback;

        public interface Callback {
            void onSearchFinished(List<YoutubeVideos> searchResults);
        }

    public YoutubeAsyncTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String searchResults = null;
        try {
            searchResults = NetworkUtils.doHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    @Override
    protected void onPostExecute(String s) {
            List<YoutubeVideos> searchResults = null;
            if(s != null) {
                searchResults = YoutubeConfig.parseResults(s);
            }
            mCallback.onSearchFinished(searchResults);
    }
}
