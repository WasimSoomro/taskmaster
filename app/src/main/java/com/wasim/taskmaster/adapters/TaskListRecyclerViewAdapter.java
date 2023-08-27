package com.wasim.taskmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.wasim.taskmaster.MainActivity;
import com.wasim.taskmaster.R;
import com.wasim.taskmaster.activities.TaskDetailActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.TaskListViewHolder> {
    List<Task> tasks;
    Context callingActivity;
    public TaskListRecyclerViewAdapter(List<Task> tasks,Context callingActivity) {
        this.tasks = tasks;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list, parent, false);
        return new TaskListViewHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {

        TextView taskFragmentTextView = (TextView) holder.itemView.findViewById(R.id.taskFragmentTextView);
        String dateString = formatDateString(tasks.get(position));
        String taskFragmentText = (position+1) + ". " + tasks.get(position).getTitle()
        + "\n" + tasks.get(position).getBody()
//        + "\n" + tasks.get(position).getDateCreated()
                + "\n" + dateString
        + "\n" + tasks.get(position).getTaskCategory();

        taskFragmentTextView.setText(taskFragmentText);

        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(v -> {
            Intent goToTaskIntent = new Intent(callingActivity, TaskDetailActivity.class);
            //I followed along with video at 2:58:40
            goToTaskIntent.putExtra(MainActivity.TASK_TITLE_EXTRA_TAG, tasks.get(position).getTitle());
            goToTaskIntent.putExtra(MainActivity.TASK_DESCRIPTION_EXTRA_TAG, tasks.get(position).getBody());
            goToTaskIntent.putExtra(MainActivity.TASK_STATE_EXTRA_TAG, tasks.get(position).getTaskCategory());
            callingActivity.startActivity(goToTaskIntent);
        });
    }
    @Override
    public int getItemCount() {

        return tasks.size();
    }

    private String formatDateString(Task task) {
        DateFormat dateCreatedIso8601InputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        dateCreatedIso8601InputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat dateCreatedOutputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateCreatedOutputFormat.setTimeZone(TimeZone.getDefault());
        String dateCreatedString = "";

        try {
            Date dateCreatedJavaDate = dateCreatedIso8601InputFormat.parse(task.getDateCreated().format());
            if (dateCreatedJavaDate != null) {
                dateCreatedString = dateCreatedOutputFormat.format(dateCreatedJavaDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateCreatedString;
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
