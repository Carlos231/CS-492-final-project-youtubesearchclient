package com.example.youtubesearchclient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class YoutubeViewModel extends ViewModel {
    private YoutubeRepository mRepository;
    private LiveData<List<YoutubeVideos>> mSearchResults;
    private LiveData<Status> mLoadingStatus;

    public YoutubeViewModel() {
        mRepository = new YoutubeRepository();
        mSearchResults = mRepository.getSearchResults();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadSearchResults(String query) {
        mRepository.loadSearchResults(query);
    }

    public LiveData<List<YoutubeVideos>> getSearchResults() {
        return mSearchResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}
