package com.parse.Stalkie;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Dashboard extends AppCompatActivity implements LocationListener {

    private Toolbar toolbar;

    TextView mUserLocation;

    LocationManager mLocationManager;

    protected static final String TAG = "Dashboard";

    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mUserLocation = (TextView) findViewById(R.id.user_location);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                try {
                    Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    mUserLocation.setText(ParseUser.getCurrentUser().getUsername()
                            + "\nLatitude: " + lastKnownLocation.getLatitude()
                            + "\nLongitude: " + lastKnownLocation.getLongitude());
                } catch (Exception e) {
                    mUserLocation.setText(ParseUser.getCurrentUser().getUsername()
                            + "\nNo last known location." + "\nWaiting for a location update.");
                }

                // Location updates in
                // 1000 milliseconds, 60 seconds, 1 minute, 0m distance
                // The number you multiply to 1000 * 60 will be the number of minutes before
                // the app requests a location update
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 0, Dashboard.this);
            }
        } else {
            try {
                Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mUserLocation.setText(ParseUser.getCurrentUser().getUsername()
                        + "\nLatitude: " + lastKnownLocation.getLatitude()
                        + "\nLongitude: " + lastKnownLocation.getLongitude());
            } catch (Exception e) {
                mUserLocation.setText(ParseUser.getCurrentUser().getUsername()
                        + "\nNo last known location." + "\nWaiting for a location update.");
            }

            // Location updates in
            // 1000 milliseconds, 60 seconds, 1 minute, 0m distance
            // The number you multiply to 1000 * 60 will be the number of minutes before
            // the app requests a location update
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 0, Dashboard.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard,menu);

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
            Toast.makeText(getApplicationContext(), "Settings icon",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_add) {
//            Toast.makeText(getApplicationContext(), "Add icon",
//                    Toast.LENGTH_SHORT).show();
            supportInvalidateOptionsMenu();

            Intent intent = new Intent(Dashboard.this, CreateMeetingActivity.class);
            startActivity(intent);

        }
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(Dashboard.this, DispatchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
//        menu.removeItem(R.id.action_add);
//        menu.removeItem(R.id.action_settings);
        return true;
    }

    @Override
    public void onLocationChanged(final Location location) {
        if(ParseUser.getCurrentUser() != null) {
            mUserLocation = (TextView) findViewById(R.id.user_location);
            mUserLocation.setText(ParseUser.getCurrentUser().getUsername()
                    + "\nLatitude: " + location.getLatitude()
                    + "\nLongitude: " + location.getLongitude());

            ParseQuery<ParseObject> query = ParseQuery.getQuery("LocationTable");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> users, ParseException e) {
                    if (e == null) {
                        if(users.size() > 0) {
                            // Already existing users with the username
                            // Find and update the existing record
                            ParseObject retrievedUser = users.get(0);
                            ParseQuery<ParseObject> updateQuery = ParseQuery.getQuery("LocationTable");
                            updateQuery.getInBackground(retrievedUser.getObjectId(), new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                                        object.put("location", point);
                                        object.saveInBackground();
                                    } else {
                                        Log.e(TAG, "Error in retrieving record for update.");
                                    }
                                }
                            });
                        } else {
                            // No users exist with the user name
                            // Create a new record
                            ParseObject locationObject = new ParseObject("LocationTable");
                            ParseACL locationACL = new ParseACL();
                            locationACL.setWriteAccess(ParseUser.getCurrentUser(), true);
                            locationACL.setReadAccess(ParseUser.getCurrentUser(), true);
                            ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                            locationObject.put("username", ParseUser.getCurrentUser().getUsername());
                            locationObject.put("location", point);
                            locationObject.saveInBackground();
                        }
                    } else {
                        Log.e(TAG, "Error in username search query.");
                    }
                }
            });
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "Status of location provider has changed.");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "Location Provider Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Prompt user to enable GPS settings
        Log.i(TAG, "Location Provider Disabled");
        Toast.makeText(Dashboard.this, "GPS disabled", Toast.LENGTH_SHORT).show();
        Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }
}
