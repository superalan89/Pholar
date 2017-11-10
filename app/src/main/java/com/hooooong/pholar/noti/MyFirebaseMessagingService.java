package com.hooooong.pholar.noti;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hooooong.pholar.R;
import com.hooooong.pholar.view.home.DetailActivity;

/**
 * Created by Android Hong on 2017-10-31.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MsgService";

    /**
     * 내 앱이 화면에 현재 떠있으면 Notification이 전송되었을 때 이 함수가 호출된다.
     *
     * @param remoteMessage remoteMessage 에는 server 의 data 값이 들어오게 된다.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            // 여기서 notification 메세지를 받아 처리
            // getData() 는 Map 형식이기 때문에 key 값을 알아야 사용할 수 있다.
            sendNotification(remoteMessage);
        }

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
    // [END receive_message]


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message body received.
     */
    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("post_id", remoteMessage.getData().get("post_id"));

        String flag = remoteMessage.getData().get("flag");
        intent.putExtra("flag", flag);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 555, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "DEFAULT CHANNEL";
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.pholar_icon)
                        .setContentTitle("PHOLAR")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
        if ("detail".equals(flag)) {
            notificationBuilder.setContentText(remoteMessage.getData().get("nickName") + "님이 회원님의 포스팅에 좋아요를 눌렀습니다.");
        } else if ("comment".equals(flag)) {
            notificationBuilder.setContentText(remoteMessage.getData().get("nickName") + "님이 회원님의 포스팅에 댓글을 달았습니다.");
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}