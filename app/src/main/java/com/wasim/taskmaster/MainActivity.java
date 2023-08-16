package com.wasim.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wasim.taskmaster.activities.AddTask;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupAddTaskButton();


        Button allTasksButton = findViewById(R.id.AllTasks);
        allTasksButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }));
    }

void setupAddTaskButton(){
    Button addTaskButton = findViewById(R.id.AddTaskButton);

    addTaskButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println("Add Task Pressed");
            Log.v(TAG,  "I'm a verbose log");
            Log.d(TAG, "I'm a DEBUG log");
            Log.i(TAG, "I;m an INFO log");
            Log.w(TAG, "I'm a warning log");
            Log.e(TAG, "I'm an error log");
            Log.wtf(TAG, "What a terrible failure");

            (( TextView)findViewById(R.id.MainActivityLabelTextView)).setText("");


        }
    });
    Intent goToAddTaskIntent = new Intent(MainActivity.this, AddTask.class);
    startActivity(goToAddTaskIntent);
}
}