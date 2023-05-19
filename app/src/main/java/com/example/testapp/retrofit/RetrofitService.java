package com.example.testapp.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    public Retrofit getRetrofit() {
        return retrofit;
    }

     public RetrofitService() {
         initialize();
     }

    private void initialize() {
         retrofit = new Retrofit.Builder()
                 .baseUrl("http://172.20.10.2:8080")
                 .addConverterFactory(GsonConverterFactory.create(new Gson()))
                 .build();
    }

}
