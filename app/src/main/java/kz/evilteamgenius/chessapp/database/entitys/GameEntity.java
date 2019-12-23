package kz.evilteamgenius.chessapp.database.entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class GameEntity {
    @PrimaryKey
    public Long id;
    @ColumnInfo
    public String white;
    @ColumnInfo
    public String black;
    @ColumnInfo
    public String fen;
    @ColumnInfo
    public String result;
    @ColumnInfo
    public int from_x;
    @ColumnInfo
    public int from_y;
    @ColumnInfo
    public int to_x;
    @ColumnInfo
    public int to_y;
    @ColumnInfo
    public String next_move;

    @Override
    public String toString() {
        return "GameEntity{" +
                "id=" + id +
                ", white='" + white + '\'' +
                ", black='" + black + '\'' +
                ", fen='" + fen + '\'' +
                ", result='" + result + '\'' +
                ", from_x=" + from_x +
                ", from_y=" + from_y +
                ", to_x=" + to_x +
                ", to_y=" + to_y +
                ", next_move='" + next_move + '\'' +
                '}';
    }
}
