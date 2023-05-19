package com.example.testapp.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;

public class UserTaskHolder extends RecyclerView.ViewHolder {
     TextView taskFinished;
     TextView taskId;
     TextView taskLevel;

     TextView taskName;

     TextView taskType;

     ImageButton editButton;
     ImageButton deleteButton;

     EditText taskNameEdit;
     EditText taskLevelEdit;
     EditText taskFinishedEdit;
     ImageButton acceptAction;
     ImageButton declineAction;

     CardView regularCard;

     CardView editCard;
     Spinner dropdown;

     ImageButton addButton;
    public UserTaskHolder(@NonNull View itemView) {
        super(itemView);

        regularCard = itemView.findViewById(R.id.list_item);
        taskId = itemView.findViewById(R.id.user_task_id);
        taskType = itemView.findViewById(R.id.task_type);
        taskLevel = itemView.findViewById(R.id.task_level);
        taskFinished = itemView.findViewById(R.id.task_finished);
        taskName = itemView.findViewById(R.id.user_task_text);
        editButton = itemView.findViewById(R.id.edit_button);
        deleteButton = itemView.findViewById(R.id.delete_button);

        editCard = itemView.findViewById(R.id.list_item_edit);
        taskLevelEdit = itemView.findViewById(R.id.task_level_edit);
        taskFinishedEdit = itemView.findViewById(R.id.task_finished_edit);
        taskNameEdit = itemView.findViewById(R.id.user_task_text_edit);
        declineAction = itemView.findViewById(R.id.decline_button);
        acceptAction = itemView.findViewById(R.id.accept_button);
        dropdown = itemView.findViewById(R.id.spinner_task_types);

        addButton = itemView.findViewById(R.id.add_button);

    }
}
