package me.cassiano.map_sample;

import rx.Observable;
import rx_fcm.FcmReceiverUIBackground;
import rx_fcm.Message;

/**
 * Created by matheus on 08/12/16.
 */
public class AppFcmReceiverUIBackground implements FcmReceiverUIBackground {
    @Override
    public void onNotification(Observable<Message> oMessage) {

    }
}
