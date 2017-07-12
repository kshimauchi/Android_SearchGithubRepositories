package com.kshimauchi.myapplication;

import android.net.Uri;
import android.util.Log;

import com.kshimauchi.myapplication.Model.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
            //checking url string
            Log.d(TAG, "url: " + urlString);

        }catch(MalformedURLException e){

            e.printStackTrace();
        }
        return url;
    }
    //make a network call
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try
        {
            InputStream in = urlConnection.getInputStream();

            Scanner input = new Scanner(in);
            //This happens at the begining of the String
            input.useDelimiter("\\A");

            return (input.hasNext()) ? input.next() : null;
            //finally is right!
            }
            finally
            {
                urlConnection.disconnect();
            }
    }
//pass the JSON String and parse and add it to the collection of type Repository
public static ArrayList<Repository> parsingJSON(String json) throws JSONException
{
    ArrayList<Repository> result = new ArrayList<Repository>();
    JSONObject main = new JSONObject(json);
    JSONArray items = main.getJSONArray("items");
    //iterate through the items Array and pullout the contents in string format
    for(int i =0; i < items.length(); i++)
    {
        JSONObject item = items.getJSONObject(i);
        //A single item constits of a name, owner and url as the model suggests in Repository
        String name = item.getString("name"); //name
        JSONObject owner = item.getJSONObject("owner"); // owner
        String ownerName = owner.getString("login");//is the login owner
        String url = item.getString("html_url");
        Log.i(TAG, "name :"+ name);
        Log.i(TAG, "owner OBJ : " + owner.toString());
        Log.i(TAG, "url : " + url );
        //Collect information
        Repository repo = new Repository(name, ownerName, url);
        //add the information to the results arraylist
        result.add(     repo    );
    }
    //return result
    return result;
    }
}
