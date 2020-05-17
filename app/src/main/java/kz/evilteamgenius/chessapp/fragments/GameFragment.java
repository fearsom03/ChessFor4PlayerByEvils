/*
 * Copyright 2014 Thomas Hoffmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kz.evilteamgenius.chessapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

import kz.evilteamgenius.chessapp.BoardView;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.api.loaders.LastMoveLoader;
import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;
import kz.evilteamgenius.chessapp.engine.Player;
import kz.evilteamgenius.chessapp.models.Game;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static kz.evilteamgenius.chessapp.extensions.ViewExtensionsKt.toast;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})
public class GameFragment extends Fragment {

    private TextView turn;
    private BoardView board;
    public String currentMatch;
    boolean infunc;

    private final Handler handler = new Handler();
    private Timer timer = new Timer();
    Runnable runnable = new Runnable() {
        public void run() {
            getLastMove();
        }
    };

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentMatch = getArguments().getString("matchID");
        //Main.gameFragment = this;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (kz.evilteamgenius.chessapp.engine.Game.match == null) {
            // ((Main) getActivity()).showStartFragment();
            return null;
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        turn = v.findViewById(R.id.turn);
        board = v.findViewById(R.id.board);
        kz.evilteamgenius.chessapp.engine.Game.UI = this;
        updateTurn();

        if (!kz.evilteamgenius.chessapp.engine.Game.match.isLocal){
            callAsynchronousTask();
            infunc = false;
        }

        //TODO: remove websocket get move with polling
//        if (!Game.match.isLocal) {
//            ((MainActivity) getActivity()).getMove(board);
//        }
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!kz.evilteamgenius.chessapp.engine.Game.isGameOver()) kz.evilteamgenius.chessapp.engine.Game.save(getActivity());
    }


    /**
     * Updates the board
     *
     * @param gameOver true if the game is already over
     */
    public void update(boolean gameOver) {
        Timber.d(" UI:update() " + gameOver + " board=null?" + (board == null));
        if (board == null) return;
        board.invalidate();
        if (!gameOver) updateTurn();
    }

    /**
     * Called when the game is over
     *
     * @param win true if this player won
     */
    public void gameOver(boolean win) {
        if (turn == null || getActivity() == null) return;

        String winText = getString(R.string.gameover) + "\n" + getString(R.string.win);
        String loseText = getString(R.string.gameover) + "\n" + getString(R.string.loss);
        turn.setText((win ? winText : loseText));
    }

    /**
     * Called when a local match is over.
     * Also deletes the data for this local match
     *
     * @param winnerPlayer the player who won the match
     */
    public void gameOverLocal(final Player winnerPlayer) {
        if (turn == null || getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            String text = getString(R.string.gameover)
                    + "\n" + " "
                    + getString(R.string.winlocal,
                    kz.evilteamgenius.chessapp.engine.Game.match.mode == kz.evilteamgenius.chessapp.engine.Game.MODE_4_PLAYER_TEAMS
                            ? "Team " + winnerPlayer.team :
                            winnerPlayer.name);
            turn.setText(text);

            // Stuff that updates the UI

        });
        getActivity().getSharedPreferences("localMatches", Context.MODE_PRIVATE).edit()
                .remove("match_" + kz.evilteamgenius.chessapp.engine.Game.match.id + "_" + kz.evilteamgenius.chessapp.engine.Game.match.mode).apply();
        Timber.d("Deleting match_%s_%s", kz.evilteamgenius.chessapp.engine.Game.match.id, kz.evilteamgenius.chessapp.engine.Game.match.mode);
    }

    /**
     * Update the 'current turn' information view
     */
    public void updateTurn() {
        Timber.d(" UI:updateTurn() turn=null?%s", (turn == null));
        if (turn == null) return;
        if (kz.evilteamgenius.chessapp.engine.Game.isGameOver()) {
            gameOver(kz.evilteamgenius.chessapp.engine.Game.getWinnerTeam() == kz.evilteamgenius.chessapp.engine.Game.getPlayer(kz.evilteamgenius.chessapp.engine.Game.myPlayerId).team);
        } else {
            StringBuilder sb = new StringBuilder();
            String current = kz.evilteamgenius.chessapp.engine.Game.players[kz.evilteamgenius.chessapp.engine.Game.turns % kz.evilteamgenius.chessapp.engine.Game.players.length].id;
            Timber.d(" current player: %s", current);
            for (Player p : kz.evilteamgenius.chessapp.engine.Game.players) {
                Timber.d(" UI:updateTurn() player " + p.id + " " + p.name + " " + p.team);
                sb.append("<font color='")
                        .append(String.format("#%06X", (0xFFFFFF & kz.evilteamgenius.chessapp.engine.Game.getPlayerColor(p.id))))
                        .append("'>");
                if (p.id.equals(current)) sb.append("-> ");
                if (kz.evilteamgenius.chessapp.engine.Game.match.mode == kz.evilteamgenius.chessapp.engine.Game.MODE_4_PLAYER_TEAMS) {
                    sb.append(p.name).append(" [").append(p.team).append("]</font><br />");
                } else {
                    sb.append(p.name).append("</font><br />");
                }
            }
            sb.delete(sb.lastIndexOf("<br />"), sb.length());
            getActivity().runOnUiThread(() -> {

                // Stuff that updates the UI
                turn.setText(Html.fromHtml(sb.toString()));

            });
        }
    }

    private void getLastMove() {
        if(infunc)
            return;
        infunc = true;
        String token = getToken();
//        toast(getContext(),token);
        LastMoveLoader lastMoveLoader = new LastMoveLoader(new LastMoveLoader.LastMoveCallback() {
            @Override
            public void onMoveLoaded(Game game) {
                if ( !game.getMade_by().equals(kz.evilteamgenius.chessapp.engine.Game.myPlayerUserame) && !game.getMade_by().isEmpty()){
                    Coordinate pos1 = new Coordinate(game.getFrom_x(), game.getFrom_y(), Board.rotations);
                    Coordinate pos2 = new Coordinate(game.getTo_x(), game.getTo_y(), Board.rotations);
//                    System.out.println("testFunc: " + Calendar.getInstance().getTime() + " " +  pos1.toString());
                    Board.moveWhenReceived(pos1, pos2);
                    toast(getContext(), game.toString());
                    getActivity().runOnUiThread(board::invalidate);
                    kz.evilteamgenius.chessapp.engine.Game.game = game;
                }
            }

            @Override
            public void onResponseFailed(String errorMessage) {
                toast(getContext(), errorMessage);
            }
        });
        lastMoveLoader.getLastMove(token, kz.evilteamgenius.chessapp.engine.Game.game.getId());
        infunc = false;
    }


    private String getToken() {
        SharedPreferences preferences = requireContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("token", "");
    }

    public void callAsynchronousTask() {
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000); //execute in every 50000 ms
    }
}
