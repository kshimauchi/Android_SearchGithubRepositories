package com.kshimauchi.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.kshimauchi.myapplication.Model.Repository;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Repository>>{
    static final String TAG = "MainActivity";
    private ProgressBar progress;
    private EditText search;
    //private TextView textView;
    //replacement with recycler
    private RecyclerView recView;
    public final static String URLKEY ="url";
    private static final int GITHUB_SEARCH_LOADER =1;
    public final static String SEARCH_QUERY_EXTRA ="query";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        search = (EditText) findViewById(R.id.searchQuery);
        //textView = (TextView) findViewById(R.id.displayJSON);
        //add to the activity main xml
        recView = (RecyclerView)findViewById(R.id.recyclerView);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();

        if (itemNumber == R.id.search) {
            /*Execute asynctask here */
            String s = search.getText().toString();
            Bundle qryBudle = new Bundle();
            qryBudle.putString(SEARCH_QUERY_EXTRA, s);
            LoaderManager loaderManager = getSupportLoaderManager();
            //
            Loader<ArrayList<Repository>> loader = loaderManager.getLoader(GITHUB_SEARCH_LOADER);
            //
            if (loader == null) {
                //we pass three arguements to initialize
                loaderManager.initLoader(GITHUB_SEARCH_LOADER, qryBudle, this).forceLoad();
            } else {
                loaderManager.restartLoader(GITHUB_SEARCH_LOADER, qryBudle, this);
            }
        }
        return true;
    }
    @Override
    public Loader<ArrayList<Repository>> onCreateLoader(int id, final Bundle args){
        return new AsyncTaskLoader<ArrayList<Repository>>(this) {
            @Override
            protected  void onStartLoading(){
            super.onStartLoading();
                if(args == null) return;
                //make visible on loading
                progress.setVisibility(View.VISIBLE);
            }
            @Override
            public ArrayList<Repository> loadInBackground() {
                String query = args.getString(SEARCH_QUERY_EXTRA);
                if(query == null || TextUtils.isEmpty(query))
                return null;

                ArrayList<Repository> result = null;

                URL url = NetworkUtil.makeURL(query, "stars");

                Log.i(TAG, "url " + url.toString());

                try{
                    String json = NetworkUtil.getResponseFromHttpUrl(url);
                    result = NetworkUtil.parsingJSON(json);
                    Log.d(TAG, "parsed: " +result);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch(JSONException e){
                    e.printStackTrace();
                    return null;
                }
                return result;
            }
        };
    }
    @Override
    public void onLoadFinished(Loader<ArrayList<Repository>> loader, final ArrayList<Repository> data){
    progress.setVisibility(View.GONE);
       
        if(null != data){
            Adapter adapter = new Adapter(data, new Adapter.itemClickListener() {
                @Override
                public void onItemClick(int clickedItemIndex) {
                String url = data.get(clickedItemIndex).getUrl();
                String.format("Url%s", url);
                    Intent browserIntent =
                            //new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            //browserIntent.putExtra(URLKEY, url);
                    new Intent(MainActivity.this,Web.class  );
                    Intent intent = browserIntent.putExtra(URLKEY, url);
                    startActivity(browserIntent);


                    Log.d(TAG,String.format("URL %s", url ));
                    //Intent intent = new Intent(MainActivity.this, Web.class);
                    }
                });
            recView.setAdapter(adapter);
            }
        }
        @Override
    public  void onLoaderReset(Loader<ArrayList<Repository>> loader){
            progress.setVisibility(View.INVISIBLE);
        }
    //Currently not being used
    }
