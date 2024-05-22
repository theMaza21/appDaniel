package com.daniel.appdaniel.retrofit;

import com.daniel.appdaniel.models.FCMBody;
import com.daniel.appdaniel.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi
{
    @Headers({
            "Content-Type:applicaton/json",
            "Authorization:key = AAAA044M_HA:APA91bHkHNSfmXlUz_3R9wYh6IhbGbkBcuN2zF6sxLLgK9Ug-aIXbdvMEfPs7iy6-OqLXY84fl1_7Zm0-TfMbDeeAEMlRrIr31HQRtM14W_JPM9y-r7MR7n7TF2OK1wmDnXDbp9xudUi"

            })
    @POST("fcm/send")
    Call<FCMResponse>send(@Body FCMBody body);
}
