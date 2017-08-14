package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    //an empty constructor makes sure that the class is not going to be initialized
    private QueryUtils() {

    }


    public static String fetchStoryData(String requestURL) {
        URL url = createUrl(requestURL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error fetching story data ", e);
        }
        return jsonResponse;
    }

    public static URL createUrl(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        int readTimeout = 5000;
        int connectTimeout = 10000;

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the story JSON results, ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<Story> extractStories(String storyData) {
        Log.v("QueryUtils", "entered extractStories");
        ArrayList<Story> stories = new ArrayList<>();

        try {
            JSONObject storyObject = new JSONObject(storyData);
            Log.v("QueryUtils", " \n\n\n value of storyObject in extract Stories   " + storyObject);
            JSONObject responseObject = storyObject.getJSONObject("response");
            Log.v("QueryUtils", " \n\n\n value of responseObject    " + responseObject);
            JSONArray items = responseObject.getJSONArray("results");
            Log.v("QueryUtils", " \n\n\n ITEMS ITEMS    ITEMS    ITEMS     ITEMS     ITEMS     ITEMS    ITEMS  " + items);
            String authorString = "";
            String sectionNameString = "";
            String titleString = "";
            String publicationDateString = "";
            String storyUrlString = "";
            JSONObject tagsObject;

            for (int i = 0; i < items.length(); i++) {
                JSONObject itemsObject = items.getJSONObject(i);
                JSONArray tagsArray = itemsObject.getJSONArray("tags");
                Log.v("QueryUtils", " \n\n\n\n value of tags array:  " + tagsArray + " \n\n\n");
                if(tagsArray.length() >= 1) {
                    tagsObject = tagsArray.getJSONObject(0);
                } else {
                    tagsObject = new JSONObject();
                }

                if (!itemsObject.has("webTitle")) {
                    titleString = "No Title Listed";
                } else {
                    titleString = itemsObject.getString("webTitle");
                }

                if (!itemsObject.has("sectionId")) {
                    sectionNameString = "No Section Listed";
                } else {
                    sectionNameString = itemsObject.getString("sectionId");
                }
                if (!itemsObject.has("webUrl")) {
                    storyUrlString = "No Story URL Listed";
                }else {
                    storyUrlString = itemsObject.getString("webUrl");
                }
                if(!itemsObject.has("webPublicationDate")){
                    publicationDateString = "No publication date Listed";
                } else {
                    publicationDateString = itemsObject.getString("webPublicationDate").substring(0, 10);
                }
                if(!tagsObject.has("webTitle")){
                    authorString = "No author listed";
                } else {
                    authorString = tagsObject.getString("webTitle");
                }


                stories.add(new Story(authorString, sectionNameString, titleString, publicationDateString, storyUrlString ));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the story JSON results", e);
        }
        Log.v("QueryUtils", " \n\n\n value of stories:  " + stories);

        return stories;
    }

}
