package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class StoryListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Story>> {

    public static String STORY_QUERY_URL = null;
    private ProgressBar mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_list_activity);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<Story>> onCreateLoader(int id, Bundle args){
        return new StoryLoader(StoryListActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Story>> loader, ArrayList<Story> stories){
        if(stories == null){
            return;
        }
        ProgressBar loadProgressIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
        loadProgressIndicator.setVisibility(View.GONE);

        TextView emptyListViewText = (TextView) findViewById(R.id.empty_story_list_view);
        emptyListViewText.setVisibility(View.VISIBLE);

        updateUI(stories);
    }

    public void onLoaderReset(Loader<ArrayList<Story>> loader){

    }

    public void getStoryList(View view){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected == true){
            ListView storyListView = (ListView) findViewById(R.id.story_list);
            storyListView.setVisibility(View.GONE);
            final TextView noConnectivityView = (TextView) findViewById(R.id.no_connectivity_view);
            noConnectivityView.setVisibility(View.GONE);
            ProgressBar loadProgressIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
            loadProgressIndicator.setVisibility(View.VISIBLE);
            mProgress = loadProgressIndicator;

            String BASE_BOOK_QUERY_URL  = "https://content.guardianapis.com/search?q=";
            String testApiKey = "test";
            int searchId = R.id.story_query_text_input;
            EditText searchTermObject = (EditText) findViewById(searchId);
            String searchTermString = searchTermObject.getText().toString();
            String requestUrl = BASE_BOOK_QUERY_URL + searchTermObject;
            STORY_QUERY_URL = requestUrl;
            Uri baseUri = Uri.parse(STORY_QUERY_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("api-key", testApiKey);
            STORY_QUERY_URL = uriBuilder.toString();
            getLoaderManager().restartLoader(0, null, this);
        } else {
            final ListView storyListView = (ListView) findViewById(R.id.story_list);
            storyListView.setVisibility(View.GONE);
            final TextView noConneectivityView = (TextView) findViewById(R.id.no_connectivity_view);
            noConneectivityView.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI(ArrayList stories){
        final ListView storyListView = (ListView) findViewById(R.id.story_list);
        storyListView.setEmptyView(findViewById(R.id.empty_story_list_view));
        final StoryAdapter itemsAdapter = new StoryAdapter(StoryListActivity.this, stories);
        storyListView.setAdapter(itemsAdapter);
    }
}
