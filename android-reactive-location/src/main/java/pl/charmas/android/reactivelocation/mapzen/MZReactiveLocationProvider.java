package pl.charmas.android.reactivelocation.mapzen;

import android.content.Context;
import android.location.Location;
import android.support.annotation.RequiresPermission;

//import com.google.android.gms.common.api.Api;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.common.api.Result;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.ActivityRecognitionResult;
//import com.google.android.gms.location.GeofencingRequest;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResult;
//import com.google.android.gms.location.places.AutocompleteFilter;
//import com.google.android.gms.location.places.AutocompletePredictionBuffer;
//import com.google.android.gms.location.places.PlaceBuffer;
//import com.google.android.gms.location.places.PlaceFilter;
//import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
//import com.google.android.gms.location.places.PlacePhotoMetadata;
//import com.google.android.gms.location.places.PlacePhotoMetadataResult;
//import com.google.android.gms.location.places.PlacePhotoResult;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.maps.model.LatLngBounds;

import com.mapzen.android.lost.api.LocationRequest;

import pl.charmas.android.reactivelocation.observables.location.LastKnownLocationObservable;
import pl.charmas.android.reactivelocation.observables.location.LocationUpdatesObservable;
import rx.Observable;

/**
 * Factory of observables that can manipulate location
 * delivered by Google Play Services.
 */
public class MZReactiveLocationProvider {
    private final Context ctx;

    public MZReactiveLocationProvider(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Creates observable that obtains last known location and than completes.
     * Delivered location is never null - when it is unavailable Observable completes without emitting
     * any value.
     * <p/>
     * Observable can report {@link pl.charmas.android.reactivelocation.observables.GoogleAPIConnectionException}
     * when there are trouble connecting with Google Play Services and other exceptions that
     * can be thrown on {@link com.google.android.gms.location.FusedLocationProviderApi#getLastLocation(GoogleApiClient)}.
     * Everything is delivered by {@link rx.Observer#onError(Throwable)}.
     *
     * @return observable that serves last know location
     */
    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    public Observable<Location> getLastKnownLocation() {
        return LastKnownLocationObservable.createObservable(ctx);
    }

    /**
     * Creates observable that allows to observe infinite stream of location updates.
     * To stop the stream you have to unsubscribe from observable - location updates are
     * then disconnected.
     * <p/>
     * Observable can report {@link pl.charmas.android.reactivelocation.observables.GoogleAPIConnectionException}
     * when there are trouble connecting with Google Play Services and other exceptions that
     * can be thrown on {@link com.google.android.gms.location.FusedLocationProviderApi#requestLocationUpdates(GoogleApiClient, LocationRequest, com.google.android.gms.location.LocationListener)}.
     * Everything is delivered by {@link rx.Observer#onError(Throwable)}.
     *
     * @param locationRequest request object with info about what kind of location you need
     * @return observable that serves infinite stream of location updates
     */
    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    public Observable<Location> getUpdatedLocation(LocationRequest locationRequest) {
        return LocationUpdatesObservable.createObservable(ctx, locationRequest);
    }



    /**
     * Observable that emits {@link GoogleApiClient} object after connection.
     * In case of error {@link pl.charmas.android.reactivelocation.observables.GoogleAPIConnectionException} is emmited.
     * When connection to Google Play Services is suspended {@link pl.charmas.android.reactivelocation.observables.GoogleAPIConnectionSuspendedException}
     * is emitted as error.
     * Do not disconnect from apis client manually - just unsubscribe.
     *
     * @param apis collection of apis to connect to
     * @return observable that emits apis client after successful connection
     */
//    public Observable<GoogleApiClient> getGoogleApiClientObservable(Api... apis) {
//        //noinspection unchecked
//        return GoogleAPIClientObservable.create(ctx, apis);
//    }

    /**
     * Util method that wraps {@link PendingResult} in Observable.
     *
     * @param result pending result to wrap
     * @param <T>    parameter type of result
     * @return observable that emits pending result and completes
     */
//    public static <T extends Result> Observable<T> fromPendingResult(PendingResult<T> result) {
//        return Observable.create(new PendingResultObservable<>(result));
//    }
}
