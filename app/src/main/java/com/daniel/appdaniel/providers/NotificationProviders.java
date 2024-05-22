package com.daniel.appdaniel.providers;

import com.daniel.appdaniel.models.FCMBody;
import com.daniel.appdaniel.models.FCMResponse;
import com.daniel.appdaniel.retrofit.IFCMApi;
import com.daniel.appdaniel.retrofit.RetrofitClinet;

import retrofit2.Call;

public class NotificationProviders
{
    private String url = "https://fcm.googleapis.com";
    public NotificationProviders()
    {
    }
    public Call<FCMResponse> sendNotification(FCMBody body)
    {
        return RetrofitClinet.getClint(url).create(IFCMApi.class).send(body);
    }
}
