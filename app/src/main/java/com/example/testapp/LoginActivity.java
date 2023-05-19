package com.example.testapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.model.User;
import com.example.testapp.retrofit.LoginApi;
import com.example.testapp.retrofit.RetrofitService;
import com.example.testapp.retrofit.UserApi;
import com.example.testapp.retrofit.WorkerApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText passwordView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        passwordView = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo:crypting password
                String password = passwordView.getText().toString();
                System.out.println(password);
                String email = "antoniojuric@gmail.com";
                String emailBruno = "filip.bura2408@gmail.com";

                mAuth.signInWithEmailAndPassword(emailBruno, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user != null) {
                                        RetrofitService retrofit = new RetrofitService();
                                        LoginApi loginApi = retrofit.getRetrofit().create(LoginApi.class);
                                        System.out.println(user.getEmail().toString());
                                        loginApi.login(user.getEmail()).enqueue(new Callback<User>() {
                                            @Override
                                            public void onResponse(Call<User> call, Response<User> response) {
                                                User user = response.body();
                                                if(user != null) {
                                                    if(!user.getRegistred()) {
                                                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(t ->   {
                                                            String token = t.getResult().toString();
                                                            RetrofitService retrofitService = new RetrofitService();
                                                            UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
                                                            userApi.changeDeviceToken(String.valueOf(user.getId()), token).enqueue(new Callback<Void>() {
                                                                @Override
                                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                                }
                                                                @Override
                                                                public void onFailure(Call<Void> call, Throwable t) {

                                                                }
                                                            });
                                                        });

                                                        subsribeFunction(user.getAdmin());
                                                    }

                                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                    i.putExtra("email", user.getId());
                                                    startActivity(i);

                                                } else {
                                                    Toast.makeText(LoginActivity.this,"User is not active!", Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<User> call, Throwable t) {
                                                t.printStackTrace();
                                                Toast.makeText(LoginActivity.this, "Wrong password or user is not active!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

        TextView redirectToRegisterBtn = findViewById(R.id.redirectToRegister);
        redirectToRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
    public void subsribeFunction(boolean isAdmin) {

        if(isAdmin) {
            FirebaseMessaging.getInstance().subscribeToTopic("task_finished").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe failed";
                    }
                    System.out.println("Admin subsribed for task finished");
                    Log.d(TAG, msg);
                }
            });
            FirebaseMessaging.getInstance().subscribeToTopic("task-redirected").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe failed";
                    }
                    System.out.println("Admin subsribed for task redirected");
                    Log.d(TAG, msg);
                }
            });
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("new-task").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe failed";
                    }
                    System.out.println("Worker subsribed for new task");
                    Log.d(TAG, msg);
                }
            });

            FirebaseMessaging.getInstance().subscribeToTopic("accept-redirect").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String msg = "Subscribed";
                    if (!task.isSuccessful()) {
                        msg = "Subscribe failed";
                    }
                    System.out.println("Worker subsribed for redirect task");
                    Log.d(TAG, msg);
                }
            });
        }

    }
}
