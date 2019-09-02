package com.ahmed.todolistapp.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {NoteModel.class} , version = 1 , exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDataBase extends RoomDatabase {

    private static final String TAG = AppDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "todolist";
    private static AppDataBase sInstance;

    public static AppDataBase getInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract NoteDao noteDao();

}
