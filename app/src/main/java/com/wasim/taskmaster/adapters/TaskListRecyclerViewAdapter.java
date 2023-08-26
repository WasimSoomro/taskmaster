package com.wasim.taskmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wasim.taskmaster.MainActivity;
import com.wasim.taskmaster.R;
import com.wasim.taskmaster.activities.TaskDetailActivity;
import com.wasim.taskmaster.models.Task;

import java.util.List;

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
        String taskFragmentText = (position+1) + ". " + tasks.get(position).getTitle()
        + "\n" + tasks.get(position).getBody()
        + "\n" + tasks.get(position).getDateCreated()
        + "\n" + tasks.get(position).getCategory();

        taskFragmentTextView.setText(taskFragmentText);

        View taskViewHolder = holder.itemView;
        taskViewHolder.setOnClickListener(v -> {
            Intent goToTaskIntent = new Intent(callingActivity, TaskDetailActivity.class);
            //I followed along with video at 2:58:40
            goToTaskIntent.putExtra(MainActivity.TASK_TITLE_EXTRA_TAG, tasks.get(position).getTitle());
            goToTaskIntent.putExtra(MainActivity.TASK_DESCRIPTION_EXTRA_TAG, tasks.get(position).getBody());
            goToTaskIntent.putExtra(MainActivity.TASK_STATE_EXTRA_TAG, tasks.get(position).getState());
            callingActivity.startActivity(goToTaskIntent);
        });


    }

    @Override
    public int getItemCount() {

        return tasks.size();
    }

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {
        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
