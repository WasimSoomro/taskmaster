package com.wasim.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskCategoryEnum;
import com.google.android.material.snackbar.Snackbar;
import com.wasim.taskmaster.MainActivity;
import com.wasim.taskmaster.R;

import java.util.Date;
//import com.amazonaws.util.DateUtils;

public class AddTaskActivity extends AppCompatActivity {
    private final String TAG = "AddTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task2);

//        Spinner taskCategorySpinner = (Spinner) findViewById(R.id.TaskCategorySpinner);

//        setupTaskCategorySpinner(taskCategorySpinner);
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
//            String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(new Date());

            Task newTask = Task.builder()
                    .title(title)
                    .body(description)
                    .dateCreated(new Temporal.DateTime(new Date(),0))
                    .taskCategory(TaskCategoryEnum.NEW)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(newTask),  // making a GraphQL request to the cloud
                    successResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): made a task successfully"),  // success callback
                    failureResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): failed with this response: " + failureResponse)  // failure callback
            );
//            Task taskToSubmit = new Task(
//                    ((EditText)findViewById(R.id.editTextText)).getText().toString(),
//                    ((EditText)findViewById(R.id.editTextText2)).getText().toString(),
//                    new Date(),
//                    Task.State.NEW
////                    TaskCategoryEnum.fromString(taskCategorySpinner.getSelectedItem().toString())
//
//            );

//            taskmasterDatabase.taskDao().insertATask(taskToSubmit);

            Snackbar.make(findViewById(android.R.id.content), "Task submitted", Snackbar.LENGTH_SHORT).show();
        });
    }


}