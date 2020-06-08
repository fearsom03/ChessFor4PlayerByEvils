
package kz.evilteamgenius.chessapp.engine.pieces;

import java.util.LinkedList;
import java.util.List;

import kz.evilteamgenius.chessapp.engine.Coordinate;

public class Knight extends Piece {

    public Knight(final Coordinate p, final String o) {
        super(p, o);
    }

    @Override
    public List<Coordinate> getPossiblePositions(Piece[][] board) {
        List<Coordinate> re = new LinkedList<>();
        Coordinate c = new Coordinate(position.x + 2, position.y + 1);
        if (c.isValid() && sameTeam(c, board)) re.add(c);

        c = new Coordinate(position.x + 2, position.y - 1);
        if (c.isValid() && sameTeam(c, board)) re.add(c);

        c = new Coordinate(position.x - 2, position.y + 1);
        if (c.isValid() && sameTeam(c, board)) re.add(c);

        c = new Coordinate(position.x - 2, position.y - 1);
        if (c.isValid() && sameTeam(c, board)) re.add(c);

        c = new Coordinate(position.x + 1, position.y + 2);
        if (c.isValid() && sameTeam(c, board)) re.add(c);

        c = new Coordinate(position.x - 1, position.y + 2);
        if (c.isValid() && sameTeam(c, board)) re.add(c);

        c = new Coordinate(position.x + 1, position.y - 2);
        if (c.isValid() && sameTeam(c, board)) re.add(c);

        c = new Coordinate(position.x - 1, position.y - 2);
        if (c.isValid() && sameTeam(c, board)) re.add(c);

        return re;
    }

    @Override
    public String getString() {
        return "\u265E";
    }
}
