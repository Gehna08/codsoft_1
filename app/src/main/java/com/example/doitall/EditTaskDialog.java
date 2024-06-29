package com.example.doitall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.doitall.R;
import com.example.doitall.Task;

public class EditTaskDialog extends DialogFragment {

    private static final String ARG_TASK = "task";

    private EditText editTaskEditText;
    private Button saveButton;
    private Task task;
    private OnTaskEditedListener listener;

    public static EditTaskDialog newInstance(Task task) {
        EditTaskDialog dialog = new EditTaskDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_task_dialog, container, false);
        editTaskEditText = view.findViewById(R.id.editTaskText);
        saveButton = view.findViewById(R.id.saveButton);

        if (getArguments() != null) {
            task = (Task) getArguments().getSerializable(ARG_TASK);
            if (task != null) {
                editTaskEditText.setText(task.getTaskText());
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task != null) {
                    task.setTaskText(editTaskEditText.getText().toString());
                    if (listener != null) {
                        listener.onTaskEdited(task);
                    }
                    dismiss();
                }
            }
        });

        return view;
    }

    public void setOnTaskEditedListener(OnTaskEditedListener listener) {
        this.listener = listener;
    }

    public interface OnTaskEditedListener {
        void onTaskEdited(Task editedTask);
    }
}
