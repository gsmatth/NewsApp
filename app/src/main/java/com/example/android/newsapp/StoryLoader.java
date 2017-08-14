package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import static com.example.android.newsapp.StoryListActivity.STORY_QUERY_URL;

/**
 * Created by djp on 8/9/17.
 */

public class StoryLoader extends AsyncTaskLoader<ArrayList<Story>> {
    public StoryLoader(Context context)
        {super(context);
        }

    @Override
    protected void onStartLoading(){

        Log.v("StoryLoader", "  OnStart Loading and forceload() executing");
        forceLoad();
    }

    @Override
    public ArrayList<Story> loadInBackground(){
        if(STORY_QUERY_URL == null){
            Log.v("StoryLoader", "  story query url is null");
            return null;
        }
        Log.v("StoryLoader", "value of story query url  " + STORY_QUERY_URL);
        final String storyData = QueryUtils.fetchStoryData(STORY_QUERY_URL);
        Log.v("StoryLoader", "value of storyData:   " + storyData);
        return QueryUtils.extractStories(storyData);
    }
}
