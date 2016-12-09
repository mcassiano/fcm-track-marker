package me.cassiano.map_sample;

import android.app.Application;

import rx_fcm.internal.RxFcm;

/**
 * Created by matheus on 08/12/16.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RxFcm.Notifications.init(this, new AppFcmReceiverData(), new AppFcmReceiverUIBackground());
    }
}
