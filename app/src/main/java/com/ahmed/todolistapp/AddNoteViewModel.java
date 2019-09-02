package com.ahmed.todolistapp;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.ahmed.todolistapp.database.AppDataBase;
import com.ahmed.todolistapp.database.NoteModel;

public class AddNoteViewModel  extends ViewModel {

    private LiveData<NoteModel> note;

    public AddNoteViewModel(AppDataBase appDataBase,int noteId) {
        note = appDataBase.noteDao().loadNoteById(noteId);
    }

    public LiveData<NoteModel> getNote() {
        return note;
    }
}
