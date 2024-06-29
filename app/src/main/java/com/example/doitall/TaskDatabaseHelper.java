package com.example.doitall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task_database";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK_TEXT = "task_text";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_DUE_DATE = "due_date";
    private static final String COLUMN_COMPLETED = "completed";

    // SQL statement to create the tasks table
    private static final String SQL_CREATE_TASKS =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TASK_TEXT + " TEXT," +
                    COLUMN_PRIORITY + " INTEGER DEFAULT 0," +
                    COLUMN_DUE_DATE + " TEXT," +
                    COLUMN_COMPLETED + " INTEGER DEFAULT 0)";



    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TEXT, task.getTaskText());
        values.put(COLUMN_PRIORITY, task.getPriority());
        values.put(COLUMN_DUE_DATE, task.getDueDate());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);

        // Insert row
        long id = db.insert(TABLE_TASKS, null, values);

        // Close database connection
        db.close();

        return id; // Return the id of the inserted row
    }


    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TEXT, task.getTaskText());
        values.put(COLUMN_PRIORITY, task.getPriority()); // Ensure priority is correctly set
        values.put(COLUMN_DUE_DATE, task.getDueDate());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);

        db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }


    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))); // Ensure correct handling
                task.setTaskText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TEXT)));
                task.setPriority(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY)));
                task.setDueDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE)));
                task.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1);
                taskList.add(task);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return taskList;
    }
}
