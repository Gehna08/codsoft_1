package com.example.doitall;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class NewTaskDialog extends DialogFragment {

    private EditText newTaskText;
    private EditText priorityEditText;
    private Button newTaskButton;
    private OnTaskAddedListener onTaskAddedListener;

    public void setOnTaskAddedListener(OnTaskAddedListener onTaskAddedListener) {
        this.onTaskAddedListener = onTaskAddedListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.new_task, null);
        newTaskText = view.findViewById(R.id.newTaskTest);
        priorityEditText = view.findViewById(R.id.priorityEditText);
        newTaskButton = view.findViewById(R.id.newTaskButton);

        newTaskButton.setOnClickListener(v -> {
            String taskText = newTaskText.getText().toString();
            String priorityText = priorityEditText.getText().toString();
            if (!taskText.isEmpty() && !priorityText.isEmpty() && onTaskAddedListener != null) {
                int priority = Integer.parseInt(priorityText);
                onTaskAddedListener.onTaskAdded(taskText, priority);
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    public interface OnTaskAddedListener {
        void onTaskAdded(String taskText, int priority);
    }
}
