package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StoryListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Story>> {

    public static String storyQueryUrl = null;
    private StoryAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_list_activity);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<Story>> onCreateLoader(int id, Bundle args) {
        return new StoryLoader(StoryListActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Story>> loader, ArrayList<Story> stories) {
        if (stories == null) {
            return;
        }
        ProgressBar loadProgressIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
        loadProgressIndicator.setVisibility(View.GONE);

        TextView emptyListViewText = (TextView) findViewById(R.id.empty_story_list_view);
        emptyListViewText.setVisibility(View.VISIBLE);

        updateUI(stories);
    }

    public void onLoaderReset(Loader<ArrayList<Story>> loader) {

    }

    public void getStoryList(View view) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected == true) {
            ListView storyListView = (ListView) findViewById(R.id.story_list);
            storyListView.setVisibility(View.GONE);
            final TextView noConnectivityView = (TextView) findViewById(R.id.no_connectivity_view);
            noConnectivityView.setVisibility(View.GONE);
            ProgressBar loadProgressIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
            loadProgressIndicator.setVisibility(View.VISIBLE);

            final String BASE_BOOK_QUERY_URL = "https://content.guardianapis.com/search?";
            final String TEST_API_KEY = "test";
            final String SHOW_TAGS = "contributor";
            String contentSearchTerm;

            int searchId = R.id.story_query_text_input;
            EditText searchTermObject = (EditText) findViewById(searchId);
            contentSearchTerm = searchTermObject.getText().toString();
            Uri baseUri = Uri.parse(BASE_BOOK_QUERY_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("q", contentSearchTerm);
            uriBuilder.appendQueryParameter("show-tags", SHOW_TAGS);
            uriBuilder.appendQueryParameter("api-key", TEST_API_KEY);
            storyQueryUrl = uriBuilder.toString();
            getLoaderManager().restartLoader(0, null, this);
        } else {
            final ListView storyListView = (ListView) findViewById(R.id.story_list);
            storyListView.setVisibility(View.GONE);
            final TextView noConnectivityView = (TextView) findViewById(R.id.no_connectivity_view);
            noConnectivityView.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI(ArrayList stories) {
        final ListView storyListView = (ListView) findViewById(R.id.story_list);
        storyListView.setEmptyView(findViewById(R.id.empty_story_list_view));
        itemsAdapter = new StoryAdapter(StoryListActivity.this, stories);
        storyListView.setAdapter(itemsAdapter);
        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story storyItem = itemsAdapter.getItem(position);
                String storyUrl = storyItem.getStoryUrl();
                openWebPage(storyUrl);
            }
        });
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
