package kz.evilteamgenius.chessapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;
import kz.evilteamgenius.chessapp.engine.GameEngine;
import kz.evilteamgenius.chessapp.engine.Player;
import kz.evilteamgenius.chessapp.engine.pieces.Piece;

public class BoardView extends View {

    private Coordinate selection;
    private final Paint boardPaint = new Paint();
    private final Paint textPaint = new Paint();

    public BoardView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        textPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "Chess.ttf"));
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if ((GameEngine.myTurn() || GameEngine.match.isLocal) && !GameEngine.isGameOver()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int max = Board.extendedBoard ? 12 : 8;
                int x = (int) (event.getX() / getWidth() * max);
                int y = max - 1 - (int) (event.getY() / getWidth() * max);
//                Timber.d("selection: %s  %s ", x, y);
                Coordinate c = new Coordinate(x, y);
                if (c.isValid() && Board.getPiece(c, Board.BOARD) != null &&
                        Board.getPiece(c, Board.BOARD).getPlayerId().equals(GameEngine.currentPlayer())) {
                    selection = c;
//                    Timber.d("Selected!");
                    invalidate();
                } else {
                    if (selection != null) {
                        // we have a piece selected and clicked on a new position
                        if (Board.move(selection, c, getContext())) {
//                            Timber.d("Moved!");
                            selection = null;
                            invalidate();
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void draw(final Canvas canvas) {
        super.draw(canvas);
//        Timber.d("onDraw");
        int max = Board.extendedBoard ? 12 : 8;
        float cellWidth = canvas.getWidth() / (float) max;
        Coordinate c;
        Piece p;
        textPaint.setTextSize(cellWidth);
        float textOffset = 0.15f * cellWidth;
        boardPaint.reset();
        for (int x = 0; x < max; x++) {
            for (int y = 0; y < max; y++) {
                c = new Coordinate(x, y);
                if (c.isValid()) {
                    if ((x + y) % 2 == 0) boardPaint.setColor(Color.parseColor("#D4D4D4"));
                    else boardPaint.setColor(Color.parseColor("#9B9B9B"));
                    drawCoordinate(c, canvas, cellWidth, boardPaint, max);
                    if (isInEditMode()) continue;
                    p = Board.getPiece(c, Board.BOARD);
                    if (p != null) {
                        textPaint.setColor(GameEngine.getPlayerColor(p.getPlayerId()));
                        canvas.drawText(p.getString(), x * cellWidth,
                                (max - y) * cellWidth - textOffset, textPaint);
                    }
                }
            }
        }
        if (selection != null && (p = Board.getPiece(selection, Board.BOARD)) != null) {
            boardPaint.setAlpha(128);
            boardPaint.setColor(Color.CYAN);
            canvas.drawCircle(selection.x * cellWidth + cellWidth / 2,
                    (max - selection.y - 1) * cellWidth + cellWidth / 2, cellWidth / 2, boardPaint);
            textPaint.setColor(GameEngine.getPlayerColor(p.getPlayerId()));
            canvas.drawText(p.getString(), selection.x * cellWidth,
                    (max - selection.y) * cellWidth - 10, textPaint);
            for (Coordinate possible : p.getPossiblePositions(Board.BOARD)) {
                drawCoordinate(possible, canvas, cellWidth, boardPaint, max);
            }
        }
        // print last moves
        boardPaint.setStyle(Paint.Style.STROKE);
        boardPaint.setStrokeWidth(5f);
        for (Player player : GameEngine.players) {
            if (player.lastMove != null) {
                boardPaint.setColor(player.color);
//                Timber.d("draw lastMove: %s to %s "
//                        , player.lastMove.first.toString()
//                        , player.lastMove.second.toString());
                drawCoordinate(player.lastMove.first, canvas, cellWidth, boardPaint, max);
                drawCoordinate(player.lastMove.second, canvas, cellWidth, boardPaint, max);
            }
        }
    }

    /**
     * Draws a rectangle at the given coordinate
     *
     * @param c         the coordinate
     * @param canvas    the canvas to draw to
     * @param cellWidth the widht of each cell
     * @param paint     the paint
     */
    private void drawCoordinate(final Coordinate c, final Canvas canvas, final float cellWidth, final Paint paint, int max) {
        canvas.drawRect(c.x * cellWidth, (max - c.y - 1) * cellWidth, (c.x + 1) * cellWidth,
                (max - c.y) * cellWidth, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(Math.min(getMeasuredWidth(), getMeasuredHeight()),
                Math.min(getMeasuredWidth(), getMeasuredHeight()));
    }
}
