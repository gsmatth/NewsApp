package com.example.android.newsapp;

/**
 * Created by djp on 8/9/17.
 */

public class Story {


    private String mAuthor;
    private String mSectionName;
    private String mTitle;
    private String mPublicationDate;
    private String mStoryUrl;

    public Story(String author, String sectionName, String title, String publicationDate, String storyUrl) {

        mAuthor = author;
        mSectionName = sectionName;
        mTitle = title;
        mPublicationDate = publicationDate;
        mStoryUrl = storyUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getStoryUrl() {
        return mStoryUrl;
    }

    @Override
    public String toString() {
        return "Story{ " + "mAuthor= " + mAuthor + " " +
                "mSectionName= " + mSectionName + " " +
                "mTitle= " + mTitle + " " +
                "mPublicationDate= " + mPublicationDate + " " +
                "mStoryUrl= " + mStoryUrl + " " +
                '}';
    }
}