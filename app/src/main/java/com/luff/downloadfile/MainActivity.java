package com.luff.downloadfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mRssFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRssFeed = (TextView) findViewById(R.id.rss_feed);
//        mRssFeed.setText("abc");
        new HTTPDownloadTask().execute("http://www.24h.com.vn/upload/rss/thoitrang.rss",
                "http://www.24h.com.vn/upload/rss/anninhhinhsu.rss",
                "http://www.24h.com.vn/upload/rss/taichinhbatdongsan.rss",
                "http://www.24h.com.vn/upload/rss/giaoducduhoc.rss");
    }

    private class HTTPDownloadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String urlStr = params[3];
            InputStream is = null;
            String result = "";
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10*1000);
                connection.setConnectTimeout(10*1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("debug", "The response is: " + response);
                is = connection.getInputStream();

                //read string
                final int bufferSize = 1024;
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                while(true) {
                    int count = is.read(buffer, 0, bufferSize);
                    if(count == -1) {
                        break;
                    }

                    os.write(buffer);
                }
                os.close();

                result = new String(os.toByteArray(), "UTF-8");
                //mRssFeed.setText("deno");
                Log.d("debug1", result);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mRssFeed.setText(result);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
}
