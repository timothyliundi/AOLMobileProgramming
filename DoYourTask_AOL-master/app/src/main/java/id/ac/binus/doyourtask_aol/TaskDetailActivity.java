package id.ac.binus.doyourtask_aol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import id.ac.binus.doyourtask_aol.Model.TaskModel;
import id.ac.binus.doyourtask_aol.Utils.DatabaseHelper;

public class TaskDetailActivity extends AppCompatActivity implements DialogCloseListener {

    private TextView tvTitleDetail, tvDueDateDetail, tvDescriptionDetail;
    private Button btnEdit;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        tvTitleDetail = findViewById(R.id.tvTitleDetail);
        tvDueDateDetail = findViewById(R.id.tvDueDateDetail);
        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
//        btnEdit = findViewById(R.id.btnEdit);

        Intent intent = getIntent();
        if(intent != null){
            String title = intent.getStringExtra("title");
            String dueDate = intent.getStringExtra("dueDate");
            String description = intent.getStringExtra("description");

            tvTitleDetail.setText(title);
            tvDueDateDetail.setText("Due Date: " + dueDate);
            tvDescriptionDetail.setText("Description: " + description);
        }

//        btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EditTask.newInstance().show(getSupportFragmentManager(), EditTask.TAG);
//            }
//        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        Intent intent = new Intent();
        displayTaskDetails(intent.getIntExtra("id", -1));
    }

    private void displayTaskDetails(int taskId) {
        TaskModel taskModel = db.getTask(taskId);
        if (taskModel != null) {
            tvTitleDetail.setText(taskModel.getTaskTitle());
            tvDueDateDetail.setText(taskModel.getTaskDueDate());
            tvDescriptionDetail.setText(taskModel.getTaskDescription());
        }
    }
}