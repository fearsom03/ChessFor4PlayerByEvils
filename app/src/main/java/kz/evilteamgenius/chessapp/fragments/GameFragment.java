package kz.evilteamgenius.chessapp.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kz.evilteamgenius.chessapp.BoardView;
import kz.evilteamgenius.chessapp.Const;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.api.loaders.LastMoveLoader;
import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;
import kz.evilteamgenius.chessapp.engine.GameEngine;
import kz.evilteamgenius.chessapp.engine.Player;
import kz.evilteamgenius.chessapp.models.Game;
import kz.evilteamgenius.chessapp.viewModels.GameViewModel;
import timber.log.Timber;

import static android.content.Context.MODE_PRIVATE;
import static kz.evilteamgenius.chessapp.extensions.LifecycleExtensionKt.getToken;
import static kz.evilteamgenius.chessapp.extensions.ViewExtensionsKt.toast;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})
public class GameFragment extends Fragment {

    @BindView(R.id.previousMusic)
    ImageView previousMusic;
    @BindView(R.id.pauseMusic)
    ImageView pauseMusic;
    @BindView(R.id.playMusic)
    ImageView playMusic;
    @BindView(R.id.nextMusic)
    ImageView nextMusic;
    @BindView(R.id.board)
    BoardView board;
    @BindView(R.id.turn)
    TextView turn;
    @BindView(R.id.namesLinear)
    LinearLayout linearLayout;
    @BindView(R.id.musicLine)
    LinearLayout musicLine;
    @BindView(R.id.wallBackground)
    RelativeLayout wallBackground;

    private String currentMatch;
    private boolean infunc;
    private GameViewModel viewModel;
    private final Handler handler = new Handler();
    private Timer timer = new Timer();
    private Runnable runnable = this::getLastMove;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentMatch = getArguments().getString("matchID");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        viewModel.getMusicValue().observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                playMusic.setBackgroundColor(Color.GREEN);
                pauseMusic.setBackgroundColor(Color.TRANSPARENT);
            } else {
                playMusic.setBackgroundColor(Color.TRANSPARENT);
                pauseMusic.setBackgroundColor(Color.GREEN);
            }
        });
        viewModel.getSelectedStyle().observe(requireActivity(), integer -> {
            try {
                switch (integer) {
                    case 0:
                        wallBackground.setBackground(requireActivity().getResources().getDrawable(R.drawable.back_grad));
                        break;
                    case 1:
                        wallBackground.setBackground(requireActivity().getResources().getDrawable(R.drawable.back_grad_1));
                        break;
                    case 2:
                        wallBackground.setBackground(requireActivity().getResources().getDrawable(R.drawable.back_grad_3));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        initViews();
        if (!GameEngine.match.isLocal) {
            toast(getContext(), getResources().getString(R.string.gameStartedMsg));
            callAsynchronousTask();
            infunc = false;
        }
        GameEngine.UI = this;
        updateTurn();
    }

    private void initViews() {
        SharedPreferences preferences
                = requireActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean forMusic = preferences.getBoolean(Const.PLAY_MUSIC, true);
        if (forMusic) {
            musicLine.setVisibility(View.VISIBLE);
        } else {
            musicLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!GameEngine.isGameOver()) {
            //here we need use ROOM
            //todo add to room

            // then it'll be much easier to start match again
            GameEngine.save(getActivity());
        }
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
            @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String text = getString(R.string.gameover)
                    + "\n" + " "
                    + getString(R.string.winlocal,
                    GameEngine.match.mode == GameEngine.MODE_4_PLAYER_TEAMS
                            ? "Team " + winnerPlayer.team :
                            winnerPlayer.name);
            turn.setText(text);

            // Stuff that updates the UI
        });
//        getActivity().getSharedPreferences("localMatches", Context.MODE_PRIVATE).edit()
//                .remove("match_" + GameEngine.match.id + "_" + GameEngine.match.mode).apply();
//        Timber.d("Deleting match_%s_%s", GameEngine.match.id, GameEngine.match.mode);
    }

    /**
     * Update the 'current turn' information view
     */
    public void updateTurn() {
        Timber.d(" UI:updateTurn() turn=null?%s", (turn == null));
        if (turn == null) return;
        if (GameEngine.isGameOver()) {
            gameOver(GameEngine.getWinnerTeam() == GameEngine.getPlayer(GameEngine.myPlayerId).team);
        } else {
            StringBuilder sb = new StringBuilder();
            String current = GameEngine.players[GameEngine.turns % GameEngine.players.length].id;
            Timber.d(" current player: %s", current);
            ArrayList<TextView> arr = new ArrayList<>();
            for (Player p : GameEngine.players) {
                sb = new StringBuilder();
                Timber.d(" UI:updateTurn() player " + p.id + " " + p.name + " " + p.team);
                sb.append("<font color='")
                        .append(String.format("#%06X", (0xFFFFFF & GameEngine.getPlayerColor(p.id))))
                        .append("'>");
                sb.append(getString(R.string.beReady)).append(" -- ");
                if (GameEngine.match.mode == GameEngine.MODE_4_PLAYER_TEAMS) {
                    sb.append(p.name).append(" [").append(p.team).append("]</font><br />");
                } else {
                    sb.append(p.name).append("</font><br />");
                }
                TextView t = new TextView(requireContext());
                t.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                t.setText(Html.fromHtml(sb.toString()));
                t.setBackgroundColor(GameEngine.getPlayerColor(current));
                t.setPadding(10, 10, 10, 10);
                t.setGravity(Gravity.CENTER);
                arr.add(t);
            }
            sb.delete(sb.lastIndexOf("<br />"), sb.length());
            requireActivity().runOnUiThread(() -> {
//                turn.setText(Html.fromHtml(sb.toString()));
                linearLayout.removeAllViews();
                for (TextView t : arr) {
                    linearLayout.addView(t);
                }

            });
        }
    }

    private void getLastMove() {
        try {
            if (infunc)
                return;
            infunc = true;
            String token = getToken(requireActivity());
//        toast(getContext(),token);
            LastMoveLoader lastMoveLoader = new LastMoveLoader(new LastMoveLoader.LastMoveCallback() {
                @Override
                public void onMoveLoaded(Game game) {
                    try {
                        if (game.getResult().equals("over")) {
                            timer.cancel();
                            timer.purge();
                        }
                        if (!game.getMade_by().equals(GameEngine.myPlayerUserame) && !game.getMade_by().isEmpty()) {
                            GameEngine.game = game;
                            Coordinate pos1 = new Coordinate(game.getFrom_x(), game.getFrom_y(), Board.rotations);
                            Coordinate pos2 = new Coordinate(game.getTo_x(), game.getTo_y(), Board.rotations);
                            //toast(getContext(), game.toString());
                            Board.moveWhenReceived(pos1, pos2, getContext());
                            requireActivity().runOnUiThread(board::invalidate);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onResponseFailed(String errorMessage) {
                    try {
                        toast(requireActivity(), errorMessage);
                        requireActivity().onBackPressed();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            lastMoveLoader.getLastMove(token, GameEngine.game.getId());
            infunc = false;
        } catch (Exception e) {
            e.printStackTrace();
            handler.removeCallbacks(runnable);
        }

    }

    private void callAsynchronousTask() {
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000); //execute in every 50000 ms
    }

    @OnClick({R.id.previousMusic, R.id.pauseMusic, R.id.playMusic, R.id.nextMusic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.previousMusic:
                view.setBackgroundColor(Color.GREEN);
                viewModel.startPrevMusic();
                view.postDelayed(() ->
                        view.setBackgroundColor(Color.TRANSPARENT), TimeUnit.MILLISECONDS.toMillis(500));
                break;
            case R.id.pauseMusic:
                viewModel.setMusicIsPlaying(false);
                break;
            case R.id.playMusic:
                viewModel.setMusicIsPlaying(true);
                break;
            case R.id.nextMusic:
                view.setBackgroundColor(Color.GREEN);
                viewModel.startNextMusic();
                view.postDelayed(() ->
                        view.setBackgroundColor(Color.TRANSPARENT), TimeUnit.MILLISECONDS.toMillis(500));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(runnable);
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        handler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    public void onStart() {
        callAsynchronousTask();
        super.onStart();
    }
}
