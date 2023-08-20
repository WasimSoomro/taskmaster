package com.wasim.taskmaster;

import static com.wasim.taskmaster.activities.SettingsActivity.USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wasim.taskmaster.activities.AddTask;
import com.wasim.taskmaster.activities.AllTasks;
import com.wasim.taskmaster.activities.SettingsActivity;
import com.wasim.taskmaster.activities.TaskDetailActivity;

public class MainActivity extends AppCompatActivity {
    public static String Task_One;
    private final String TAG = "MainActivity";

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupAddTaskButton();
        setupAllTaskButton();
        setupSettingsButton();
        setupTaskOneButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRepopulateUsername();
    }

    void setupRepopulateUsername() {
        preferences = PreferenceManager.getDefaultSharedPreferences((this));
        String userName = preferences.getString(USERNAME_TAG, "Need Username");
        TextView repopulateUsername = findViewById(R.id.MainActivityLabelTextView);
        repopulateUsername.setText(userName + "'s Tasks");
    }

    void setupAllTaskButton() {
        Button allTasksButton = findViewById(R.id.AllTasks);
        allTasksButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(goToAllTasksIntent);
        });
    }

    void setupAddTaskButton() {
        Button addTaskButton = findViewById(R.id.AddTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTasksIntent = new Intent(MainActivity.this, AddTask.class);
            startActivity(goToAddTasksIntent);
        });
    }

    void setupSettingsButton() {
        ImageView settingsButton = findViewById(R.id.mainActivitySettingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent goToSettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(goToSettingsIntent);
        });
    }

    void setupTaskOneButton() {
        Button taskOneButton = findViewById(R.id.task1);
        taskOneButton.setOnClickListener(view -> {
            String taskName = taskOneButton.getText().toString();
            Intent goToTaskDetailIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
            goToTaskDetailIntent.putExtra(Task_One, taskName);
            startActivity(goToTaskDetailIntent);
        });


    }
}