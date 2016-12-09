package me.cassiano.map_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdoward.rxgooglemap.MapObservableProvider;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx_fcm.FcmReceiverUIForeground;
import rx_fcm.Message;

public class MainActivity extends AppCompatActivity implements FcmReceiverUIForeground {

    private MapObservableProvider mLocationProvider;
    private PublishSubject<LatLng> mCurrentMarkerPosition = PublishSubject.create();
    private Marker mVehicleMarker;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mLocationProvider = new MapObservableProvider(mapFragment);

        mLocationProvider
                .getMapReadyObservable()
                .flatMap((map) -> {

                    LatLng latLng = new LatLng(-19.918137, -43.986931);

                    MarkerOptions options = new MarkerOptions();
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.taxi);

                    options.position(latLng);
                    options.icon(icon);

                    mGoogleMap = map;
                    mVehicleMarker = map.addMarker(options);

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                    return mCurrentMarkerPosition.asObservable();
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LatLng>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LatLng location) {
                        MapAnimationUtils.animateMarker(location, mVehicleMarker);
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                    }
                });

    }


    @Override
    public void onTargetNotification(Observable<Message> oMessage) {
        oMessage.subscribe(new Observer<Message>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Message message) {
                String latitude = message.payload().getString("lat");
                String longitude = message.payload().getString("long");

                LatLng location = new LatLng(Double.parseDouble(latitude),
                        Double.parseDouble(longitude));

                mCurrentMarkerPosition.onNext(location);
            }
        });
    }

    @Override
    public void onMismatchTargetNotification(Observable<Message> oMessage) {

    }

    @Override
    public boolean matchesTarget(String key) {
        return true;
    }
}
