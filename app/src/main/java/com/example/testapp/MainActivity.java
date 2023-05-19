package com.example.testapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.adapter.TaskAdapter;


import com.example.testapp.adapter.UserAdapter;
import com.example.testapp.adapter.UserTaskAdapter;
import com.example.testapp.model.Task;
import com.example.testapp.model.UserTask;
import com.example.testapp.retrofit.AdminApi;
import com.example.testapp.retrofit.LoginApi;
import com.example.testapp.retrofit.RetrofitService;
import com.example.testapp.retrofit.WorkerApi;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        Integer id = null;
        String name = null;
        boolean isAdmin = false;
        if (extras != null) {
            id = extras.getInt("id");
            name = extras.getString("name");
            isAdmin = extras.getBoolean("isAdmin");
        }

        TextView userName = findViewById(R.id.worker_id);
        userName.setText(name);
            recyclerView = findViewById(R.id.taskList_recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RetrofitService retrofitService = new RetrofitService();
        WorkerApi workerApi = retrofitService.getRetrofit().create(WorkerApi.class);
        AdminApi adminApi = retrofitService.getRetrofit().create(AdminApi.class);
        LoginApi loginApi = retrofitService.getRetrofit().create(LoginApi.class);
        //todo:check if user changed device

        if(isAdmin) {
            TextView organizationUnit = findViewById(R.id.worker_unit_id);
            organizationUnit.setText("Olympia Sky");
            ShapeableImageView taskListLsabel = findViewById(R.id.task_list_label);
            taskListLsabel.setVisibility(View.INVISIBLE);
            //admins profile page
            loadUsers(adminApi);


        } else {
            //workers profile page
            loadTasks(String.valueOf(id), workerApi);

            ShapeableImageView userListLabel = findViewById(R.id.user_list_label);
            userListLabel.setVisibility(View.GONE);
            ShapeableImageView unitsLabelList = findViewById(R.id.unit_list_label);
            unitsLabelList.setVisibility(View.GONE);

            AppCompatButton finishButton = findViewById(R.id.btnFinish);
            AppCompatButton redirectButton = findViewById(R.id.btnRedirect);

            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView taskIdHolder = findViewById(R.id.currentTask);
                    String taskId = taskIdHolder.getText().toString();
                    System.out.println("Holded id" + taskId);

                    workerApi.taskFinished(taskId).enqueue(new Callback<UserTask>() {
                        @Override
                        public void onResponse(Call<UserTask> call, Response<UserTask> response) {
                            UserTask usertask = response.body();
                            System.out.println(usertask.getId());
                            ConstraintLayout constraintLayout = findViewById(R.id.popup);
                            ShapeableImageView shapeableImageView = findViewById(R.id.task_list_label);
                            constraintLayout.setVisibility(View.GONE);
                            ConstraintLayout parent = findViewById(R.id.parent_user_layout);
                            Transition transition = new Slide(Gravity.BOTTOM);
                            transition.setDuration(600);
                            TransitionManager.beginDelayedTransition(parent, transition);
                            shapeableImageView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            workerApi.sendNotificationTaskFinished().enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result= response.body().toString();
                                    System.out.println(result);
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                            loadTasks(String.valueOf(usertask.getUser().getId()), workerApi);
                            Toast.makeText(MainActivity.this, "Succesfully change task to finished", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<UserTask> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Failed to finish task", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            //todo: implement clicking on redirect button
            redirectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }

    private void populateUsersWithTasks(List<UserTask> userTaskList) {
        Set<String> userSet = new HashSet<>();
        userTaskList.forEach(ut -> userSet.add(ut.getUser().getName()));
        List<String> userList = new ArrayList<>();
        userList.addAll(userSet);
        UserAdapter userAdapter = new UserAdapter(MainActivity.this, userList, userTaskList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(userAdapter);

        ImageButton addTaskButton = findViewById(R.id.add_button);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = new Task();
                UserTask userTask = new UserTask();
                userTask.setTask(task);
                userTask.setFinished(false);
                userTask.setUser(userTaskList.get(0).getUser());
                userTask.setOrganizationUnit(userTaskList.get(0).getOrganizationUnit());
                userTask.setReqRedirect(false);

            }
        });


    }

    private void loadTasks(String id, WorkerApi workerApi) {
        workerApi.getTasksForWorker(id).enqueue(new Callback<List<UserTask>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<UserTask>> call, Response<List<UserTask>> response) {
                for (Object o : response.body()) {
                    System.out.println(o);
                }
                populateListView(response.body());
            }
            

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<UserTask>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUsers(AdminApi adminApi) {
        adminApi.tasksOverview().enqueue(new Callback<List<UserTask>>() {
            @Override
            public void onResponse(Call<List<UserTask>> call, Response<List<UserTask>> response) {
                if(response.isSuccessful()) {
                    List<UserTask> userTaskList = response.body();
                    populateUsersWithTasks(userTaskList);
                }
                int code = response.code();
                System.out.println("HTTP code: " + code);

            }
            @Override
            public void onFailure(Call<List<UserTask>> call, Throwable t) {

            }
        });

    }

    private void populateListView(List<UserTask> taskList) {
        TextView unitName = findViewById(R.id.worker_unit_id);
        //with condition that one person works only at one unit
        unitName.setText(taskList.get(0).getOrganizationUnit().getName());
        TaskAdapter taskAdapter = new TaskAdapter(MainActivity.this, taskList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(taskAdapter);
    }
}