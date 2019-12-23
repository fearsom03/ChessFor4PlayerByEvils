package kz.evilteamgenius.chessapp.database;

import android.content.Context;

import androidx.room.Room;

public class AppDatabaseWrapper {
    private static AppDatabase db = null;

    public static AppDatabase GetDatabase(Context context) {
            if (db==null){
                db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "chessApp.db").allowMainThreadQueries().build();
            }
            return db;
    }

    public static void CloseDatabase()
    {
        if (db != null && !db.inTransaction()) {
            if (db.isOpen()) {
                db.close();
            }

            db = null;
        }
    }

}
