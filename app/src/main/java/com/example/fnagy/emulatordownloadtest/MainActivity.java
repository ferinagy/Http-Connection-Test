package com.example.fnagy.emulatordownloadtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HttpConnectionTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        download();
                    }
                }).start();
            }
        });
    }

    private void download() {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL("http://ovh.net/files/1Mb.dat").openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            final int responseCode = conn.getResponseCode();
            Log.d(TAG, "downloading, response code: " + responseCode);

            final InputStream in = conn.getInputStream();
            final int contentLength = conn.getContentLength();
            Log.d(TAG, "content length: " + contentLength);
            final byte buffer[] = new byte[8196];
            int total = 0;

            while (true) {
                int len;
                try {
                    len = in.read(buffer);
                } catch (IOException e) {
                    Log.w(TAG, "caught exception reading: " + e.toString(), e);
                    break;
                }
                if (len == -1) {
                    Log.d(TAG, "reached end of stream");
                    break;
                }
                total += len;
            }
            Log.d(TAG, "received: " + total);

            if (total != contentLength) {
                Log.e(TAG, "received bytes (" + total + ") differ from content length (" + contentLength + "), missed " + (contentLength - total) + " bytes");
            } else {
                Log.i(TAG, "received all bytes successfully");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
