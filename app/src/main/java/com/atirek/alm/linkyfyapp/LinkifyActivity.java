package com.atirek.alm.linkyfyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class LinkifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkify);

        getIntent();
        //Intent intent = getIntent();
        //String s = intent.getStringExtra("share");
        //onNewIntent(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = new Intent(LinkifyActivity.this, MainActivity.class);
        startActivity(i);

    }

    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String id = data.substring(data.lastIndexOf("/") + 1);

            // Receiving side
            try {
                byte[] data1 = Base64.decode(id, Base64.DEFAULT);
                id = new String(data1, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Log.d("dataId", id);
        }
    }

}
