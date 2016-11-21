package pl.charmas.android.reactivelocation.mapzen.observables;

import android.content.Context;

import com.google.android.gms.location.LocationServices;


public abstract class MZBaseLocationObservable<T> extends MZBaseObservable<T> {
    protected MZBaseLocationObservable(Context ctx) {
        super(ctx);
    }
}
