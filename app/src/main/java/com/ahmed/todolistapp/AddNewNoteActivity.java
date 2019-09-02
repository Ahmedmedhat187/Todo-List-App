package com.ahmed.todolistapp;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.ahmed.todolistapp.database.AppDataBase;
import com.ahmed.todolistapp.database.NoteModel;
import java.util.Date;

public class AddNewNoteActivity extends AppCompatActivity {

    private static final String TAG = AddNewNoteActivity.class.getSimpleName();
    public static final String EXTRA_TASK_ID = "extraTaskId";
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    private static final int DEFAULT_TASK_ID = -1;

    private int mTaskId = DEFAULT_TASK_ID;
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;
    AppDataBase mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        initViews();
        mDataBase =  AppDataBase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button);
            if (mTaskId == DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                // final LiveData<NoteModel> note = mDataBase.noteDao().loadNoteById(mTaskId);
                // note.observe();

                AddNoteViewModelFactory factory = new AddNoteViewModelFactory(mDataBase , mTaskId);
                final AddNoteViewModel viewModel = ViewModelProviders.of(this , factory).get(AddNoteViewModel.class);
                viewModel.getNote().observe(this, new Observer<NoteModel>() {
                    @Override
                    public void onChanged(NoteModel noteModel) {
                        Log.e(TAG , "update data from live data");
                        viewModel.getNote().removeObserver(this);
                        populateUI(noteModel);
                    }
                });
            }
        }


    }


    public void populateUI(NoteModel noteModel){
        if (noteModel == null){
            return;
        }
        mEditText.setText(noteModel.getDescription());
        setPriorityInViews(noteModel.getPriority());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }




    private void initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription);
        mRadioGroup = findViewById(R.id.radioGroup);
        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSavedButtonClicked();
        }});
    }



    public void onSavedButtonClicked(){
        String description  = mEditText.getText().toString();
        int priority = getPriorityFromViews();

        final NoteModel noteModel = new NoteModel(description, priority, new Date());
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(mTaskId == DEFAULT_TASK_ID){
                    mDataBase.noteDao().insertNote(noteModel);
                }else {
                    noteModel.setId(mTaskId);
                    mDataBase.noteDao().updateNote(noteModel);
                }
                finish();
        }});
    }


    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }


    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }

}
