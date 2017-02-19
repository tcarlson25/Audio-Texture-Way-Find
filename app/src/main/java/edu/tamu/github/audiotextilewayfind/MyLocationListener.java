package edu.tamu.github.audiotextilewayfind;

/**
 * Created by Mikw on 2/17/2017.
 */
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.ImageButton;

public class MyLocationListener implements LocationListener {
    //Saves the last location that the GPS has gotten
    private static Location myLocation;

    @Override
    //Updates the location value when the GPS location changes
    public void onLocationChanged(Location location) {
        myLocation = location;
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static Location getLocation()
    {
        return myLocation;
    }
}

