package com.example.youtubesearchclient;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class YoutubeRepository implements YoutubeAsyncTask.Callback {
    private static final String TAG = YoutubeRepository.class.getSimpleName();
    private MutableLiveData<List<YoutubeVideos>> mResults;
    private MutableLiveData<Status> mLoadingStatus;
    private String mCurrentQuery;

    public YoutubeRepository() {
        mResults = new MutableLiveData<>();
        mResults.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);

        mCurrentQuery = null;
    }

    public LiveData<List<YoutubeVideos>> getSearchResults() {
        return mResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    @Override
    public void onSearchFinished(List<YoutubeVideos> searchResults) {
        mResults.setValue(searchResults);
        if(searchResults != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    private boolean shouldExecuteSearch(String query) {
        return !TextUtils.equals(query, mCurrentQuery);
    }

    public void loadSearchResults(String query) {
        if (shouldExecuteSearch(query)) {
            String url;
//            if (query.equals("trending")){
////                https://www.googleapis.com/youtube/v3/videos?part=contentDetails&chart=mostPopular&regionCode=IN&key=AIzaSyDmewgMhAomWnfke33G_jqjVtnfRx2WzD4
////                https://www.googleapis.com/youtube/v3/videos?part=contentDetails&chart=mostPopular&regionCode=IN&key=AIzaSyDmewgMhAomWnfke33G_jqjVtnfRx2WzD4
//
//                url = Uri.parse("https://www.googleapis.com/youtube/v3/videos").buildUpon()
//                        .appendQueryParameter("part", "contentDetails")
//                        .appendQueryParameter("chart", "mostPopular")
//                        .appendQueryParameter("regionCode", "IN")
//                        .appendQueryParameter("key", "AIzaSyDmewgMhAomWnfke33G_jqjVtnfRx2WzD4")
//                        .build()
//                        .toString();
//
//                mResults.setValue(null);
//                mLoadingStatus.setValue(Status.LOADING);
//                new YoutubeAsyncTask(this).execute(url);
//            } else {
                mCurrentQuery = query;
                url = YoutubeConfig.buildUrl(query);
                mResults.setValue(null);
                mLoadingStatus.setValue(Status.LOADING);
                new YoutubeAsyncTask(this).execute(url);
//            }
//            Log.d(TAG, url);
        } else {
            Log.d(TAG, "using cached search results");
        }
    }
}
