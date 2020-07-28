package com.example.youtubesearchclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ImageDecoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerAdapter mAdapter;
    private ProgressBar mLoadingIndicator;
    private ArrayList<YoutubeVideos> mResults;
    private YoutubeViewModel mViewModel;
    Preferences theme;

    private final SharedPreferences.OnSharedPreferenceChangeListener mPrefListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    doYoutubeSearch();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(mPrefListener);

        theme.setApp_Theme("true");
        mResults = null;
        //toolbar
        getSupportActionBar().setElevation(0);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(mAdapter);
        prefValues();

        mViewModel = new ViewModelProvider(this).get(YoutubeViewModel.class);

        mViewModel.getSearchResults().observe(this, new Observer<List<YoutubeVideos>>() {
            @Override
            public void onChanged(List<YoutubeVideos> youtubeVideos) {
                mAdapter.updateResults(youtubeVideos);
            }
        });

        mViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                }
            }
        });

        doYoutubeSearch();
    }

    public void prefValues() {
        //get saved preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String size = preferences.getString(
                getString(R.string.pref_size_key),
                getString(R.string.pref_size_default)
        );
        String show = preferences.getString(
                getString(R.string.pref_show_key),
                getString(R.string.pref_show_default)
        );
        String user = preferences.getString(
                getString(R.string.pref_user_key),
                ""
        );

        Preferences.setVideo_Size(size);
//        mViewModel.loadSearchResults(size, show, user);
        Log.d(TAG, Preferences.getVideo_Size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareForecast();
                Log.v(TAG, "Share");
                return true;
            case R.id.action_settings:
                Log.v(TAG, "Settings");
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doYoutubeSearch() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String search = preferences.getString(getString(R.string.pref_search_key), getString(R.string.pref_search_default));
        String pref = preferences.getString(getString(R.string.pref_show_key), getString(R.string.pref_show_default));

        Log.v(TAG, pref);

        if (pref.equals("trending")){
            mViewModel.loadSearchResults("trending");
        } else {
            mViewModel.loadSearchResults(search);
        }
    }

    public void shareForecast() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String search = preferences.getString(getString(R.string.pref_search_key), getString(R.string.pref_search_default));

            String shareText = "Check out this search on Youtube! Here is the link: https://www.youtube.com/results?search_query=" + search;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(shareIntent, null);
            startActivity(chooserIntent);
    }
}