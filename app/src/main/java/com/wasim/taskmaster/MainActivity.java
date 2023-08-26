package com.wasim.taskmaster;

import static com.wasim.taskmaster.activities.SettingsActivity.USERNAME_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wasim.taskmaster.activities.AddTaskActivity;
import com.wasim.taskmaster.activities.AllTasks;
import com.wasim.taskmaster.activities.SettingsActivity;
import com.wasim.taskmaster.activities.TaskDetailActivity;
import com.wasim.taskmaster.adapters.TaskListRecyclerViewAdapter;
import com.wasim.taskmaster.database.TaskmasterDatabase;
import com.wasim.taskmaster.models.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String Task_One;
    private final String TAG = "MainActivity";
    //Ask for TA Help on line 35, I just added it, 2:57:19 Rey said we added it yesterday, I didn't add it
    public static final String TASK_TITLE_EXTRA_TAG = "taskTitle";
    public static final String TASK_DESCRIPTION_EXTRA_TAG = "taskDescription";
    public static final String TASK_STATE_EXTRA_TAG = "taskState";


    SharedPreferences preferences;

    List<Task> tasks = new ArrayList<>();

    TaskmasterDatabase taskmasterDatabase;
    public static final String DATABASE_NAME = "wasim_taskmaster";
    TaskListRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupDatabase();
        setupAddTaskButton();
        setupAllTaskButton();
        setupSettingsButton();
//        setupTaskOneButton();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRepopulateUsername();
        updateTaskListFromDatabase();
    }

    void setupRepopulateUsername() {
        preferences = PreferenceManager.getDefaultSharedPreferences((this));
        String userName = preferences.getString(USERNAME_TAG, "Need Username");
        TextView repopulateUsername = findViewById(R.id.MainActivityLabelTextView);
        repopulateUsername.setText(userName + "'s Tasks");
    }

    void setupDatabase(){
        taskmasterDatabase = Room.databaseBuilder(
                getApplicationContext(),
                TaskmasterDatabase.class,
                DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        tasks = taskmasterDatabase.taskDao().findAll();
    }

    void setupAllTaskButton() {
        Button allTasksButton = findViewById(R.id.AllTasks);
        allTasksButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(goToAllTasksIntent);
        });
    }

    void setupAddTaskButton() {
        Button addTaskButton = findViewById(R.id.MainActivityMoveToAddTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTasksIntent = new Intent(MainActivity.this, AddTaskActivity.class);
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


         adapter = new TaskListRecyclerViewAdapter(tasks, this);
            taskListRecyclerView.setAdapter(adapter);
        }

    void updateTaskListFromDatabase() {
        tasks.clear();
        tasks.addAll(taskmasterDatabase.taskDao().findAll());
        adapter.notifyDataSetChanged();

    }
    }
