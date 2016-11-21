package pl.charmas.android.reactivelocation.mapzen.observables;

import android.content.Context;
import android.os.Bundle;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.Api;
//import com.google.android.gms.common.api.GoogleApiClient;

import com.mapzen.android.lost.api.LostApiClient;

//
//import pl.charmas.android.reactivelocation.observables.GoogleAPIConnectionException;
//import pl.charmas.android.reactivelocation.observables.GoogleAPIConnectionSuspendedException;
import pl.charmas.android.reactivelocation.mapzen.LostAPIConnectionSuspendedException;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;


public abstract class MZBaseObservable<T> implements Observable.OnSubscribe<T> {
    private final Context ctx;

    protected MZBaseObservable(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        final LostApiClient lostApiClient = createApiClient(subscriber);
        lostApiClient.connect();

        final LostApiClient apiClient = createApiClient(subscriber);
        try {
            apiClient.connect();
        } catch (Throwable ex) {
            subscriber.onError(ex);
        }

        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                if (apiClient.isConnected()) {
                    onUnsubscribed(apiClient);
                    apiClient.disconnect();
                }
            }
        }));
    }


    protected LostApiClient createApiClient(Subscriber<? super T> subscriber) {

        ApiClientConnectionCallbacks apiClientConnectionCallbacks = new ApiClientConnectionCallbacks(subscriber);

        LostApiClient.Builder apiClientBuilder = new LostApiClient.Builder(ctx);
        apiClientBuilder.addConnectionCallbacks(apiClientConnectionCallbacks);
        LostApiClient apiClient = apiClientBuilder.build();
        apiClientConnectionCallbacks.setClient(apiClient);
        return apiClient;

    }

    protected void onUnsubscribed(LostApiClient locationClient) {
    }

    protected abstract void onLOstApiClientReady(LostApiClient apiClient, Observer<? super T> observer);

    private class ApiClientConnectionCallbacks implements
            LostApiClient.ConnectionCallbacks {

        final private Observer<? super T> observer;

        private LostApiClient apiClient;

        private ApiClientConnectionCallbacks(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onConnected() {
            try {
                onLOstApiClientReady(apiClient, observer);
            } catch (Throwable ex) {
                observer.onError(ex);
            }
        }

        @Override
        public void onConnectionSuspended() {
            observer.onError(new LostAPIConnectionSuspendedException());
        }
//
//        @Override
//        public void onConnectionFailed(ConnectionResult connectionResult) {
//            observer.onError(new GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult));
//        }

        public void setClient(LostApiClient client) {
            this.apiClient = client;
        }

    }

}
