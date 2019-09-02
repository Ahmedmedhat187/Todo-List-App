package com.ahmed.todolistapp;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.ahmed.todolistapp.database.AppDataBase;

public class AddNoteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDataBase mDataBase;
    private final int mNoteId;

    public AddNoteViewModelFactory(AppDataBase dataBase, int noteId) {
        mDataBase = dataBase;
        mNoteId = noteId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddNoteViewModel(mDataBase , mNoteId);
    }
}
