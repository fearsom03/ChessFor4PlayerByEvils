package kz.evilteamgenius.chessapp.engine;

import android.util.Pair;

import java.util.LinkedList;
import java.util.List;

import kz.evilteamgenius.chessapp.engine.pieces.King;
import kz.evilteamgenius.chessapp.engine.pieces.Piece;

public class Player {

    public final String id;
    public final int team;
    public final int color;
    public final String name;
    public Pair<Coordinate, Coordinate> lastMove;
    public List<Piece> pieces = new LinkedList<>();
    public King king;

    public Player(final String i, int t, int c, final String n) {
        id = i;
        team = t;
        color = c;
        name = n;
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Player && ((Player) other).id.equals(id);
    }
}
