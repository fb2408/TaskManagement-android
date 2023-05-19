package com.example.testapp.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserApi {

    @GET("worker/change-device-token")
    Call<Void> changeDeviceToken(@Query("id") String id, @Query("deviceToken") String deviceToken);
}
