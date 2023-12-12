package id.ac.binus.doyourtask_aol.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.binus.doyourtask_aol.AddNewTask;
import id.ac.binus.doyourtask_aol.MainActivity;
import id.ac.binus.doyourtask_aol.Model.TaskModel;
import id.ac.binus.doyourtask_aol.R;
import id.ac.binus.doyourtask_aol.Utils.DatabaseHelper;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<TaskModel> taskList;
    private MainActivity activity;
    private DatabaseHelper db;

    public TaskAdapter(DatabaseHelper db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        TaskModel item = taskList.get(position);
        holder.task.setText(item.getTaskTitle());
        holder.task.setChecked(toBoolean(item.getTaskStatus()));
        final int currentPosition = position;
        holder.task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    db.updateStatus(item.getTaskID(), 1);
                }else{
                    db.updateStatus(item.getTaskID(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private boolean toBoolean (int j){
        return j!=0;
    }

    public List<TaskModel> getTaskList() {
        return taskList;
    }

    public void setTask(List<TaskModel> taskList){
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position){
        TaskModel item = taskList.get(position);
        db.deleteTask(item.getTaskID());
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        TaskModel item = taskList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getTaskID());
        bundle.putString("task", item.getTaskTitle());
        bundle.putString("dueDate", item.getTaskDueDate());
        bundle.putString("description", item.getTaskDescription());

        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.checkBoxTask);
        }
    }

}
