package com.ahmed.todolistapp.database;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM  note ORDER BY priority")
    LiveData<List<NoteModel>> getAllNotes();

    @Insert
    void insertNote(NoteModel note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(NoteModel note);

    @Delete
    void deleteNote(NoteModel note);

    @Query("SELECT * FROM  note WHERE id =:id")
    LiveData<NoteModel> loadNoteById(int id);

}
