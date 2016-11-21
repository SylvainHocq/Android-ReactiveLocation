package pl.charmas.android.reactivelocation.mapzen.observables;

import android.content.Context;
import android.location.Location;

//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;

import com.mapzen.android.lost.api.LocationListener;
import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LocationSettingsRequest;
import com.mapzen.android.lost.api.LocationSettingsResult;
import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.android.lost.api.PendingResult;

import java.util.ArrayList;
import java.util.Collection;

import rx.Observable;
import rx.Observer;

public class MZLocationUpdatesObservable extends MZBaseLocationObservable<Location> {

    private static final String TAG = MZLocationUpdatesObservable.class.getSimpleName();

    public static Observable<Location> createObservable(Context ctx, LocationRequest locationRequest) {
        return Observable.create(new MZLocationUpdatesObservable(ctx, locationRequest));
    }

    private final LocationRequest locationRequest;
    private LocationListener locationListener;

    private MZLocationUpdatesObservable(Context ctx, LocationRequest locationRequest) {
        super(ctx);
        this.locationRequest = locationRequest;
    }

//    @Override
//    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Location> observer) {
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                observer.onNext(location);
//            }
//        };
//        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, locationListener);
//    }

//    @Override
//    protected void onUnsubscribed(GoogleApiClient locationClient) {
//        if (locationClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(locationClient, locationListener);
//        }
//    }

    @Override
    protected void onLOstApiClientReady(LostApiClient apiClient, final Observer<? super Location> observer) {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                observer.onNext(location);
            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }
        };

        boolean needBle = false;
        Collection<LocationRequest> locationRequests = new ArrayList<>();
        locationRequests.add(locationRequest);
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addAllLocationRequests(locationRequests)
                .setNeedBle(needBle)
                .build();


        @SuppressWarnings("MissingPermission")
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(apiClient, locationSettingsRequest);


        //noinspection MissingPermission
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, locationListener);
    }
}
