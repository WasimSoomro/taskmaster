package com.wasim.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wasim.taskmaster.MainActivity;
import com.wasim.taskmaster.R;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        setupViewTaskName();
    }

    void setupViewTaskName() {
        Intent callingIntent = getIntent();
        String taskNameString = null;
        if(callingIntent != null) {
            taskNameString = callingIntent.getStringExtra(MainActivity.Task_One);
    }
        TextView taskNameTextView = (TextView) findViewById(R.id.taskDetailTitle);
        if(taskNameString != null && !taskNameString.equals("")){
            taskNameTextView.setText(taskNameString);
        } else {
            taskNameTextView.setText("No Task Name");
        }

    }
}