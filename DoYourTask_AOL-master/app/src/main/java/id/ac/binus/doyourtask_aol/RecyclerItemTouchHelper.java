package id.ac.binus.doyourtask_aol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.binus.doyourtask_aol.Adapter.TaskAdapter;
import id.ac.binus.doyourtask_aol.Model.TaskModel;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private TaskAdapter taskAdapter;

    public RecyclerItemTouchHelper(TaskAdapter taskAdapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.taskAdapter = taskAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        TaskModel taskList = taskAdapter.getTaskList().get(position);
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(taskAdapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this Task?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    taskAdapter.deleteItem(position);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    taskAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (direction == ItemTouchHelper.RIGHT) {
            Intent intent = new Intent(taskAdapter.getContext(), TaskDetailActivity.class);
            intent.putExtra("title", taskList.getTaskTitle());
            intent.putExtra("dueDate", taskList.getTaskDueDate());
            intent.putExtra("description", taskList.getTaskDescription());
            taskAdapter.getContext().startActivity(intent);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    taskAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }, 500);

        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if(dX > 0){
            icon = ContextCompat.getDrawable(taskAdapter.getContext(), R.drawable.baseline_notes_24);
            background = new ColorDrawable(ContextCompat.getColor(taskAdapter.getContext(), R.color.colorPrimaryDark));
        }else{
            icon = ContextCompat.getDrawable(taskAdapter.getContext(), R.drawable.baseline_delete_24);
            background = new ColorDrawable(Color.RED);
        }

        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) {
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;

            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else {
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
    }
}
