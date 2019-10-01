package com.ue.uebook.Quickblox_Chat.utils.fcm;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.quickblox.messages.services.fcm.QBFcmPushListenerService;
import com.ue.uebook.Quickblox_Chat.App;
import com.ue.uebook.Quickblox_Chat.utils.ActivityLifecycle;
import com.ue.uebook.Quickblox_Chat.utils.NotificationUtils;
import com.ue.uebook.Quickblox_Chat.utils.ui.activity.SplashActivity;
import com.ue.uebook.R;

import java.util.Map;
public class PushListenerService extends QBFcmPushListenerService {
    private static final String TAG = PushListenerService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1;

    protected void showNotification(String message) {
        NotificationUtils.showNotification(App.getInstance(), SplashActivity.class,
                App.getInstance().getString(R.string.notification_title), message,
                R.drawable.applogo, NOTIFICATION_ID);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    @Override
    protected void sendPushMessage(Map data, String from, String message) {
        super.sendPushMessage(data, from, message);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + message);
        if (ActivityLifecycle.getInstance().isBackground()) {
            showNotification(message);
        }
    }
}