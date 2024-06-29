package com.example.doitall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private TaskDatabaseHelper dbHelper;

    public TaskAdapter(List<Task> taskList, Context context, TaskDatabaseHelper dbHelper) {
        this.taskList = taskList;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private TextView taskTextView;
        private TextView priorityTextView;
        private CheckBox taskCheckBox;
        private Button editButton;
        private Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.taskTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            taskCheckBox = itemView.findViewById(R.id.todoCheckBox);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Task task = taskList.get(position);
                        ((MainActivity) itemView.getContext()).openEditTaskDialog(task);
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Task task = taskList.get(position);
                        ((MainActivity) itemView.getContext()).deleteTask(task);
                    }
                }
            });

            taskCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Task task = taskList.get(position);
                        task.setCompleted(isChecked);
                        ((MainActivity) itemView.getContext()).updateTaskInList(task);
                    }
                }
            });
        }

        public void bind(Task task) {
            taskTextView.setText(task.getTaskText());
            taskCheckBox.setChecked(task.isCompleted());
            priorityTextView.setText("Priority: " + task.getPriority());
        }
    }
}
