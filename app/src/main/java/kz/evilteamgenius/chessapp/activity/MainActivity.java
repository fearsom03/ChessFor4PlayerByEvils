package kz.evilteamgenius.chessapp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.Arrays;

import kz.evilteamgenius.chessapp.BoardView;
import kz.evilteamgenius.chessapp.Const;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.StompUtils;
import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;
import kz.evilteamgenius.chessapp.engine.Game;
import kz.evilteamgenius.chessapp.engine.Match;
import kz.evilteamgenius.chessapp.fragments.GameFragment;
import kz.evilteamgenius.chessapp.fragments.NavigationPageFragment;
import kz.evilteamgenius.chessapp.models.MatchMakingMessage;
import kz.evilteamgenius.chessapp.models.MoveMessage;
import kz.evilteamgenius.chessapp.models.enums.MatchMakingMessageType;
import kz.evilteamgenius.chessapp.models.enums.MoveMessageType;
import kz.evilteamgenius.chessapp.service.MusicService;
import kz.evilteamgenius.chessapp.viewModels.GameViewModel;
import timber.log.Timber;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static kz.evilteamgenius.chessapp.extensions.CheckExtensionKt.checkInternet;
import static kz.evilteamgenius.chessapp.extensions.CheckExtensionKt.getUsername;
import static kz.evilteamgenius.chessapp.extensions.ViewExtensionsKt.startMusicAction;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    public static StompClient stompClient;
    private Fragment fragment;
    // indicates whether the activity is linked to service player.
    private boolean mIsBound = false;
    private MusicService mServ;
    private GameViewModel viewModel;


    public static void sendMove(Coordinate old_pos, Coordinate new_pos, boolean ifOver) {
        Timber.d("Send move!");
        old_pos = new Coordinate(old_pos.x, old_pos.y, Board.rotations);
        new_pos = new Coordinate(new_pos.x, new_pos.y, Board.rotations);
        MoveMessage message = new MoveMessage(old_pos.x, old_pos.y, new_pos.x, new_pos.y, Game.myPlayerUserame, MoveMessageType.OK);
        if (ifOver)
            message.setType(MoveMessageType.OVER);
        Gson gson = new Gson();
        String json = gson.toJson(message);
        stompClient.send(new StompMessage(
                // Stomp command
                StompCommand.SEND,
                // Stomp Headers, Send Headers with STOMP
                // the first header is required, and the other can be customized by ourselves
                Arrays.asList(
                        new StompHeader(StompHeader.DESTINATION, Game.roomAdress),
                        new StompHeader("authorization", Game.myPlayerUserame)
                ),
                // Stomp payload
                json)
        ).subscribe();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
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
        //end music staff
        fragment = new NavigationPageFragment();
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.frame, fragment);
        tr.commitNow();
    }

    public void connectAndMakeMatch(int LAST_SELECTED_MATCH_MODE) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address);
        StompUtils.lifecycle(stompClient);
        Timber.d("Start connecting to server");
        // Connect to WebSocket server
        stompClient.connect();
        int numberOfPlayers = LAST_SELECTED_MATCH_MODE == 1 || LAST_SELECTED_MATCH_MODE == 2 ? 2 : 4;
        MatchMakingMessage matchMakingMessageToSend = new MatchMakingMessage(MatchMakingMessageType.CONNECT, LAST_SELECTED_MATCH_MODE, null, null);
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
                        LAST_SELECTED_MATCH_MODE, false);
                Game.newGame(match, matchMakingMessageReceived.getPlayers(), getUsername(this), matchMakingMessageReceived.getRoom_id());
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
        stompClient.topic(Game.roomAdress).subscribe(stompMessage -> {
            MoveMessage message = new Gson().fromJson(stompMessage.getPayload(), MoveMessage.class);
            if (message.getPlayerID().equals(Game.myPlayerUserame))
                return;
            Timber.d("Received: *****\n %s ***** \n", message.toString());
            Coordinate pos1 = new Coordinate(message.getFrom_x(), message.getFrom_y(), Board.rotations);
            Coordinate pos2 = new Coordinate(message.getTo_x(), message.getTo_y(), Board.rotations);
            Board.moveWhenReceived(pos1, pos2);
            // Stuff that updates the UI
            this.runOnUiThread(board::invalidate);
        }, throwable -> {
            Timber.e("Throwable %s", throwable.getMessage());
            if (!checkInternet(this)) {
                Timber.e("Kuka its internet connection");
            }
        });
    }


    public void startGame(final String matchID) {
        Timber.d("startGame");
        fragment = new GameFragment();
        Bundle b = new Bundle();
        b.putString("matchID", matchID);
        fragment.setArguments(b);
        replaceFragment(fragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        startMusic();
    }

    public void startMusic() {
        if (mServ != null) {
            mServ.start();
        }
    }

    public void stopMusic() {
        if (mServ != null) {
            mServ.pause();
        }
    }
}