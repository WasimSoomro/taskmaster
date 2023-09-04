package com.wasim.taskmaster;

import static com.wasim.taskmaster.activities.SettingsActivity.USERNAME_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.icu.util.Output;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.wasim.taskmaster.activities.AddTaskActivity;
import com.wasim.taskmaster.activities.AllTasks;
import com.wasim.taskmaster.activities.SettingsActivity;
import com.wasim.taskmaster.adapters.TaskListRecyclerViewAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private final MediaPlayer mp = new MediaPlayer();

    public static final String TASK_TITLE_EXTRA_TAG = "taskTitle";
    public static final String TASK_DESCRIPTION_EXTRA_TAG = "taskDescription";
    public static final String TASK_STATE_EXTRA_TAG = "taskState";

    List<Task> tasks;
    TaskListRecyclerViewAdapter adapter;

    SharedPreferences preferences;

    Button announceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tasks = new ArrayList<>();

        announceButton = findViewById(R.id.MainActivityVoiceButton);

        logAppStartup();

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        createTeamInstances();
        setupAddTaskButton();
        setupAllTaskButton();
        setupRecyclerView();
        setupSettingsButton();
        setupAnnounceButton();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRepopulateUsername();
        updateTaskListFromDatabase();
    }

    void setupAddTaskButton() {
        Button addTaskButton = findViewById(R.id.MainActivityMoveToAddTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent goToAddTasksIntent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(goToAddTasksIntent);
        });
    }

    void setupAllTaskButton() {
        Button allTasksButton = findViewById(R.id.AllTasks);
        allTasksButton.setOnClickListener(view -> {
            Intent goToAllTasksIntent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(goToAllTasksIntent);
        });
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
                        if (databaseTask.getTeamP() != null
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

                if (parent.getChildAdapterPosition(view) == tasks.size() - 1) {
                    outRect.bottom = 0;
                }
            }
        });


        adapter = new TaskListRecyclerViewAdapter(tasks, this);
        taskListRecyclerView.setAdapter(adapter);
    }

    void setupRepopulateUsername() {
        preferences = PreferenceManager.getDefaultSharedPreferences((this));
        String userName = preferences.getString(USERNAME_TAG, "Need Username");
        TextView repopulateUsername = findViewById(R.id.MainActivityLabelTextView);
        repopulateUsername.setText(userName + "'s Tasks");
    }


    
    void setupAnnounceButton() {
        announceButton.setOnClickListener(v -> {

            String taskName = ((EditText) findViewById(R.id.editTextText)).getText().toString();
            Amplify.Predictions.convertTextToSpeech(
                    taskName,
                    result -> playAudio(result.getAudioData()),
                    error -> Log.e(TAG, "Audio conversion of task failed", error)
            );
        });
    }


    private void playAudio(InputStream data) {
        File mp3File = new File(getCacheDir(), "audio,mp3");

    try(
    OutputStream out = new FileOutputStream(mp3File))

    {
        byte[] buffer = new byte[8 * 1_024];
        int bytesRead = 0;
        while ((bytesRead = data.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        Log.i(TAG, "audio file finished reading");

        mp.reset();
        mp.setOnPreparedListener(MediaPlayer::start);
        mp.setDataSource(new FileInputStream(mp3File).getFD());
        mp.prepareAsync();
    } catch(IOException error) {
        Log.e(TAG, "Error writing audio file", error);
    }

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




