package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;


import java.util.ArrayList;

import static com.example.android.newsapp.StoryListActivity.storyQueryUrl;

/**
 * Created by djp on 8/9/17.
 */

public class StoryLoader extends AsyncTaskLoader<ArrayList<Story>> {
    public StoryLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Story> loadInBackground() {
        if (storyQueryUrl == null) {
            return null;
        }
        final String storyData = QueryUtils.fetchStoryData(storyQueryUrl);
        return QueryUtils.extractStories(storyData);
    }
}
