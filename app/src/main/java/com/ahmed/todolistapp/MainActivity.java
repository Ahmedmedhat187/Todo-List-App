package com.ahmed.todolistapp;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import com.ahmed.todolistapp.database.AppDataBase;
import com.ahmed.todolistapp.database.NoteModel;
import java.util.List;
import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity  implements NoteAdapter.ItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();;
    private FloatingActionButton fabButton;
    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;

    AppDataBase mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), VERTICAL));
        mAdapter = new NoteAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position =  viewHolder.getAdapterPosition();
                        List<NoteModel> noteModels = mAdapter.getNoteEntries();
                        mDataBase.noteDao().deleteNote(noteModels.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);


        fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewNoteIntent = new Intent(MainActivity.this, AddNewNoteActivity.class);
                startActivity(addNewNoteIntent);
            }
        });

        mDataBase =  AppDataBase.getInstance(getApplicationContext());
        setupViewModel();
    }





    public void setupViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getNotes().observe(this, new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(List<NoteModel> noteModels) {
                Log.e(TAG , "update data from livedata in viewmodel");
                mAdapter.setTasks(noteModels);
            }
        });
    }


    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(this , AddNewNoteActivity.class);
        intent.putExtra(AddNewNoteActivity.EXTRA_TASK_ID , itemId);
        startActivity(intent);
    }
}
