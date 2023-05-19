package com.example.testapp.retrofit;

import com.example.testapp.model.UserTask;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WorkerApi {

    @GET("/worker/getTasksForWorker")
    Call<List<UserTask>> getTasksForWorker(@Query("id") String id);

    @GET("/worker/finishingTask")
    Call<UserTask> taskFinished(@Query("id") String taskId);

    @GET("/worker/task-finished-notification")
    Call<String> sendNotificationTaskFinished();


}
