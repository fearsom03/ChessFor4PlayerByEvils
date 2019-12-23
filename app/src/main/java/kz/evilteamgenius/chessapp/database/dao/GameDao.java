package kz.evilteamgenius.chessapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kz.evilteamgenius.chessapp.database.entitys.GameEntity;

@Dao
public interface GameDao {

    @Insert
    void insert(GameEntity game);

    @Delete
    void delete(GameEntity game);

    @Update
    void update(GameEntity... games);

    @Query("SELECT * FROM gameentity")
    List<GameEntity> getAllGame();

    @Query("SELECT * FROM gameentity WHERE id IN (:gameEntId)")
    List<GameEntity> loadGameById(int[] gameEntId);

    @Query("DELETE FROM GameEntity")
    void deleteAll();
}
