package com.kshimauchi.myapplication;

import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil
{
    /************************************************************************/
    public static final String GITHUB_BASE_URL= "https://api.github.com/search/repositories";
    public static final String PARAM_QUERY = "q";
    public static final String PARAM_SORT ="sort";
    public static final String TAG="";
    //https://api.github.com/search/repositories?q=android&sort=stars
    /************************************************************************/
//we need to have
    public static URL makeURL(String searchQuery, String sortBy)
    {
        Uri uri = Uri.parse(GITHUB_BASE_URL).buildUpon()

                .appendQueryParameter(PARAM_QUERY, searchQuery)

                .appendQueryParameter(PARAM_SORT, sortBy).build();

        URL url = null;

        try
            {
                String urlString = uri.toString();

                url = new URL(uri.toString());

                Log.d(TAG, "url: " + urlString);

        }catch(MalformedURLException e){

            e.printStackTrace();
        }
        return url;
    }
    //make a network call
    public static String getResponseFromHttpUrl(URL url) throws IOException{

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try
            {
                InputStream in = urlConnection.getInputStream();

                Scanner input = new Scanner(in);
               //This happens at the begining of the String
                input.useDelimiter("\\A");

       return (input.hasNext()) ? input.next() : null;

        }       finally     {

            urlConnection.disconnect();
        }
    }
}
