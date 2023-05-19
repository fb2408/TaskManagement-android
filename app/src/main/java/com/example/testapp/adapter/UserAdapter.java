package com.example.testapp.adapter;

import android.app.Activity;
import android.content.Context;

import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.MainActivity;
import com.example.testapp.R;
import com.example.testapp.model.Task;
import com.example.testapp.model.TaskType;
import com.example.testapp.model.User;
import com.example.testapp.model.UserTask;
import com.example.testapp.retrofit.AdminApi;
import com.example.testapp.retrofit.RetrofitService;
import com.google.android.material.imageview.ShapeableImageView;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserHolder>{
    private final Context ctx;
    private List<String> userList;
    private List<UserTask> userTaskMap;

    List<TaskType> taskTypeList = new ArrayList<>();

    public UserAdapter(Context ctx, List<String> userList, List<UserTask> userTaskMap) {
        this.ctx = ctx;
        this.userList = userList;
        this.userTaskMap = userTaskMap;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_list_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        String name = userList.get(position);
        holder.name.setText(name);

        holder.showMasterDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout constraintLayout = ((Activity) ctx).findViewById(R.id.master_detail_popup);
                ConstraintLayout constraintLayout1 = ((Activity) ctx).findViewById(R.id.parent_user_layout);
                ShapeableImageView shapeableImageView = ((Activity) ctx).findViewById(R.id.task_list_label);
                RecyclerView recyclerView = ((Activity) ctx).findViewById(R.id.taskList_recyclerView);
                TextView userName =  ((Activity) ctx).findViewById(R.id.user_name_popup);
                TextView orgUnit =  ((Activity) ctx).findViewById(R.id.org_unit_name_popup);
                //make more dynamically
                orgUnit.setText("Aria bar");
                userName.setText(name);
//                List<Task> listTasks = userTaskMap.get(name);
                List<UserTask> listTasks = new ArrayList<>();
                listTasks = userTaskMap.stream().filter(ut -> ut.getUser().getName().equals(name)).collect(Collectors.toList());
                for (UserTask listTask : listTasks) {
                    System.out.println(listTask.getId());
                }
                UserTaskAdapter userTaskAdapter = new UserTaskAdapter(ctx, listTasks);
                RecyclerView rw_user_tasks = ((Activity) ctx).findViewById(R.id.user_task_list_recycler_view);
                rw_user_tasks.setLayoutManager(new LinearLayoutManager(ctx));
                rw_user_tasks.setHasFixedSize(true);
                rw_user_tasks.setAdapter(userTaskAdapter);

                shapeableImageView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                Transition transition = new Slide(Gravity.BOTTOM);
                transition.setDuration(600);
                TransitionManager.beginDelayedTransition(constraintLayout1, transition);
                constraintLayout.setVisibility(View.VISIBLE);
                ImageButton addButton = ((Activity) ctx).findViewById(R.id.add_button);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConstraintLayout constraintLayout2 = ((Activity) ctx).findViewById(R.id.list_item_add_cl);
                        constraintLayout2.setVisibility(View.VISIBLE);
                        constraintLayout2.setFocusable(true);
                        constraintLayout2.setFocusableInTouchMode(true);
                        addButton.setVisibility(View.GONE);
                        ImageButton declineButton = ((Activity) ctx).findViewById(R.id.decline_button);
                        ImageButton acceptButton = ((Activity) ctx).findViewById(R.id.accept_button);
                        Spinner dropdown = ((Activity) ctx).findViewById(R.id.spinner_task_types);

                        RetrofitService retrofitService = new RetrofitService();
                        AdminApi adminApi = retrofitService.getRetrofit().create(AdminApi.class);
                        adminApi.listAllTaskTypes().enqueue(new Callback<List<TaskType>>() {
                            @Override
                            public void onResponse(Call<List<TaskType>> call, Response<List<TaskType>> response) {

                                taskTypeList = response.body();
                                ArrayList<String> dropdownOptions = new ArrayList<>();

                                List<String> taskTypeListName = new ArrayList<>();
                                for (TaskType taskType : taskTypeList) {
                                    taskTypeListName.add(taskType.getTaskTypeName());
                                    System.out.println(taskType.getTaskTypeName());
                                }
                                dropdownOptions.addAll(taskTypeListName);

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,android.R.layout.simple_spinner_item, dropdownOptions);
                                dropdown.setAdapter(adapter);
                                dropdown.setSelection(0);
                            }
                            @Override
                            public void onFailure(Call<List<TaskType>> call, Throwable t) {
                            }});

                        declineButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                constraintLayout2.setVisibility(View.GONE);
                                addButton.setVisibility(View.VISIBLE);
                            }
                        });
                        acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //first of all fetch all task types

                                        EditText taskNameEdit = ((Activity) ctx).findViewById(R.id.user_task_text_edit);
                                        EditText taskLevelEdit = ((Activity) ctx).findViewById(R.id.task_level_edit);


                                        UserTask userTask = new UserTask();
                                        Task task = new Task();
                                        for (TaskType tt : taskTypeList) {
                                            if(tt.getTaskTypeName().equals(dropdown.getSelectedItem().toString())) {
                                                task.setTaskType(tt);
                                                break;
                                            }
                                         }
                                        task.setName(taskNameEdit.getText().toString());
                                        task.setLevel(Integer.parseInt(taskLevelEdit.getText().toString()));
                                        userTask.setFinished(false);
                                        //todo: make more dynamically
                                        User user = new User();
                                        user.setName(name);
                                        userTask.setUser(user);
                                        userTask.setTask(task);
                                        adminApi.addTask(task).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if(response.isSuccessful()) {
                                                    //todo: add this user task at backend
                                                    userTaskAdapter.addItem(0, userTask);
                                                    constraintLayout2.setVisibility(View.GONE);
                                                    addButton.setVisibility(View.VISIBLE);
                                                    Toast.makeText(ctx, "Task with name " + taskNameEdit.getText().toString() + " succesfully added!", Toast.LENGTH_SHORT).show();
                                                } else{
                                                    Toast.makeText(ctx, "Task with name " + taskNameEdit.getText().toString() + " already exsist!", Toast.LENGTH_SHORT).show();
                                                    taskNameEdit.setFocusable(true);
                                                }


                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {

                                            }
                                        });
                                        //update task and triger on backend to add usertask








                            }
                        });


                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}
