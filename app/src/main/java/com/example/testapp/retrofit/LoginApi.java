package com.example.testapp.retrofit;

import com.example.testapp.model.Task;
import com.example.testapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginApi {
    @GET("/login")
    Call<User> login(@Query("email") String email);
}
