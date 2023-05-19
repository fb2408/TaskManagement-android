package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.model.User;
import com.example.testapp.retrofit.LoginApi;
import com.example.testapp.retrofit.RetrofitService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final int SPLASH_SCREEN_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser == null) {
                    Intent i1 = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i1);
                    finish();
                } else {
                    RetrofitService retrofit = new RetrofitService();
                    LoginApi loginApi = retrofit.getRetrofit().create(LoginApi.class);
                    loginApi.login(currentUser.getEmail()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            if(user != null) {
                                i.putExtra("id", user.getId());
                                i.putExtra("name", user.getName());
                                i.putExtra("isAdmin", user.getAdmin());
                            }

                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });


                }

            }
        }, SPLASH_SCREEN_TIME_OUT);

    }
}
