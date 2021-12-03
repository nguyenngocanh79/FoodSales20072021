package com.example.foodsales20072021;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


import com.example.foodsales20072021.model.Token;
import com.example.foodsales20072021.utils.TokenManager;
import com.example.foodsales20072021.view.activity.HomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
            updateToken(s);
    }

    private void updateToken(String refreshToken){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Lấy UserId từ bộ nhớ
        TokenManager tokenManager = TokenManager.getInstance();
        String userId = tokenManager.getUserId();
        Token token = new Token(refreshToken);
        if(!userId.equals("")){//Nếu còn UserID tức chưa token chưa bị expired
            db.collection("Tokens")
                    .document(userId)
                    .set(token);
        }

    }

    //RemoteMessage chính là biến Data gửi lên
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("BBB","có mail");
//            //Nếu server (bài này là máy tự gửi) gửi Data message thì phải tự tạo Notification
            sendNotification(remoteMessage);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * param messageBody FCM message body received.
     */
    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        //Thay ký tự bằng "" để tạo channel ID
//        String s = user.replaceAll("[\\D]","");
        //Chỉ lấy 4 số đầu vì id là số integer, và phòng trường hợp có ít số trong chuỗi
//        int j = Integer.parseInt(s.substring(0,3));
        int j = new Random().nextInt(Integer.MAX_VALUE);
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.channel_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(Integer.parseInt(icon))
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        int i = 0;
        if(j>0){
            i=j;
        }

        notificationManager.notify(i /* ID of notification */, notificationBuilder.build());
    }
}
