package kz.evilteamgenius.chessapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import kz.evilteamgenius.chessapp.database.dao.GameDao;
import kz.evilteamgenius.chessapp.database.entitys.GameEntity;

@Database(entities = {
        GameEntity.class,
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GameDao gameDao();
}
