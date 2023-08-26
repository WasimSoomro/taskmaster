package com.wasim.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.wasim.taskmaster.MainActivity;
import com.wasim.taskmaster.R;
import com.wasim.taskmaster.database.TaskmasterDatabase;
import com.wasim.taskmaster.models.Task;
import com.wasim.taskmaster.models.TaskCategoryEnum;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    TaskmasterDatabase taskmasterDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task2);

        taskmasterDatabase = Room.databaseBuilder(
                getApplicationContext(),
                        TaskmasterDatabase.class,
                        MainActivity.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();


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
            Task taskToSubmit = new Task(
                    ((EditText)findViewById(R.id.editTextText)).getText().toString(),
                    ((EditText)findViewById(R.id.editTextText2)).getText().toString(),
                    new Date(),
                    Task.State.NEW
//                    TaskCategoryEnum.fromString(taskCategorySpinner.getSelectedItem().toString())

            );

            taskmasterDatabase.taskDao().insertATask(taskToSubmit);
            Snackbar.make(findViewById(android.R.id.content), "Task submitted", Snackbar.LENGTH_SHORT).show();
        });
    }


}