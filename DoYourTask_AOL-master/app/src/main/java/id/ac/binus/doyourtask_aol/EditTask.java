package id.ac.binus.doyourtask_aol;

import static android.graphics.Color.GRAY;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import id.ac.binus.doyourtask_aol.Model.TaskModel;
import id.ac.binus.doyourtask_aol.Utils.DatabaseHelper;

public class EditTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText editTitle, editDate, editDescription;
    private Button btnEdit;
    private DatabaseHelper db;

    private boolean isUpdate = false;
    private int taskIdToUpdate = -1;

    public static EditTask newInstance(){
        return new EditTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTitle = getView().findViewById(R.id.editTitle);
        editDate = getView().findViewById(R.id.editDate);
        editDescription = getView().findViewById(R.id.editDescritpion);

        btnEdit = getView().findViewById(R.id.BtnEditTask);

        db = new DatabaseHelper(getActivity());
        db.openDatabase();

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            taskIdToUpdate = bundle.getInt("id");
            String task = bundle.getString("task");
            editTitle.setText(task);
            if(task.length()>0){
                btnEdit.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            }
        }
        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    editTitle.setEnabled(false);
                    editTitle.setTextColor(GRAY);
                }else{
                    editTitle.setEnabled(true);
                    editTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //add new tasks
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTitle.getText().toString();
                String dueDate = editDate.getText().toString();
                String description = editDescription.getText().toString();

                db.updateTask(taskIdToUpdate, text, dueDate, description);

                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
