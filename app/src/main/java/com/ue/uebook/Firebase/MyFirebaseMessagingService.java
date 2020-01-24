package com.ue.uebook.Firebase;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ue.uebook.R;
import com.ue.uebook.SplashActivity.SplashScreenApp;
import com.ue.uebook.VideoSdk.VideoCallRecive;
import com.ue.uebook.VoiceCall.VoiceCallReceive;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String name,noti_msz,user_id,Avtar,channel_id;
    Bitmap bmp = null;
    private static  int value=0;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        componentInfo.getPackageName();

        if (remoteMessage.getData().size() > 0) {
            Log.d("RedListed", "Message data payload: " + remoteMessage.getData());
            Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "I will be sent!");
            this.sendBroadcast(i);
            try {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData());
                    name=jsonObject.getString("UserName");
                noti_msz =jsonObject.getString("noti_msg");
                user_id=jsonObject.getString("user_id");
                Avtar =jsonObject.getString("Avtar");
                channel_id =jsonObject.getString("channel_id");
                Log.d("channel_id",channel_id);
//                if (taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("com.ue.uebook.ChatSdk.MessageScreen"))
//                {
//
//                }
                 if (noti_msz.equalsIgnoreCase("videoCall")){
                           Intent intent = new Intent(this, VideoCallRecive.class);
                    intent.putExtra("id",channel_id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else   if (noti_msz.equalsIgnoreCase("audioCall")){
                     Intent intent = new Intent(this, VoiceCallReceive.class);
                     intent.putExtra("id",channel_id);
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(intent);
                }

                else
                    {

                     if (taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("com.ue.uebook.ChatSdk.MessageScreen")){

                     }

                     else
                         {

                         sendNotification(noti_msz,getBitmapfromUrl("dnddemo.com\\/ebooks\\/api\\/v1\\/upload\\/books\\/book_1571738214.jpg"));
                     }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        try {
            InputStream in = new URL("http://dnddemo.com/ebooks/api/v1/upload/"+"book_1571040310.jpg").openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.

            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]



    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

    }
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     */
    private void sendNotification(String msg ,Bitmap imageurl) {
        value++;
        Intent intent = new Intent(this, SplashScreenApp.class);
        intent.putExtra("sendTo",user_id);
        intent.putExtra("channel_id",channel_id);
        intent.putExtra("name",name);
        intent.putExtra("imageUrl",Avtar);
        intent.putExtra("id",1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int randomRequestCode = new Random().nextInt(54325);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, randomRequestCode /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.applogo)
                        .setContentTitle(name)
                        .setContentText(msg)
                        .setAutoCancel(true)
                       // .setLargeIcon(imageurl)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);// Since android Oreo notification channel is needed.
        createNotificationChannel();

        notificationManager.notify(value /* ID of notification */, notificationBuilder.build());
    }
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        String channelId = getString(R.string.app_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}