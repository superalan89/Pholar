package com.hooooong.pholar.noti;

import android.util.Log;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Android Hong on 2017-11-10.
 */

public class SendNotification {

    public static void sendLikeNotification(String post_id, String nickName, String token) {
        Log.e("sendLikeNotification", "sendLikeNotification");
        Log.e("sendLikeNotification", "nickName : " + nickName);
        Log.e("sendLikeNotification", "token  " + token);

        // Body 설정 + "\", \"imagePath\" : \"" +
        String json = "{\"to\": \"" + token + "\"" +
                ", \"nickName\" : \"" + nickName +"\"" +
                ", \"post_id\" : \"" +post_id +
                "\"}";

        Log.e("sendLikeNotification", "json : " + json);

        // 2. Firebase Function 에서 보내는 경우 application/json
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        // Retrofit 설정
        Retrofit retrofit = new Retrofit
                .Builder()
                // 2. Firebase Function 에서 보내는
                .baseUrl("https://us-central1-pholar-f5bf3.cloudfunctions.net/")
                .build();
        // Interface 결합
        IRetro service = retrofit.create(IRetro.class);

        // Service 로 연결 준비
        Call<ResponseBody> remote = service.sendLikeNotification(body);
        remote.enqueue(new Callback<ResponseBody>() {
                           @Override
                           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                               if (response.isSuccessful()) {
                                   Log.e("SendNotification", "sendLikeNotification 성공");
                               }
                           }

                           @Override
                           public void onFailure(Call<ResponseBody> call, Throwable t) {
                               Log.e("Retro", t.getMessage());
                           }
                       }
        );
    }

    public static void sendCommentNotification(String post_id, String nickName, String token) {
        Log.e("sendCommentNotification", "sendLikeNotification");
        Log.e("sendCommentNotification", "nickName : " + nickName);
        Log.e("sendCommentNotification", "token L " + token);

        // Body 설정 + "\", \"imagePath\" : \"" +
        String json = "{\"to\": \"" + token + "\"" +
                ", \"nickName\" : \"" + nickName +"\"" +
                ", \"post_id\" : \"" +post_id +
                "\"}";

        Log.e("sendCommentNotification", "json : " + json);

        // 2. Firebase Function 에서 보내는 경우 application/json
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        // Retrofit 설정
        Retrofit retrofit = new Retrofit
                .Builder()
                // 2. Firebase Function 에서 보내는
                .baseUrl("https://us-central1-pholar-f5bf3.cloudfunctions.net/")
                .build();
        // Interface 결합
        IRetro service = retrofit.create(IRetro.class);

        // Service 로 연결 준비
        Call<ResponseBody> remote = service.sendCommentNotification(body);
        remote.enqueue(new Callback<ResponseBody>() {
                           @Override
                           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                               if (response.isSuccessful()) {
                                   Log.e("SendNotification", "sendCommentNotification 성공");
                               }
                           }

                           @Override
                           public void onFailure(Call<ResponseBody> call, Throwable t) {
                               Log.e("Retro", t.getMessage());
                           }
                       }
        );



    }
}
