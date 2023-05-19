package com.example.testapp.adapter;


import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;

public class TaskHolder extends RecyclerView.ViewHolder {


    TextView name;
    ImageButton showPopupButton;
    ImageButton bulletButton;
    public TaskHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.task_text);
        showPopupButton = itemView.findViewById(R.id.arrow_button);
        bulletButton = itemView.findViewById(R.id.list_bullet);
    }
}


