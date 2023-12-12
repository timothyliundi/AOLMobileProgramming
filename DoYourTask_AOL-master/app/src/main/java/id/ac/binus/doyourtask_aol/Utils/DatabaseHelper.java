package id.ac.binus.doyourtask_aol.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import id.ac.binus.doyourtask_aol.Model.TaskModel;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "DoYourTaskDatabase";
    private static final String TASK_TABLE = "task";
    private static final String ID = "id";
    private static final String STATUS = "status";
    private static final String TITLE = "title";
    private static final String DATE = "dueDate";
    private static final String DESC = "description";

    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + TASK_TABLE + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            STATUS + " INTEGER, " + TITLE + " TEXT, " + DATE + " TEXT, " + DESC + " TEXT)";

    private SQLiteDatabase db;
    public DatabaseHelper(@Nullable Context context) {
        super(context, "NAME", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TASK_TABLE);
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public boolean insertData(TaskModel task){

        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, task.getTaskStatus());
        contentValues.put(TITLE, task.getTaskTitle());
        contentValues.put(DATE, task.getTaskDueDate());
        contentValues.put(DESC, task.getTaskDescription());

        long result = db.insert("task", null, contentValues);

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public List<TaskModel> getAllTask(){
        List<TaskModel> taskList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(TASK_TABLE, null, null,
                    null, null, null, null,
                    null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(ID));
                        @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(STATUS));
                        @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(TITLE));
                        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DATE));
                        @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DESC));

                        TaskModel task = new TaskModel(id, status, title, date, description);

                        taskList.add(task);
                    } while (cursor.moveToNext());
                }
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return taskList;
    }

    @SuppressLint("Range")
    public TaskModel getTask(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        TaskModel taskModel = null;

        Cursor cursor = db.query(
                TASK_TABLE, // Table name
                new String[]{ID, STATUS, TITLE, DATE, DESC},
                ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            taskModel = new TaskModel(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getInt(cursor.getColumnIndex(STATUS)),
                    cursor.getString(cursor.getColumnIndex(TITLE)),
                    cursor.getString(cursor.getColumnIndex(DATE)),
                    cursor.getString(cursor.getColumnIndex(DESC))
            );

            cursor.close();
        }

        return taskModel;
    }

    public void updateStatus(int id, int status){
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TASK_TABLE, contentValues, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String title, String dueDate, String description) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, title);
        contentValues.put(DATE, dueDate);
        contentValues.put(DESC, description);
        db.update(TASK_TABLE, contentValues, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TASK_TABLE,ID + "=?", new String[] {String.valueOf(id)});
    }

}
