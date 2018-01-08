package com.example.cameronpilarski.contactssqlite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cameronpilarski on 12/2/17.
 *
 * This activity acts as a full screen mode for the profile image
 *
 */

public class FullScreenActivity extends Activity {

    private SystemUIManager system_ui_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen);

        // hide bottom bar
        system_ui_manager = new SystemUIManager(this);
        system_ui_manager.hideView();

        // create variable for picture
        CircleImageView profileImage = (CircleImageView) findViewById(R.id.profile_image_full_screen);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when clicked, go back to contact
                //Intent myIntent = new Intent(FullScreenActivity.this, ContactActivity.class);
                //startActivity(myIntent);
                // use onBackPressed for now because we want to keep same contact info
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
