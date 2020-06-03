package kz.evilteamgenius.chessapp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;

import kz.evilteamgenius.chessapp.BoardView;
import kz.evilteamgenius.chessapp.Const;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.StompUtils;
import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;
import kz.evilteamgenius.chessapp.engine.GameEngine;
import kz.evilteamgenius.chessapp.engine.Match;
import kz.evilteamgenius.chessapp.fragments.GameFragment;
import kz.evilteamgenius.chessapp.fragments.NavigationPageFragment;
import kz.evilteamgenius.chessapp.fragments.UpdateYourApplicationFragment;
import kz.evilteamgenius.chessapp.models.Game;
import kz.evilteamgenius.chessapp.models.MatchMakingMessage;
import kz.evilteamgenius.chessapp.models.MoveMessage;
import kz.evilteamgenius.chessapp.models.enums.MatchMakingMessageType;
import kz.evilteamgenius.chessapp.service.MusicService;
import kz.evilteamgenius.chessapp.utils.ConnectivityReceiver;
import kz.evilteamgenius.chessapp.viewModels.GameViewModel;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import timber.log.Timber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static kz.evilteamgenius.chessapp.Constants.URL_MAKE_MOVE;
import static kz.evilteamgenius.chessapp.extensions.CheckExtensionKt.checkInternet;
import static kz.evilteamgenius.chessapp.extensions.CheckExtensionKt.getUsername;
import static kz.evilteamgenius.chessapp.extensions.LifecycleExtensionKt.getToken;
import static kz.evilteamgenius.chessapp.extensions.ViewExtensionsKt.startMusicAction;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})

public class MainActivity extends AppCompatActivity implements ServiceConnection, ConnectivityReceiver.ConnectivityReceiverListener {

    public static StompClient stompClient;
    private Fragment fragment;
    static String token;
    // indicates whether the activity is linked to service player.
    private boolean mIsBound = false;
    private MusicService mServ;
    private GameViewModel viewModel;
    private ConnectivityReceiver receiver;
    private boolean isBeenChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token = getToken(this);
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        receiver = new ConnectivityReceiver();
        receiver.setListener(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        //music staff
        startMusicAction(this);
        doBindService();
        viewModel.getMusicValue().observe(this, aBoolean -> {
            if (aBoolean) {
                startMusic();
            } else {
                stopMusic();
            }
        });

        viewModel.checkVersion().observe(this, aBoolean -> {
            if (!aBoolean) {
                isBeenChecked = true;
                replaceFragment(new UpdateYourApplicationFragment());
            } else {
                if (isBeenChecked) {
                    fragment = new NavigationPageFragment();
                    replaceFragment(fragment);
                }
            }
        });

        viewModel.getNextSong().observe(this, aBoolean -> {
            if (aBoolean) {
                nextSong();
            }
        });

        viewModel.getPrevSong().observe(this, aBoolean -> {
            if (aBoolean) {
                prevSong();
            }
        });
        //end music staff
        fragment = new NavigationPageFragment();
        replaceFragment(fragment);
    }

    public static void sendMove(Coordinate old_pos, Coordinate new_pos, boolean ifOver) {
        old_pos = new Coordinate(old_pos.x, old_pos.y, Board.rotations);
        new_pos = new Coordinate(new_pos.x, new_pos.y, Board.rotations);
        GameEngine.game.setMade_by(GameEngine.myPlayerUserame);
        GameEngine.game.setFrom_x(old_pos.x);
        GameEngine.game.setFrom_y(old_pos.y);
        GameEngine.game.setTo_x(new_pos.x);
        GameEngine.game.setTo_y(new_pos.y);
        if (ifOver)
            GameEngine.game.setResult(GameEngine.myPlayerUserame + " wins!");
        new MakeMoveToServer().execute();
    }

    public void connectAndMakeMatch(int last_game_mode) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address);
        StompUtils.lifecycle(stompClient);
        Timber.d("Start connecting to server");
        // Connect to WebSocket server
        stompClient.connect();
        int numberOfPlayers = last_game_mode == 1 || last_game_mode == 2 ? 2 : 4;
        MatchMakingMessage matchMakingMessageToSend = new MatchMakingMessage(MatchMakingMessageType.CONNECT, last_game_mode, null, null);
        Gson gson = new Gson();
        String json = gson.toJson(matchMakingMessageToSend);
        if (!checkInternet(this)) {
            Timber.e("INTERNET CONNECTION LOST");
        }
        stompClient.send(new StompMessage(
                // Stomp command
                StompCommand.SEND,
                // Stomp Headers, Send Headers with STOMP
                // the first header is required, and the other can be customized by ourselves
                Arrays.asList(
                        new StompHeader(StompHeader.DESTINATION, Const.makeMatchAddress),
                        new StompHeader("authorization", getUsername(this))
                ),
                // Stomp payload
                json)
        ).subscribe();
//
        String dest = Const.makeMatchResponse.replace(Const.placeholder, getUsername(this));
        stompClient.topic(dest).subscribe(stompMessage -> {
            MatchMakingMessage matchMakingMessageReceived = new Gson().fromJson(stompMessage.getPayload(), MatchMakingMessage.class);
            //JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
            Timber.d("Received: *****\n %s *****\n", matchMakingMessageReceived.toString());
            if (matchMakingMessageReceived.getMessageType() == MatchMakingMessageType.CONNECTED) {
                Match match = new Match(String.valueOf(System.currentTimeMillis()),
                        last_game_mode, false);
                GameEngine.newGame(match, matchMakingMessageReceived.getPlayers(), getUsername(this), matchMakingMessageReceived.getRoom_id());
                startGame(match.id);
            }
        }, throwable -> {
            Timber.e("Throwable %s", throwable.getMessage());
            if (!checkInternet(this)) {
                Timber.e("ITS INTERNET CONNECTION kuka ");
            }
        });
    }

    public void getMove(BoardView board) {
        stompClient.topic(GameEngine.roomAdress).subscribe(stompMessage -> {
            MoveMessage message = new Gson().fromJson(stompMessage.getPayload(), MoveMessage.class);
            if (message.getPlayerID().equals(GameEngine.myPlayerUserame))
                return;
            Timber.d("Received: *****\n %s ***** \n", message.toString());
            Coordinate pos1 = new Coordinate(message.getFrom_x(), message.getFrom_y(), Board.rotations);
            Coordinate pos2 = new Coordinate(message.getTo_x(), message.getTo_y(), Board.rotations);
            Board.moveWhenReceived(pos1, pos2, MainActivity.this);
            // Stuff that updates the UI
            this.runOnUiThread(board::invalidate);
        }, throwable -> {
            Timber.e("Throwable %s", throwable.getMessage());
            if (!checkInternet(this)) {
                Timber.e("Kuka its internet connection");
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.frame, fragment);
        tr.commitNow();
    }


    public void startGame(final String matchID) {
        Timber.d("startGame");
        fragment = new GameFragment();
        Bundle b = new Bundle();
        b.putString("matchID", matchID);
        fragment.setArguments(b);
        replaceFragment(fragment);
    }


    //start music methods
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        viewModel.setInternetCheck(isConnected);
    }

    public static class MakeMoveToServer extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Timber.d("sendMove 3");
            Game game = GameEngine.game;
            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("id", String.valueOf(game.getId()))
                    .add("player1", game.getPlayer1())
                    .add("player2", game.getPlayer2())
                    .add("player3", game.getPlayer1())
                    .add("player4", game.getPlayer2())
                    .add("FEN", game.getFEN() == null ? "" : game.getFEN())
                    .add("result", game.getResult() == null ? "" : game.getResult())
                    .add("from_x", String.valueOf(game.getFrom_x()))
                    .add("from_y", String.valueOf(game.getFrom_y()))
                    .add("to_x", String.valueOf(game.getTo_x()))
                    .add("to_y", String.valueOf(game.getTo_y()))
                    .add("made_by", game.getMade_by() == null ? "" : game.getMade_by())
                    .add("type", String.valueOf(game.getType()))
                    .build();

            Request request = new Request.Builder()
                    .url(URL_MAKE_MOVE)
                    .post(formBody)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (!result.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(result);
                        Game receivedGame = new Game(jsonObject.getLong("id"),
                                jsonObject.getString("play1"),
                                jsonObject.getString("play2"),
                                jsonObject.getString("play3"),
                                jsonObject.getString("play4"),
                                jsonObject.getString("FEN"),
                                jsonObject.getString("result"),
                                jsonObject.getInt("from_x"),
                                jsonObject.getInt("from_y"),
                                jsonObject.getInt("to_x"),
                                jsonObject.getInt("to_y"),
                                jsonObject.getString("made_by"),
                                jsonObject.getInt("type"));
                        //showToast(receivedGame.toString());
                    } else {
//                        toast(this,"Make move to server failed in Game Activity");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mServ = ((MusicService.ServiceBinder) service).getService();
        mServ.start();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mServ = null;
    }

    public void doUnbindService() {
        // disconnects the service activity.
        if (mIsBound) {
            unbindService(this);
            mIsBound = false;
        }
    }

    public void doBindService() {
        // activity connects to the service.
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopMusic();
        stopCheckInternet();
    }

    private void stopCheckInternet() {
        try {
            if (receiver == null) {
                Timber.d("Receiver Can't unregister a receiver which was never registered");
            } else {
                unregisterReceiver(receiver);
                receiver = null;
            }
        } catch (Exception err) {
            Timber.e("%s --> %s", err.getClass().getName(), err.getMessage());
            Timber.e("Receiver not registerer Couldn't get context");
        }
    }

    private void startCheckInternet() {
        if (receiver != null) {
            Timber.d("Receiver Can't register receiver which already has been registered");
        } else {
            try {
                receiver = new ConnectivityReceiver();
                registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                receiver.setListener(this);
            } catch (Exception err) {
                Timber.e("%s --> %s", err.getClass().getName(), err.getMessage());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startMusic();
        startCheckInternet();
    }

    private void startMusic() {
        if (mServ != null) {
            mServ.start();
        }
    }

    private void stopMusic() {
        if (mServ != null) {
            mServ.pause();
        }
    }

    private void nextSong() {
        if (mServ != null) {
            mServ.startNextSong();
        }
        viewModel.defaultValChanger();
    }

    private void prevSong() {
        if (mServ != null) {
            mServ.startPreviousSong();
        }
        viewModel.defaultValChanger();
    }
    //end music methods
}