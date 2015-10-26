package com.parse.Stalkie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.Stalkie.R;

import java.util.Arrays;
import java.util.List;

public class ViewLocation extends AppCompatActivity {

    TextView userLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        supportInvalidateOptionsMenu();

        userLocations = (TextView) findViewById(R.id.userLocations);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("LocationTable");
        String[] names = {"ananya@test.com", "test@test.com", "abcd@test.com"};
        query.whereContainedIn("username", Arrays.asList(names));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locationList, ParseException e) {
                if (e == null) {
                    String temp = " ";
                    if(locationList != null && locationList.size() > 0) {
                        for(int i = 0; i < locationList.size(); i++) {
                            ParseGeoPoint point = null;
                            ParseObject location = locationList.get(i);
                            point = (ParseGeoPoint) location.get("location");
                            temp = temp + ((String) location.get("username")).split("@")[0]
                                    + ": " + point.getLatitude()
                                    + ", " + point.getLongitude()
                                    + "\n ";
                        }
                    }
                    userLocations.setText(temp);
                } else {
                    userLocations.setText("Oops! Something went wrongs in accessing the database.");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_location, menu);
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
        if (id == R.id.action_back) {
            supportInvalidateOptionsMenu();

            Intent intent = new Intent(ViewLocation.this, MeetingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
