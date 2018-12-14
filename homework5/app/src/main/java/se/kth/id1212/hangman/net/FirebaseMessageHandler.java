package se.kth.id1212.hangman.net;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static se.kth.id1212.hangman.net.FirebaseService.TAG;

public class FirebaseMessageHandler extends FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        this.broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() == null) {
            return;
        }

        Intent intent = new Intent("user_notification");
        intent.putExtra("message", remoteMessage.getNotification().getBody());
        this.broadcaster.sendBroadcast(intent);

        Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

    }
}
