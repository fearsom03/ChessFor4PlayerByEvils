package kz.evilteamgenius.chessapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Objects;

import kz.evilteamgenius.chessapp.BoardView;
import kz.evilteamgenius.chessapp.Const;
import kz.evilteamgenius.chessapp.R;
import kz.evilteamgenius.chessapp.StompUtils;
import kz.evilteamgenius.chessapp.engine.Board;
import kz.evilteamgenius.chessapp.engine.Coordinate;
import kz.evilteamgenius.chessapp.engine.Game;
import kz.evilteamgenius.chessapp.engine.Match;
import kz.evilteamgenius.chessapp.fragments.FragmentOptionsFragment;
import kz.evilteamgenius.chessapp.fragments.GameFragment;
import kz.evilteamgenius.chessapp.fragments.NavigationPageFragment;
import kz.evilteamgenius.chessapp.models.MatchMakingMessage;
import kz.evilteamgenius.chessapp.models.MoveMessage;
import kz.evilteamgenius.chessapp.models.enums.MatchMakingMessageType;
import kz.evilteamgenius.chessapp.models.enums.MoveMessageType;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

@SuppressWarnings({"FieldCanBeLocal", "ResultOfMethodCallIgnored", "CheckResult"})

public class KukaActivity extends AppCompatActivity implements NavigationPageFragment.OnFragmentInteractionListener {

    private Fragment fragment;
    public static StompClient stompClient;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuka);
        fragment = new NavigationPageFragment();
        replaceFragment(fragment);
    }


    public void replaceFragment(Fragment fragment) {
        FragmentTransaction tr = this.getSupportFragmentManager().beginTransaction();
        tr.replace(R.id.frame, fragment);
        tr.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void connectAndMakeMatch(int LAST_SELECTED_MATCH_MODE) {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Const.address);
        StompUtils.lifecycle(stompClient);
        System.out.println("Start connecting to server");
        // Connect to WebSocket server
        stompClient.connect();
        int numberOfPlayers = LAST_SELECTED_MATCH_MODE == 1 || LAST_SELECTED_MATCH_MODE == 2 ? 2 : 4;
        MatchMakingMessage matchMakingMessageToSend = new MatchMakingMessage(MatchMakingMessageType.CONNECT, LAST_SELECTED_MATCH_MODE, null, null);
        Gson gson = new Gson();
        String json = gson.toJson(matchMakingMessageToSend);
        stompClient.send(new StompMessage(
                // Stomp command
                StompCommand.SEND,
                // Stomp Headers, Send Headers with STOMP
                // the first header is required, and the other can be customized by ourselves
                Arrays.asList(
                        new StompHeader(StompHeader.DESTINATION, Const.makeMatchAddress),
                        new StompHeader("authorization", getUsername())
                ),
                // Stomp payload
                json)
        ).subscribe();
//
        String dest = Const.makeMatchResponse.replace(Const.placeholder, getUsername());
        stompClient.topic(dest).subscribe(stompMessage -> {
            MatchMakingMessage matchMakingMessageReceived = new Gson().fromJson(stompMessage.getPayload(), MatchMakingMessage.class);
            //JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
            System.out.println("Received: *****\n" + matchMakingMessageReceived.toString() + "*****\n");
            if (matchMakingMessageReceived.getMessageType() == MatchMakingMessageType.CONNECTED) {
                Match match = new Match(String.valueOf(System.currentTimeMillis()),
                        LAST_SELECTED_MATCH_MODE, false);
                Game.newGame(match, matchMakingMessageReceived.getPlayers(), getUsername(), matchMakingMessageReceived.getRoom_id());
                startGame(match.id);
            }
        }, throwable -> Log.e("ConnectAndMakeMatch", "Throwable " + throwable.getMessage()));
    }

    public static void sendMove(Coordinate old_pos, Coordinate new_pos, boolean ifOver) {
        System.out.println("Send move!");
        old_pos = new Coordinate(old_pos.x, old_pos.y,Board.rotations);
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

    public void getMove(BoardView board) {
        stompClient.topic(Game.roomAdress).subscribe(stompMessage -> {
            MoveMessage message = new Gson().fromJson(stompMessage.getPayload(), MoveMessage.class);
            if(message.getPlayerID().equals(Game.myPlayerUserame))
                return;
            System.out.println("Received: *****\n" + message.toString() + "*****\n");
            Coordinate pos1 = new Coordinate(message.getFrom_x(), message.getFrom_y(),Board.rotations);
            Coordinate pos2 = new Coordinate(message.getTo_x(), message.getTo_y(), Board.rotations);
            Board.moveWhenReceived(pos1, pos2);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Stuff that updates the UI
                    board.invalidate();
                }
            });
        }, throwable -> Log.e("getMove", "Throwable " + throwable.getMessage()));
    }

    public String getUsername() {
        SharedPreferences preferences = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    public void startGame(final String matchID) {
        System.out.println("startGame");
        fragment = new GameFragment();
        Bundle b = new Bundle();
        b.putString("matchID", matchID);
        fragment.setArguments(b);
        replaceFragment(fragment);
    }
}