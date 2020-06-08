
package kz.evilteamgenius.chessapp.engine.pieces;

import java.util.LinkedList;
import java.util.List;

import kz.evilteamgenius.chessapp.engine.Coordinate;

public class King extends Piece {

    public King(final Coordinate p, final String o) {
        super(p, o);
    }

    @Override
    public List<Coordinate> getPossiblePositions(Piece[][] board) {
        List<Coordinate> re = new LinkedList<>();
        int x = position.x;
        int y = position.y;
        Coordinate c;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                c = new Coordinate(x + i, y + j);
                if (c.isValid() && sameTeam(c, board)) {
                    re.add(c);
                }
            }
        }
        return re;
    }

    @Override
    public String getString() {
        return "\u265B";
    }
}
