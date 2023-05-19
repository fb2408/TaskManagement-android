package com.example.testapp.retrofit;

import com.example.testapp.model.Task;
import com.example.testapp.model.TaskType;
import com.example.testapp.model.UserTask;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AdminApi {

    @POST("/admin/addUserTask")
    Call<Void> addUserTask(@Body UserTask userTask);

    @POST("/admin/update-user-task")
    Call<Void> updateUserTask(@Body UserTask userTask);

    @GET("/admin/new-task-notification")
    Call<Void> sendNewTaskNotification(@Query("id") String id, @Query("taskId") String taskId);

    @GET("/admin/overview-user-tasks")
    Call<List<UserTask>> tasksOverview();

    @GET("/admin/list-all-task-types")
    Call<List<TaskType>> listAllTaskTypes();
    @GET("/admin/delete-user-task")
    Call<Void> deleteUserTask(@Query("id") String userTaskId);

    @POST("/admin/update-task")
    Call<Void> updateTask(@Body Task task);

    @POST("/admin/add-task")
    Call<Void> addTask(@Body Task task);
}
