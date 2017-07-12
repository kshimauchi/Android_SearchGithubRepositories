package com.kshimauchi.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import static com.kshimauchi.myapplication.R.id.webview;

/**
 * Created by kshim on 7/11/2017.
 */

public class Web extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*Error was killing application here do to activity being set to main  */
        setContentView(R.layout.activity_web);

        String url ="http://www.google.com";

        Intent intent = getIntent();

        if(intent.hasExtra(MainActivity.URLKEY))
            url = intent.getStringExtra(MainActivity.URLKEY);
        //in the activity web.xml
        WebView webView = (WebView)findViewById(webview);
        //load
        webView.loadUrl(url);

    }
}
