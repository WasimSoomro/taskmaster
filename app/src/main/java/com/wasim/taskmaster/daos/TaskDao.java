package com.wasim.taskmaster.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.wasim.taskmaster.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public void insertATask(Task task);

    @Query("SELECT * FROM Task")
    public List<Task> findAll();

    @Query("SELECT * FROM Task ORDER BY title ASC")
    public List<Task> findAllSortedByName();

    @Query("SELECT * FROM Task WHERE id = :id")
    Task findByAnId(long id);
}
