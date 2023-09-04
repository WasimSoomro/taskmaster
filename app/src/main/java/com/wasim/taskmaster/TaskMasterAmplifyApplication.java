package com.wasim.taskmaster;

import android.app.Application;
import android.util.Log;
import android.widget.Button;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.predictions.aws.AWSPredictionsPlugin;

public class TaskMasterAmplifyApplication extends Application {
    public static final String TAG = "taskmasterapplication";

    @Override
    public void onCreate() {
        super.onCreate();

        try
        {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin());
            Amplify.addPlugin(new AWSPredictionsPlugin());

        } catch (AmplifyException ae)
        {
            Log.e(TAG, "Error initializing Amplify: " + ae.getMessage(), ae);
        }
    }
}
