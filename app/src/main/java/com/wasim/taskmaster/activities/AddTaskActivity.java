package com.wasim.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
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
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskCategoryEnum;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.wasim.taskmaster.MainActivity;
import com.wasim.taskmaster.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

//import io.reactivex.rxjava3.core.Completable;

public class AddTaskActivity extends AppCompatActivity {
    private final String TAG = "AddTaskActivity";
    private FusedLocationProviderClient fusedLocationProviderClient;

    CompletableFuture<List<Team>> teamsFuture = null;

    Button submitButton;
    EditText editTextText;
    EditText editTextText2;
    Spinner taskCategorySpinner;
    Spinner taskTeamSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task2);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        teamsFuture = new CompletableFuture<>();

        taskCategorySpinner = findViewById(R.id.TaskCategorySpinner);
        taskTeamSpinner = findViewById(R.id.AddTaskActivityTeamSpinner);
        editTextText2 = findViewById(R.id.editTextText2);
        editTextText = findViewById(R.id.editTextText);
        submitButton = findViewById(R.id.SubmitButton);

        setupTaskCategorySpinner();
        setupTaskTeamSpinner();
        setupSubmitButton();
    }

    void setupTaskCategorySpinner() {
        taskCategorySpinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                TaskCategoryEnum.values()
        ));
    }

    void setupSubmitButton() {
        submitButton.setOnClickListener(v -> {

            getUserLastLocation();

            String selectedTeamString = taskTeamSpinner.getSelectedItem().toString();

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

            Task taskToSubmit = Task.builder()
                    .title(editTextText.getText().toString())
                    .body(editTextText2.getText().toString())
//                    .dateCreated(new Temporal.DateTime(new Date(), 0))
                    .taskCategory((TaskCategoryEnum) taskCategorySpinner.getSelectedItem())
                    .teamP(selectedTeam)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(taskToSubmit), // making a GraphQL request to cloud
                    successResponse -> Log.i(TAG, "AddTaskActivity.setupSaveButton(): made product successfully"), // success callback
                    failureResponse -> Log.i(TAG, "AddTaskActivity.setupSaveButton(): failed with this response " + failureResponse) // failure callback
            );

            Snackbar.make(findViewById(R.id.AddTaskActivityView), "Task saved!", Snackbar.LENGTH_SHORT).show();
        });
    }

    void setupTaskTeamSpinner() {
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    Log.i(TAG, "Read contacts successfully");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for (Team team : success.getData()) {
                        teams.add(team);
                        teamNames.add(team.getTeamName());
                    }
                    teamsFuture.complete(teams);

                    runOnUiThread(() -> {
                        taskTeamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                teamNames
                        ));
                    });
                },
                failure -> {
                    teamsFuture.complete(null);
                    Log.i(TAG, "Did not read contacts successfully!!");
                }
        );
    }

    void getUserLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Log.e(TAG, "Location callback was null");
            }
            String currentLatitude = Double.toString(location.getLatitude());
            String currentLongitude = Double.toString(location.getLongitude());
            Log.i(TAG, "User's last latitude: " + currentLatitude);
            Log.i(TAG, "User's last longitude: " + currentLongitude);
            //For lab save these values
        });
    }
}

//        .long(stringReturn[1])
//                .latitde(stringReturn[0])
