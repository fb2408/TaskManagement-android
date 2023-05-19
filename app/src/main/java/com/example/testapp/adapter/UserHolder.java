package com.example.testapp.adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;

public class UserHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView orgUnitName;
    ImageButton showMasterDetailButton;
    ImageButton bulletButton;
    public UserHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.user_name);
        orgUnitName = itemView.findViewById(R.id.org_unit_name);
        showMasterDetailButton = itemView.findViewById(R.id.expand_user);
        bulletButton = itemView.findViewById(R.id.list_bullet);
    }
}
