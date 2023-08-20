package com.wasim.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wasim.taskmaster.R;

public class SettingsActivity extends AppCompatActivity {
    public static final String USERNAME_TAG = "userName";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setupUsernameTextEdit();
        setupSaveButton();
    }

    void setupUsernameTextEdit() {
        final EditText usernameEditText = findViewById(R.id.settingsActivityUsernamePrompt);
        String username = preferences.getString(USERNAME_TAG, null);
        usernameEditText.setText(username);
    }

    void setupSaveButton() {
        Button saveButton = findViewById(R.id.settingsActivitySaveButton);
        saveButton.setOnClickListener(view -> {
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            EditText userNameEditText = findViewById(R.id.settingsActivityUsernamePrompt);
            String userNameString = userNameEditText.getText().toString();

            preferencesEditor.putString(USERNAME_TAG, userNameString);
            preferencesEditor.apply();

            Toast.makeText(SettingsActivity.this, "Saved Username!", Toast.LENGTH_LONG).show();
        });
    }
}