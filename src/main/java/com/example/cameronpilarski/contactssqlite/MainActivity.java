package com.example.cameronpilarski.contactssqlite;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


// Created by cameronpilarski on 11/30/17.
// this activity acts as the splash screen activity

public class MainActivity extends AppCompatActivity {

    private SystemUIManager system_ui_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide bottom bar
        system_ui_manager = new SystemUIManager(this);
        system_ui_manager.hideView();

        // create new intent
        final Intent myIntent = new Intent(this, HomeActivity.class);

        // new runnable
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(myIntent);
                finish();
            }
            // splashscreen will show for 2s before moving to next activity
        }, 1500);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
