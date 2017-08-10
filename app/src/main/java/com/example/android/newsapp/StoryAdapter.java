package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.newsapp.R.id.story_author;
import static com.example.android.newsapp.R.id.story_title;
import static com.example.android.newsapp.R.id.story_section;
import static com.example.android.newsapp.R.id.story_Publication_Date;

/**
 * Created by djp on 8/9/17.
 */

public class StoryAdapter extends ArrayAdapter<Story> {

    public StoryAdapter(Context context, ArrayList<Story> stories){
        super(context, 0, stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;

        if(convertView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.story_list_item, parent, false);
        }

        Story currentStoryObject = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(story_title);
        String mTitle = currentStoryObject.getTitle();
        titleView.setText(mTitle);

        TextView authorView = (TextView) listItemView.findViewById(story_author);
        String mAuthor = currentStoryObject.getAuthor();
        authorView.setText(mAuthor);

        TextView sectionView = (TextView) listItemView.findViewById(story_section);
        String mSection = currentStoryObject.getSectionName();
        sectionView.setText(mSection);

        TextView publicationDateView = (TextView) listItemView.findViewById(story_Publication_Date);
        String mPublicationDate = currentStoryObject.getPublicationDate();
        publicationDateView.setText(mPublicationDate);

        return listItemView;
    }

}
