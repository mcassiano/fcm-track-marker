package me.cassiano.map_sample;

import rx.Observable;
import rx_fcm.FcmReceiverData;
import rx_fcm.Message;

/**
 * Created by matheus on 08/12/16.
 */
public class AppFcmReceiverData implements FcmReceiverData {
    @Override
    public Observable<Message> onNotification(Observable<Message> oMessage) {
        return oMessage;
    }
}
