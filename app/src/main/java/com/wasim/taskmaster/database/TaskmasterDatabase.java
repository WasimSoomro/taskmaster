package com.wasim.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.wasim.taskmaster.daos.TaskDao;
import com.wasim.taskmaster.models.Task;

@TypeConverters({TaskmasterDatabaseClassConverters.class})
@Database(entities = {Task.class}, version = 1)
public abstract class TaskmasterDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

}
