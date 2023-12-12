package id.ac.binus.doyourtask_aol.Model;

public class TaskModel {
    private int taskID, taskStatus;
    private String taskTitle, taskDueDate, taskDescription;
    public TaskModel(int taskID, int taskStatus, String taskTitle, String taskDueDate, String taskDescription) {
        this.taskID = taskID;
        this.taskStatus = taskStatus;
        this.taskTitle = taskTitle;
        this.taskDueDate = taskDueDate;
        this.taskDescription = taskDescription;
    }

    public int getTaskID() {
        return taskID;
    }

    public int getTaskStatus() { return taskStatus; }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDueDate() {
        return taskDueDate;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

}
