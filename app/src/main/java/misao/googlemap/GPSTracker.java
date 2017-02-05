package misao.googlemap;


import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by sonu on 05-Feb-17.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    //flag for GPS status
    boolean isGPSEnabled = false;

    //flag for Network Status
    boolean isNetworkEnabled = false;

    //flag for location
    boolean canGetLocation = false;

    Location location;  // for get the current location
    double latitude;    // for store the current latitude value
    double longitude;   // for store the current longitude value

    //The minimum distance to update to changes in meters
    private static final long MIN_DISTANCES_CHANGE_FOR_UPDATE = 10; //HERE 10 IS DENOTE THE 10 METERS

    //The minimum distance to update to changes in minute
    private static final long MIN_TIME_CHANGE_FOR_UPDATE = 1000*60*1;   //THE TIME IS DENOTE THE 1 MINUTES

    //DECLARING A LOCATION MANAGER
    protected LocationManager locationManager;


    public GPSTracker(Context context)
    {
        this.context = context;
        getLocation();
    }

    private Location getLocation() {

        try{
            locationManager  = (LocationManager)context.getSystemService(LOCATION_SERVICE);
            //getting the GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting the NEtwork status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled)
            {
                //do something when GPS and Network not availabe
            }
            else
            {
                this.canGetLocation = true;

                //First get the location form network provider
                if(isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_CHANGE_FOR_UPDATE,MIN_DISTANCES_CHANGE_FOR_UPDATE,this);
                    Log.d("Network","Network is working");

                    if(locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                //Get the location from GPS provider
                if(isGPSEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_CHANGE_FOR_UPDATE,MIN_DISTANCES_CHANGE_FOR_UPDATE,this);
                    Log.d("GPS","GPS is working");

                    if(locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if(location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude()
    {
        if(location != null)
        {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude()
    {
        if(location != null)
        {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    public void showSettingAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("GPS is setting");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to setting menu");

        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
