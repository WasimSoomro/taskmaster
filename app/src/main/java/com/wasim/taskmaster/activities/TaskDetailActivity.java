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
        String taskDescriptionString = null;
        String taskStateString = null;

        if(callingIntent != null) {
            taskNameString = callingIntent.getStringExtra(MainActivity.TASK_TITLE_EXTRA_TAG);
            taskDescriptionString = callingIntent.getStringExtra(MainActivity.TASK_DESCRIPTION_EXTRA_TAG);
            taskStateString = callingIntent.getStringExtra(MainActivity.TASK_STATE_EXTRA_TAG);

        }
        TextView taskNameTextView = (TextView) findViewById(R.id.taskDetailTitle);
        TextView taskDescriptionTextView = (TextView) findViewById(R.id.description);
        TextView taskStateTextView = (TextView) findViewById(R.id.state);


        if(taskNameString != null && !taskNameString.equals("")){
            taskNameTextView.setText(taskNameString);
        } else {
            taskNameTextView.setText("No Task Name");
        }

        if(taskDescriptionString != null && !taskDescriptionString.equals("")){
            taskDescriptionTextView.setText(taskDescriptionString);
        } else {
            taskDescriptionTextView.setText("No Description");
        }

        if(taskStateString != null && !taskStateString.equals("")){
            taskStateTextView.setText(taskStateString);
        } else {
            taskStateTextView.setText("No State");
        }

    }
}