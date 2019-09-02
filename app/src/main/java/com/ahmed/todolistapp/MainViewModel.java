package com.ahmed.todolistapp;
import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ahmed.todolistapp.database.AppDataBase;
import com.ahmed.todolistapp.database.NoteModel;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<NoteModel>> notes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.e(TAG , "get data from database");
        AppDataBase dataBase = AppDataBase.getInstance(this.getApplication());
        notes = dataBase.noteDao().getAllNotes();
    }


    public LiveData<List<NoteModel>> getNotes() {
        return notes;
    }
}
