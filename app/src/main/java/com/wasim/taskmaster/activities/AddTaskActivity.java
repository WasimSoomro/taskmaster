package com.wasim.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskCategoryEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.wasim.taskmaster.MainActivity;
import com.wasim.taskmaster.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
//import com.amazonaws.util.DateUtils;

public class AddTaskActivity extends AppCompatActivity {
    private final String TAG = "AddTaskActivity";

    CompletableFuture<List<Team>> teamsFuture = new CompletableFuture<>();

    Spinner taskCategorySpinner;
    Spinner taskTeamSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task2);

      teamsFuture = new CompletableFuture<>();

//        Spinner taskCategorySpinner = (Spinner) findViewById(R.id.TaskCategorySpinner);
        taskCategorySpinner = findViewById(R.id.AddTaskActivityTeamSpinner);

        taskTeamSpinner = findViewById(R.id.AddTaskActivityTeamSpinner);
//        setupTaskCategorySpinner(taskCategorySpinner);
//        setupTaskCategorySpinner();
        setupTaskTeamSpinner();
        setupSubmitButton();
    }

    void setupTaskCategorySpinner(Spinner taskCategorySpinner) {
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskCategoryEnum.values()
        ));
    }

    void setupSubmitButton() {
        Button submitButton = (Button) findViewById(R.id.SubmitButton);

        submitButton.setOnClickListener(v -> {
            String title = ((EditText)findViewById(R.id.editTextText)).getText().toString();
            String description = ((EditText)findViewById(R.id.editTextText2)).getText().toString();
//                  String selectedTeamString = taskTeamSpinner.getSelectedItem().toString();
                String selectedTeamString = (taskTeamSpinner.getSelectedItem() != null) ? taskTeamSpinner.getSelectedItem().toString() : "";


            List<Team> teams = null;
                  try {
                      teams = teamsFuture.get();
                  } catch (InterruptedException ie) {
                      Log.e(TAG, "InterruptedException while getting contacts");
                      Thread.currentThread().interrupt();
                  } catch (ExecutionException ee) {
                      Log.e(TAG, "ExecutionException while getting contacts");
                  }
                  assert teams != null;
                  Team selectedTeam = teams.stream().filter(c -> c.getTeamName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

//        Button submitButton = (Button) findViewById(R.id.SubmitButton);
//        submitButton.setOnClickListener(v -> {
//            String title = ((EditText)findViewById(R.id.editTextText)).getText().toString();
//            String description = ((EditText)findViewById(R.id.editTextText2)).getText().toString();
////            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());

            Task newTask = Task.builder()
                    .title(title)
                    .body(description)
//                    .dateCreated(new Temporal.DateTime(new Date(),0))
                    .taskCategory(TaskCategoryEnum.NEW)
                    .teamP(selectedTeam)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask),  // making a GraphQL request to the cloud
                    successResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully"),  // success callback
                    failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): failed with this response: " + failureResponse)  // failure callback
            );
            Snackbar.make(findViewById(android.R.id.content), "Task submitted", Snackbar.LENGTH_SHORT).show();
        });
    }

    void setupTaskTeamSpinner() {
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read contacts successfully");
                    ArrayList<String> contactNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for(Team team : success.getData()) {
                        teams.add(team);
                        contactNames.add(team.getTeamName());
                    }
                    teamsFuture.complete(teams);

                    runOnUiThread(() -> {
                        taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                contactNames
                        ));
                    });
                },
                failure -> {
                   teamsFuture.complete(null);
                    Log.i(TAG, "Did not read contacts successfully!!");
                }
        );
    }
}