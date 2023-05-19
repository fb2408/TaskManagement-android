package com.example.testapp.adapter;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.model.Task;
import com.example.testapp.model.TaskType;
import com.example.testapp.model.UserTask;
import com.example.testapp.retrofit.AdminApi;
import com.example.testapp.retrofit.RetrofitService;


import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTaskAdapter extends RecyclerView.Adapter<UserTaskHolder> {
    private final Context ctx;
    private final List<UserTask> userTaskList;

    private ImageButton addButton;
    private List<TaskType> taskTypeList;

    public UserTaskAdapter(Context ctx, List<UserTask> userTaskList) {
        this.ctx = ctx;
        this.userTaskList = userTaskList;
        addButton = ((Activity) ctx).findViewById(R.id.add_button);
        RetrofitService retrofitService = new RetrofitService();
        AdminApi adminApi = retrofitService.getRetrofit().create(AdminApi.class);
        adminApi.listAllTaskTypes().enqueue(new Callback<List<TaskType>>() {
            @Override
            public void onResponse(Call<List<TaskType>> call, Response<List<TaskType>> response) {
                taskTypeList = response.body();
            }
            @Override
            public void onFailure(Call<List<TaskType>> call, Throwable t) {
            }});
    }

    @NonNull
    @Override
    public UserTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_task_list_item, parent, false);
        return new UserTaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTaskHolder holder, int position) {
        UserTask userTask = userTaskList.get(position);
        StringBuilder sb = new StringBuilder();
//        sb.append(userTask.getTask().getName()).append("---").append(position);
        sb.append(userTask.getTask().getName());
        holder.taskId.setText(String.valueOf(userTask.getId()));
        holder.taskName.setText(sb.toString());
        holder.taskLevel.setText(String.valueOf(userTask.getTask().getLevel()));
        holder.taskType.setText(userTask.getTask().getTaskType().getTaskTypeName());
        holder.taskFinished.setText(String.valueOf(userTask.getFinished()));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem(holder, position);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                RetrofitService retrofit = new RetrofitService();
                                AdminApi adminApi = retrofit.getRetrofit().create(AdminApi.class);
                                adminApi.deleteUserTask(holder.taskId.getText().toString()).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        //notify observers that one item is deleted and all after him must be shifted one position
                                        Toast.makeText(ctx, "Successfully deleted usertask with id " + holder.taskId.getText().toString(), Toast.LENGTH_SHORT).show();
                                        userTaskList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, userTaskList.size());
//                                        userTaskList.removeIf(ut -> ut.getId() == Integer.parseInt(holder.taskId.getText().toString()));
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage("Are you sure you want to delete this task for this user?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }


    private void editItem(UserTaskHolder userTaskHolder, int position) {
        UserTask userTask = userTaskList.get(position);
        userTaskHolder.regularCard.setVisibility(View.GONE);
        userTaskHolder.editCard.setVisibility(View.VISIBLE);
        userTaskHolder.taskNameEdit.setText(userTask.getTask().getName());
        userTaskHolder.taskLevelEdit.setText(String.valueOf(userTask.getTask().getLevel()));
        userTaskHolder.taskFinishedEdit.setText(String.valueOf(userTask.getFinished()));
//        userTaskHolder.taskTypeEdit.setText(userTask.getTask().getTaskType().getTaskTypeName());
        ArrayList<String> dropdownOptions = new ArrayList<>();

            List<String> taskTypeListName = new ArrayList<>();
                for (TaskType taskType : taskTypeList) {
                    taskTypeListName.add(taskType.getTaskTypeName());
                    System.out.println(taskType.getTaskTypeName());
                }
                //todo: check nullability
                dropdownOptions.addAll(taskTypeListName);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx,android.R.layout.simple_spinner_item, dropdownOptions);
                userTaskHolder.dropdown.setAdapter(adapter);
                userTaskHolder.dropdown.setSelection(adapter.getPosition(userTask.getTask().getTaskType().getTaskTypeName()));


        userTaskHolder.declineAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userTaskHolder.regularCard.setVisibility(View.VISIBLE);
                userTaskHolder.editCard.setVisibility(View.GONE);
            }
        });

        userTaskHolder.acceptAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(userTaskHolder.dropdown.getSelectedItem().toString());
                if(userTaskHolder.taskName.getText().toString().equals(userTaskHolder.taskNameEdit.getText().toString()) &&
                    userTaskHolder.taskFinished.getText().toString().equals(userTaskHolder.taskFinishedEdit.getText().toString()) &&
                    userTaskHolder.taskType.getText().toString().equals(userTaskHolder.dropdown.getSelectedItem().toString()) &&
                    userTaskHolder.taskLevel.getText().toString().equals(userTaskHolder.taskLevelEdit.getText().toString())) {

                    userTaskHolder.regularCard.setVisibility(View.VISIBLE);
                    userTaskHolder.editCard.setVisibility(View.GONE);
                    Toast.makeText(ctx, "No changes to update",  Toast.LENGTH_SHORT).show();

                } else {

                    Task task = userTask.getTask();
                    task.setId(userTaskList.get(position).getTask().getId());
                    System.out.println(userTaskHolder.taskLevelEdit.getText().toString());
                    task.setLevel(Integer.parseInt(userTaskHolder.taskLevelEdit.getText().toString()));
                    task.setName(userTaskHolder.taskNameEdit.getText().toString());
                    System.out.println(userTaskHolder.dropdown.getSelectedItem().toString());
                    for (TaskType taskType : taskTypeList) {
                        if(taskType.getTaskTypeName().equals(userTaskHolder.dropdown.getSelectedItem().toString())) {
                            task.setTaskType(taskType);
                            break;
                        }
                    }
                    RetrofitService retrofitService = new RetrofitService();
                    AdminApi adminApi = retrofitService.getRetrofit().create(AdminApi.class);
                    adminApi.updateTask(task).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(ctx, "Succesfully updated task", Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return userTaskList.size();
    }

    public void addItem(int position, UserTask ut) {

        userTaskList.add(position, ut);
        notifyItemInserted(position);
        addButton.setVisibility(View.INVISIBLE);
        System.out.println("I am trying to add new user task");
    }
}
