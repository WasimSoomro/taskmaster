package com.wasim.taskmaster;

import static com.wasim.taskmaster.activities.SettingsActivity.USERNAME_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
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
import com.wasim.taskmaster.activities.adapters.TaskListRecyclerViewAdapter;
import com.wasim.taskmaster.activities.models.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String Task_One;
    private final String TAG = "MainActivity";
    //Ask for TA Help on line 35, I just added it, 2:57:19 Rey said we added it yesterday, I didn't add it
    public static final String TASK_TITLE_EXTRA_TAG = "taskTitle";

    SharedPreferences preferences;

    List<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupAddTaskButton();
        setupAllTaskButton();
        setupSettingsButton();
        setupTaskOneButton();
        setupRecyclerView();
        createTaskInstances();
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
    void setupRecyclerView() {
            RecyclerView taskListRecyclerView = (RecyclerView) findViewById(R.id.MainActivityTaskRecyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            taskListRecyclerView.setLayoutManager(layoutManager);

            int spaceInPixels = getResources().getDimensionPixelSize(R.dimen.task_fragment_spacing);
            taskListRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.bottom = spaceInPixels;

                    if(parent.getChildAdapterPosition(view) == tasks.size()-1) {
                        outRect.bottom = 0;
                    }
                }
            });


        TaskListRecyclerViewAdapter adapter = new TaskListRecyclerViewAdapter(tasks, this);
            taskListRecyclerView.setAdapter(adapter);
        }
    void createTaskInstances() {
        tasks.add(new Task("Call T-Mobile", "Phone Credit", Task.State.NEW));
        tasks.add(new Task("Call Chase Claims", "Wait 48 hours", Task.State.NEW));
        tasks.add(new Task("Buy Snacks", "Morning and afternoon", Task.State.NEW));
        }


    }
