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

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.wasim.taskmaster.activities.AddTaskActivity;
import com.wasim.taskmaster.activities.AllTasks;
import com.wasim.taskmaster.activities.SettingsActivity;
import com.wasim.taskmaster.adapters.TaskListRecyclerViewAdapter;

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

    List<Task> tasks;

    TaskListRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tasks = new ArrayList<>();

        createTeamInstances();
        setupAddTaskButton();
        setupAllTaskButton();
        setupSettingsButton();
        updateTaskListFromDatabase();
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

        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    Log.i(TAG, "Read tasks successfully!");
                    tasks.clear();
                    for (Task databaseTask : success.getData()) {

                        //IN lab, get this from settings?
                        String teamName = "Android";
                        if(databaseTask.getTeamP() != null
                       && databaseTask.getTeamP().getTeamName().equals(teamName)) {
                            tasks.add(databaseTask);
                        }
                    }
                    runOnUiThread(() ->
                    {
                        adapter.notifyDataSetChanged();
                    });
                },
                failure -> Log.i(TAG, "Did not read tasks successfully")
        );
    }

    void createTeamInstances() {
        Team team1 = Team.builder()
                .teamName("Android")
                .build();

        Amplify.API.mutate(
                        ModelMutation.create(team1),
                        successResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): made a team successfully"),
                        failureResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): team failed with this response: " + failureResponse)
        );
        Team team2 = Team.builder()
                .teamName("IOS")
                .build();

        Amplify.API.mutate(
                        ModelMutation.create(team2),
                        successResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): made a team successfully"),
                        failureResponse -> Log.i(TAG, "MainActivity.createTeamInstances(): team failed with this response: " + failureResponse)
        );

    }

    }
