package com.parse.Stalkie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

/**
 * Created by Anisha on 23-10-2015.
 *
 * This activity decides whether to start the logged in activity (Dashboard) or the logged out activity (Welcomescreen or MainActivity).
 */
public class DispatchActivity extends Activity {
    public DispatchActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            startActivity(new Intent(this, DashboardActivity.class));
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}
