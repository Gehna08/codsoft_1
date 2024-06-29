package com.example.doitall;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private TaskDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportActionBar().hide();

        dbHelper = new TaskDatabaseHelper(this);
        taskList = dbHelper.getAllTasks();


        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(taskList, this, dbHelper);
        tasksRecyclerView.setAdapter(taskAdapter);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewTaskDialog();
            }
        });
    }

    private void openNewTaskDialog() {
        NewTaskDialog newTaskDialog = new NewTaskDialog();
        newTaskDialog.setOnTaskAddedListener(new NewTaskDialog.OnTaskAddedListener() {
            @Override
            public void onTaskAdded(String taskText, int priority) {
                Task newTask = new Task(taskText, priority, false);
                long id = dbHelper.insertTask(newTask);
                newTask.setId(id);
                taskList.add(newTask);
                taskAdapter.notifyDataSetChanged();
            }
        });
        newTaskDialog.show(getSupportFragmentManager(), "NewTaskDialog");
    }





    public void openEditTaskDialog(final Task task) {
        EditTaskDialog editTaskDialog = EditTaskDialog.newInstance(task);
        editTaskDialog.setOnTaskEditedListener(new EditTaskDialog.OnTaskEditedListener() {
            @Override
            public void onTaskEdited(Task editedTask) {
                dbHelper.updateTask(editedTask);
                taskAdapter.notifyDataSetChanged();
            }
        });
        editTaskDialog.show(getSupportFragmentManager(), "EditTaskDialog");
    }

    public void deleteTask(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteTask(task);
                        removeTaskFromList(task);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    void updateTaskInList(final Task updatedTask) {
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.getId() == updatedTask.getId()) {
                taskList.set(i, updatedTask);
                final int index = i;
                tasksRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        taskAdapter.notifyItemChanged(index);
                    }
                });
                break;
            }
        }
    }

    private void removeTaskFromList(final Task taskToRemove) {
        final int position = taskList.indexOf(taskToRemove);
        if (position != -1) {
            taskList.remove(position);
            tasksRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    taskAdapter.notifyItemRemoved(position);
                    taskAdapter.notifyItemRangeChanged(position, taskList.size());
                }
            });
        }
    }

    public void showDeleteTaskDialog(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(task);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}

