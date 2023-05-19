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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.model.UserTask;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
    private final Context ctx;
    private List<UserTask> listOfTaskForWorker;

    public TaskAdapter(Context ctx, List<UserTask> listOfTaskForWorker) {
        this.listOfTaskForWorker = listOfTaskForWorker;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        UserTask task = listOfTaskForWorker.get(position);
        if(task.getFinished()) {
            holder.showPopupButton.setAlpha(0.5f);
            holder.bulletButton.setAlpha(0.5f);
            holder.name.setAlpha(0.5F);
            holder.showPopupButton.setEnabled(false);
        }
        holder.name.setText(task.getTask().getName());
        holder.showPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout constraintLayout = ((Activity) ctx).findViewById(R.id.popup);
                ConstraintLayout constraintLayout1 = ((Activity) ctx).findViewById(R.id.parent_user_layout);
                ShapeableImageView shapeableImageView = ((Activity) ctx).findViewById(R.id.task_list_label);
                RecyclerView recyclerView = ((Activity) ctx).findViewById(R.id.taskList_recyclerView);
                TextView taskName =  ((Activity) ctx).findViewById(R.id.task_text_extend);
                shapeableImageView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                Transition transition = new Slide(Gravity.BOTTOM);
                transition.setDuration(600);
                TransitionManager.beginDelayedTransition(constraintLayout1, transition);
                constraintLayout.setVisibility(View.VISIBLE);
                TextView taskIdHolder =  ((Activity) ctx).findViewById(R.id.currentTask);
                taskIdHolder.setText(String.valueOf(task.getId()));
                taskName.setText(task.getTask().getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfTaskForWorker.size();
    }
}
